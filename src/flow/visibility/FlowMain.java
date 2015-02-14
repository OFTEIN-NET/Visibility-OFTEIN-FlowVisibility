package flow.visibility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import flow.visibility.pcap.FlowDumper;
import flow.visibility.pcap.FlowProcess;
import flow.visibility.tapping.FlowTapping;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

import netgrok.view.network.NetworkView;

public class FlowMain  {

   private static JFreeChart createChart(XYSeriesCollection dataset) {
		   
   	    JFreeChart Chart = ChartFactory.createHistogram("Number Packets of Flows", "Flow Number", "Number of Packets", dataset,PlotOrientation.VERTICAL, false, false, false);
        Chart.getXYPlot().setForegroundAlpha(0.75f);
		
        return Chart;
		   
   }
   

   public static void main(String[] args) throws Exception { 
	   
	   
        /*************************************************************************** 
         * Creating the Main GUI 
         **************************************************************************/  
       
        final JDesktopPane desktop = new JDesktopPane();
        
		final JMenuBar mb = new JMenuBar();
		JMenu menu;
		
		menu = new JMenu("File");
		JMenuItem Open = new JMenuItem("Open");
		JMenuItem Save = new JMenuItem("Save");
		menu.add(Open);
		menu.add(Save);

		menu.addSeparator();
		JMenuItem Exit = new JMenuItem("Exit");
		menu.add(Exit);
	    Exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
		
		mb.add(menu);
		
		menu = new JMenu("Control");
		JMenuItem Start = new JMenuItem("Start Capture");
		JMenuItem Stop = new JMenuItem("Stop Capture");
		menu.add(Start);
   	    Start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                FlowDumper.main(false);
            }
        });

	
		menu.add(Stop);
		Stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                FlowDumper.main(true);
            }
        });

		
		
		mb.add(menu);
		
		menu = new JMenu("Configuration");
		JMenuItem Tapping = new JMenuItem("Tapping Configuration");
		JMenuItem Display = new JMenuItem("Display Configuration");
		menu.add(Tapping);
		Tapping.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                FlowTapping.main(null);
            }
        });
		menu.add(Display);
		
		mb.add(menu);
		
		menu = new JMenu("Flow Detail");
		JMenuItem FlowVisual = new JMenuItem("Flow Visualization");
		JMenuItem FlowInspect = new JMenuItem("Flow Inspections");
		menu.add(FlowVisual);
		FlowVisual.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	NetworkView.main(null);
            }
        });
		menu.add(FlowInspect);
		FlowInspect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
		mb.add(menu);
		
		menu = new JMenu("Help");
		menu.add(new JMenuItem("About"));
		mb.add(menu);
		

        JFrame frame = new JFrame("OF@TEIN Flow Visibility Tools");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(desktop);
        frame.setJMenuBar(mb);
        frame.setSize(1215, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //Add Blank Internal Jframe
        
   	    JInternalFrame FlowStatistic = new JInternalFrame("Flow Statistic", true, true, true, true);
   	    FlowStatistic.setBounds(0, 0, 600, 330);
   	    ChartPanel chartPanel = new ChartPanel(createChart(null));
        chartPanel.setMouseZoomable(true, false);
  	    FlowStatistic.add(chartPanel);
   	    FlowStatistic.setVisible(true);
   	    desktop.add(FlowStatistic);
        
        JInternalFrame FlowSummary = new JInternalFrame("Flow Summary", true, true, true, true);
        FlowSummary.setBounds(0, 331, 600, 329);
        JTextArea textArea = new JTextArea(50, 10);
 	    JScrollPane scrollPane = new JScrollPane(textArea);
 	    FlowSummary.add(scrollPane);
 	    FlowSummary.setVisible(true);
 	    desktop.add(FlowSummary);
        
        JInternalFrame FlowInspection = new JInternalFrame("Flow Inspection", true, true, true, true);
        FlowInspection.setBounds(601, 0, 600, 660);
        JTextArea textArea2 = new JTextArea(50, 10);
 	    JScrollPane scrollPane2 = new JScrollPane(textArea2);
 	    FlowInspection.add(scrollPane2);
        FlowInspection.setVisible(true);
        desktop.add(FlowInspection);
        
        
       // Regularly update the Frame Content
    	    
        for ( ; ; ) {
        	
       	    desktop.removeAll();
       	    desktop.add(FlowProcess.FlowStatistic());
       	    desktop.add(FlowProcess.FlowSummary());
       	    desktop.add(FlowProcess.FlowInspection());
       	    desktop.revalidate();
        	Thread.sleep(3000);
       	    
        }
       
}
        
        
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
        

}
