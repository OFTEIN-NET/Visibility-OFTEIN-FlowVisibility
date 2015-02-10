// ThreadDemo.java

package oftein.visibility.pcap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapIf;

public class FlowDumper
{
   public static void main (boolean Stop)
   {
	   
	   final FlowDumperThread mt = new FlowDumperThread();
       
	   if (!Stop){
		   mt.start();
		   System.out.println("Start Capture");
	   }
		
	   else {
		   mt.requestStop();
		   System.out.println("Stop Capture");
	   }
   }
}

class FlowDumperThread extends Thread
{
	
   private volatile boolean stop = false;
   
   public void run ()
   {
  
	while (!stop) {
	   
		List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
		StringBuilder errbuf = new StringBuilder(); // For any error msgs

		/***************************************************************************
		 * First get a list of devices on this system
		 **************************************************************************/
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf
			    .toString());
			
		}

		System.out.println("Network devices found:");

		int TotalInterface = 0;
		for (PcapIf device : alldevs) {
			TotalInterface++;
		}
				
		System.out.println(TotalInterface);
		String[] choices = new String[TotalInterface];
		
		int i = 0;
		for (PcapIf device : alldevs) {
			String description =
			    (device.getDescription() != null) ? device.getDescription()
			        : "No description available";
			    System.out.println(i);    
			System.out.printf("#%d: %s [%s]\n", i, device.getName(), description);
			choices[i] = device.getName()+ " : " + description;
			i++;
			System.out.println(i);
		}

        String choice = InterfaceSelection(choices);
        
        int index = Arrays.asList(choices).indexOf(choice);
        System.out.println("index: " + index);
        System.out.println("selected: " + choice);
	    
		PcapIf device = alldevs.get(index); // We know we have at least 1 device
		
		
	    /***************************************************************************
	     * Second we open up the selected device
	     **************************************************************************/
	    int snaplen = 64 * 1024;           // Capture all packets, no trucation
	    int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
	    int timeout = 10 * 1000;           // 10 seconds in millis
	    Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
	    if (pcap == null) {
	      System.err.printf("Error while opening device for capture: %s\n", 
	        errbuf.toString());
	      return;
	    }
			
	    /***************************************************************************
	     * Third we create a PcapDumper and associate it with the pcap capture
	     ***************************************************************************/
	    String ofile = "tmp-capture-file.pcap";
	    PcapDumper dumper = pcap.dumpOpen(ofile); // output file


	    /***************************************************************************
	     * Fifth we enter the loop and tell it to capture 10 packets. We pass
	     * in the dumper created in step 3
	     **************************************************************************/
	    pcap.loop(0, dumper);
			
	    File file = new File(ofile);
	    System.out.printf("%s file has %d bytes in it!\n", ofile, file.length());
			

	    /***************************************************************************
	     * Last thing to do is close the dumper and pcap handles
	     **************************************************************************/
	    dumper.close(); // Won't be able to delete without explicit close
	    pcap.close();
			
	    if (file.exists()) {
	      file.delete(); // Cleanup
	    }
	}
	
	if (stop)
		System.out.println("Detected stop");
	
   }
   
   static String InterfaceSelection(String[] choices) {
       String s = (String) JOptionPane.showInputDialog(
               null,
               "Please select your Capture Interface",
               "Capture Interface Selection",
               JOptionPane.PLAIN_MESSAGE,
               null,
               choices,
               choices[0]);
       return s;
   }

   
   public void requestStop() {
	    stop = true;
   }
   
}