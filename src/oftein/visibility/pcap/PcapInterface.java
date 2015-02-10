package oftein.visibility.pcap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class PcapInterface {

    public static int InterfaceSelection() {
    	
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
        return index;
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
}