package flow.visibility.pcap;

import java.io.PrintStream;
import java.sql.Timestamp;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jnetpcap.JCaptureHeader;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JFlowMap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

public class FlowProcess {

   private static JFreeChart createChart(XYSeriesCollection dataset) {
	   
	  JFreeChart Chart = ChartFactory.createHistogram("Number Packets of Flows", "Flow Number", "Number of Packets", dataset,PlotOrientation.VERTICAL, false, false, false);
	  Chart.getXYPlot().setForegroundAlpha(0.75f);
			
	  return Chart;
			   
   }
	
   public static JInternalFrame FlowStatistic()
   {
       
       final StringBuilder errbuf = new StringBuilder(); // For any error msgs  
       final String file = "tmp-capture-file.pcap";
	   
	   //System.out.printf("Opening file for reading: %s%n", file);  
       
       /*************************************************************************** 
        * Second we open up the selected file using openOffline call 
        **************************************************************************/  
       Pcap pcap = Pcap.openOffline(file, errbuf);  
 
       if (pcap == null) {  
           System.err.printf("Error while opening device for capture: "  
               + errbuf.toString());
       }  
       
       	Pcap pcap1 = Pcap.openOffline(file, errbuf);
        FlowMap map = new FlowMap();
        pcap1.loop(Pcap.LOOP_INFINITE, map, null);
        
        System.out.printf(map.toString());
        System.out.printf(map.toString2());
           
        String packet = map.toString2();
        String[] NumberPacket = packet.split(",");
           
        final XYSeries Flow = new XYSeries("Flow");
           
        for (int i = 0; i<NumberPacket.length-1; i=i+1) {
               
          	System.out.printf(NumberPacket[i+1] + "\n");
           	double NoPacket = Double.valueOf(NumberPacket[i+1]);
           	Flow.add(i, NoPacket);

        }
                 
		final XYSeriesCollection dataset = new XYSeriesCollection();

   	    dataset.addSeries(Flow);
   	    
   	    JInternalFrame FlowStatistic = new JInternalFrame("Flow Statistic", true, true, true, true);
   	    FlowStatistic.setBounds(0, 0, 600, 330);

   	    ChartPanel chartPanel = new ChartPanel(createChart(dataset));
        chartPanel.setMouseZoomable(true, false);

  	    FlowStatistic.add(chartPanel);
   	    FlowStatistic.setVisible(true);
        FlowStatistic.revalidate();
        pcap1.close();
        
        return FlowStatistic;
        
       }
   
   
   public static JInternalFrame FlowSummary()
   {
       
       final StringBuilder errbuf = new StringBuilder(); // For any error msgs  
       final String file = "tmp-capture-file.pcap";
	   
	   //System.out.printf("Opening file for reading: %s%n", file);  
       
       /*************************************************************************** 
        * Second we open up the selected file using openOffline call 
        **************************************************************************/  
       Pcap pcap = Pcap.openOffline(file, errbuf);  
 
       if (pcap == null) {  
           System.err.printf("Error while opening device for capture: "  
               + errbuf.toString());
       }  
       
       JInternalFrame FlowSummary = new JInternalFrame("Flow Summary", true, true, true, true);
       FlowSummary.setBounds(0, 331, 600, 329);
       JTextArea textArea = new JTextArea(50, 10);
       PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
	        System.setOut(printStream);
	        System.setErr(printStream);
	        JScrollPane scrollPane = new JScrollPane(textArea);
	        FlowSummary.add(scrollPane);
	        
       Pcap pcap2 = Pcap.openOffline(file, errbuf);
       JFlowMap superFlowMap = new JFlowMap(); 
       pcap2.loop(Pcap.LOOP_INFINITE, superFlowMap, null);
	        
       FlowSummary.setVisible(true);
       System.out.printf("%s%n", superFlowMap);
   	
       FlowSummary.revalidate();
       pcap2.close();
       
       return FlowSummary;
       
   }   

   public static JInternalFrame FlowInspection()
   {
       
       final StringBuilder errbuf = new StringBuilder(); // For any error msgs  
       final String file = "tmp-capture-file.pcap";
	   
	   //System.out.printf("Opening file for reading: %s%n", file);  
       
       /*************************************************************************** 
        * Second we open up the selected file using openOffline call 
        **************************************************************************/  
       Pcap pcap = Pcap.openOffline(file, errbuf);  
 
       if (pcap == null) {  
           System.err.printf("Error while opening device for capture: "  
               + errbuf.toString());
       }  

       
       JInternalFrame FlowInspection = new JInternalFrame("Flow Inspection", true, true, true, true);
       FlowInspection.setBounds(601, 0, 600, 660);
       JTextArea textArea2 = new JTextArea(50, 10);
       PrintStream printStream2 = new PrintStream(new CustomOutputStream(textArea2));
	        System.setOut(printStream2);
	        System.setErr(printStream2);
	        JScrollPane scrollPane2 = new JScrollPane(textArea2);
	        FlowInspection.add(scrollPane2);

       
       
		JPacketHandler<String> jpacketHandler = new JPacketHandler<String>() {

			public void nextPacket(JPacket packet, String user) {
				final JCaptureHeader header = packet.getCaptureHeader();
				System.out.printf("========================= Next Packet ===============================\n");
				System.out.printf("Packet caplen=%d wirelen=%d\n", header.caplen(),header.wirelen());
				System.out.println(packet.toString());

			}
		};
       
       Pcap pcap3 = Pcap.openOffline(file, errbuf);
       
       FlowInspection.setVisible(true);
       pcap3.loop(Pcap.LOOP_INFINITE, jpacketHandler, null);
       FlowInspection.revalidate();
       pcap3.close();
       
       return FlowInspection;
       
   }
       

   public static JInternalFrame FlowSequence()
   {
       
       final StringBuilder errbuf = new StringBuilder(); // For any error msgs  
       final String file = "tmp-capture-file.pcap";
	   
	   //System.out.printf("Opening file for reading: %s%n", file);  
       
       /*************************************************************************** 
        * Second we open up the selected file using openOffline call 
        **************************************************************************/  
       Pcap pcap = Pcap.openOffline(file, errbuf);  
 
       if (pcap == null) {  
           System.err.printf("Error while opening device for capture: "  
               + errbuf.toString());
       }  

       
       JInternalFrame FlowSequence = new JInternalFrame("Flow Sequence", true, true, true, true);
       FlowSequence.setBounds(601, 0, 600, 660);
       JTextArea textArea3 = new JTextArea(50, 10);
       PrintStream printStream3 = new PrintStream(new CustomOutputStream(textArea3));
	        System.setOut(printStream3);
	        System.setErr(printStream3);
	        JScrollPane scrollPane3 = new JScrollPane(textArea3);
	        FlowSequence.add(scrollPane3);

       
       
		JPacketHandler<String> jpacketHandler = new JPacketHandler<String>() {

			public void nextPacket(JPacket packet, String user) {
				final JCaptureHeader header = packet.getCaptureHeader();
				Timestamp timestamp = new Timestamp(header.timestampInMillis());
				Tcp tcp = new Tcp();
				Udp udp = new Udp();
				Icmp icmp = new Icmp();
				Ip4 ip4 = new Ip4();
				Ethernet ethernet = new Ethernet();
				
				if (packet.hasHeader(ip4)) {

					if (packet.hasHeader(tcp)) {
						System.out.println(timestamp.toString() + " :  [TCP]  :  " + FormatUtils.ip(ip4.source()) + ":" + tcp.source() + "->" + FormatUtils.ip(ip4.destination())+ ":" + tcp.destination());
					}	
					if (packet.hasHeader(udp)) {
						System.out.println(timestamp.toString() + " :  [UDP]  :  " + FormatUtils.ip(ip4.source()) + ":" + udp.source() + "->" + FormatUtils.ip(ip4.destination())+ ":" + udp.destination());
					}
					if (packet.hasHeader(icmp)) {
						System.out.println(timestamp.toString() + " : [ICMP]  :  " + FormatUtils.ip(ip4.source()) + "->" + FormatUtils.ip(ip4.destination())+ ":" + icmp.type());
					}

				}
				
				else if (packet.hasHeader(ethernet)) {
					System.out.println(timestamp.toString() + " :  [ETH]  :  " + FormatUtils.mac(ethernet.source()) + "->" + FormatUtils.mac(ethernet.destination())+ ":" + ethernet.type());

				}
			}
		};
       
       Pcap pcap4 = Pcap.openOffline(file, errbuf);
       
       FlowSequence.setVisible(true);
       pcap4.loop(Pcap.LOOP_INFINITE, jpacketHandler, null);
       FlowSequence.revalidate();
       pcap4.close();
       
       return FlowSequence;
       
   }

   
   
}


