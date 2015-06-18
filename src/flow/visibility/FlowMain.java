/** Copyright (C) GIST 2015
 * This Software is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation.
 */

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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jethereal.Ethereal;
import flow.visibility.pcap.FlowDumper;
import flow.visibility.pcap.FlowProcess;
import flow.visibility.tapping.FlowTapping;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

import netgrok.view.network.NetworkView;

/**
*
* The main window GUI for Visibility Tools. 
* Contains menu and internal frame which updated regularly
* by processing Pcap File using JNETPCAP Library.
*
*
* @authors Aris Cahyadi Risdianto
*/


// Main Class

public class FlowMain  {

   private static JFreeChart createChart(XYSeriesCollection dataset) {
		   
   	    JFreeChart Chart = ChartFactory.createHistogram("Number Packets of Flows", "Flow Number", "Number of Packets", dataset,PlotOrientation.VERTICAL, false, false, false);
        Chart.getXYPlot().setForegroundAlpha(0.75f);
		
        return Chart;
		   
   }
   

// Main Function
   
   public static void main(String[] args) throws Exception { 
	   
	   
        /************************************************************** 
         * Creating the Main GUI 
         **************************************************************/  
       
        final JDesktopPane desktop = new JDesktopPane();
        
		final JMenuBar mb = new JMenuBar();
		JMenu menu;
		
		/** Add File Menu to Open File and Save Result */
		
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
		
		/** Add Control Menu to Start and Stop Capture */
		
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
		
		/** Add Configuration Menu for Tapping and Display */
		
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
		
		/** Add Detail Menu for NetGrok Visualization and JEthereal Inspection */
		
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
            	Ethereal.main(null);
            }
        });
		mb.add(menu);
		
		/** Add Help Menu for Software Information */
		menu = new JMenu("Help");
		JMenuItem About = new JMenuItem("About");
		menu.add(About);
		About.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, "OF@TEIN Flow Visibility Tools @ 2015 by GIST", 
            			"About the Software", JOptionPane.PLAIN_MESSAGE);
            }
        });
		mb.add(menu);
		

		/** Creating the main frame */
        JFrame frame = new JFrame("OF@TEIN Flow Visibility Tools");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(desktop);
        frame.setJMenuBar(mb);
        frame.setSize(1215, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        /**Add Blank three (3) Internal Jframe*/
        
        /**Internal Frame from Flow Summary Chart*/
   	    JInternalFrame FlowStatistic = new JInternalFrame("Flow Statistic", true, true, true, true);
   	    FlowStatistic.setBounds(0, 0, 600, 330);
   	    ChartPanel chartPanel = new ChartPanel(createChart(null));
        chartPanel.setMouseZoomable(true, false);
  	    FlowStatistic.add(chartPanel);
   	    FlowStatistic.setVisible(true);
   	    desktop.add(FlowStatistic);
        
   	    /**Internal Frame from Flow Summary Text*/
        JInternalFrame FlowSummary = new JInternalFrame("Flow Summary", true, true, true, true);
        FlowSummary.setBounds(0, 331, 600, 329);
        JTextArea textArea = new JTextArea(50, 10);
 	    JScrollPane scrollPane = new JScrollPane(textArea);
 	    FlowSummary.add(scrollPane);
 	    FlowSummary.setVisible(true);
 	    desktop.add(FlowSummary);
        
        //JInternalFrame FlowInspection = new JInternalFrame("Flow Inspection", true, true, true, true);
        //FlowInspection.setBounds(601, 0, 600, 660);
        //JTextArea textArea2 = new JTextArea(50, 10);
 	    //JScrollPane scrollPane2 = new JScrollPane(textArea2);
 	    //FlowInspection.add(scrollPane2);
        //FlowInspection.setVisible(true);
        //desktop.add(FlowInspection);
        
 	    /**Internal Frame from Printing the Packet Sequence*/
        JInternalFrame FlowSequence = new JInternalFrame("Flow Sequence", true, true, true, true);
        FlowSequence.setBounds(601, 0, 600, 660);
        JTextArea textArea3 = new JTextArea(50, 10);
 	    JScrollPane scrollPane3 = new JScrollPane(textArea3);
 	    FlowSequence.add(scrollPane3);
 	    FlowSequence.setVisible(true);
        desktop.add(FlowSequence);
        
        
        /************************************************************** 
         * Update the Frame Regularly
         **************************************************************/
       
        /** Regularly update the Frame Content every 3 seconds */
    	    
        for ( ; ; ) {
        	
       	    desktop.removeAll();
       	    desktop.add(FlowProcess.FlowStatistic());
       	    desktop.add(FlowProcess.FlowSummary());
       	    //desktop.add(FlowProcess.FlowInspection());
       	    desktop.add(FlowProcess.FlowSequence());
       	    desktop.revalidate();
        	Thread.sleep(10000);
       	    
        }
       
}
        
        
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
        

}
