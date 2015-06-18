/** Copyright (C) GIST 2015
 * This Software is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation.
 */

package flow.visibility.tapping;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
*
* The main window GUI for Flow Tapping Applications. 
* Contains field for OpenFlow (OpenDayLight) Controller
* information and flow parameter (filter and redirect).
* The action button execute through other class for 
* querying ang inserting the flow from the controller.
*
* @authors Aris Cahyadi Risdianto
*/

// Main Class with Java Panel
public class FlowTapping extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
/************************************************************** 
 * Creating Main Panel GUI 
 **************************************************************/	
	
public FlowTapping(JButton Exit) {

	/**Create panel for ODP configuration*/
	
	JPanel panel0 = new JPanel(new GridLayout(3,2));
	panel0.setPreferredSize(new Dimension(380, 90));
	panel0.setBorder(BorderFactory.createTitledBorder("OpenDayLight Configuration"));
	JLabel ODPURLLabel = new JLabel("URL [IP:Port]");
	JLabel ODPAccountLabel = new JLabel("Account");
	JLabel ODPPasswordLabel = new JLabel("Password");
	final JTextField ODPURL = new JTextField();
	final JTextField ODPAccount = new JTextField();
	final JPasswordField ODPPassword = new JPasswordField();

	//Create panel for Tapping (Filter and Redirect) configuration
	JPanel panel1 = new JPanel(new GridLayout(11,2));
	panel1.setPreferredSize(new Dimension(380, 330));
	panel1.setBorder(BorderFactory.createTitledBorder("Tapping Configuration"));
	JLabel DPIDLabel = new JLabel("Datapath ID");
	JLabel FlowLabel = new JLabel("Flow Name");
	JLabel PriorityLabel = new JLabel("Flow Priority");
	JLabel IngressLabel = new JLabel("Input Port");
	JLabel OutgressLabel = new JLabel("Output Port");
	JLabel Filter1Label = new JLabel("Source IP Address");
	JLabel Filter2Label = new JLabel("Destination IP Address");
	JLabel Filter3Label = new JLabel("Protocol");
	JLabel Filter4Label = new JLabel("Source Port");
	JLabel Filter5Label = new JLabel("Destination Port");
	JLabel Filter6Label = new JLabel("VLAN ID");
	
	final JTextField DPID = new JTextField();
	final JTextField Flow = new JTextField();
	final JTextField Priority = new JTextField();
	final JTextField Ingress = new JTextField();
	final JTextField Outgress = new JTextField();
	final JTextField Filter1 = new JTextField();
	final JTextField Filter2 = new JTextField();
	final JTextField Filter3 = new JTextField();
	final JTextField Filter4 = new JTextField();
	final JTextField Filter5 = new JTextField();
	final JTextField Filter6 = new JTextField();
	
	JButton FlowEntry = new JButton("Check Connection & Flow Entry");
	JButton Apply = new JButton("Apply Tapping Policy");
	//JButton Exit = new JButton("Exit");
	
	//FlowEntry.addActionListener(new ButtonListener());
	//Apply.addActionListener(new ButtonListener());
	//Exit.addActionListener(new ButtonListener());
	
	/**Define actions for the buttons (Querying and Inserting the Flow) */
	
	FlowEntry.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			try {
				final String ODPURLValue = ODPURL.getText();
				final String ODPAccountValue = ODPAccount.getText();
				final String ODPPasswordValue = String.valueOf(ODPPassword.getPassword());
				OpenDayLightUtils.FlowEntry(ODPURLValue, ODPAccountValue, ODPPasswordValue);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			
			//JOptionPane.showMessageDialog(null, "   Still under Constructions !!!");
		}
	});
	
	Apply.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			
			/**Collect the input when press the "Apply" button*/
			
			final String ODPURLValue = ODPURL.getText();
			final String ODPAccountValue = ODPAccount.getText();
			final String ODPPasswordValue = String.valueOf(ODPPassword.getPassword());
			final String DPIDValue = DPID.getText();
			final String FlowValue = Flow.getText();
			final String PriorityValue = Priority.getText();
			final String IngressValue = Ingress.getText();
			final String OutgressValue = Outgress.getText();
			final String Filter1Value = Filter1.getText();
			final String Filter2Value = Filter2.getText();
			final String Filter3Value = Filter3.getText();
			final String Filter4Value = Filter4.getText();
			final String Filter5Value = Filter5.getText();
			final String Filter6Value = Filter6.getText();
			
			try {

				OpenDaylightHelper.main(ODPURLValue, ODPAccountValue, ODPPasswordValue, DPIDValue, FlowValue, PriorityValue, IngressValue, OutgressValue, Filter1Value, Filter2Value, Filter3Value, Filter4Value, Filter5Value, Filter6Value);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			//JOptionPane.showMessageDialog(null, "   Still under Constructions !!!");
		}
	});
	
/*	Exit.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			try {
				FlowMain.main(null);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			//JOptionPane.showMessageDialog(null, "   Still under Constructions !!!");
		}
	});*/
	
	
	/** Add all elements into Panel */
	
	panel0.add(ODPURLLabel);
	panel0.add(ODPURL);
	panel0.add(ODPAccountLabel);
	panel0.add(ODPAccount);
	panel0.add(ODPPasswordLabel);
	panel0.add(ODPPassword);
	
	panel1.add(DPIDLabel);
	panel1.add(DPID);
	panel1.add(FlowLabel);
	panel1.add(Flow);
	panel1.add(PriorityLabel);
	panel1.add(Priority);
	panel1.add(IngressLabel);
	panel1.add(Ingress);
	panel1.add(OutgressLabel);
	panel1.add(Outgress);
	panel1.add(Filter1Label);
	panel1.add(Filter1);
	panel1.add(Filter2Label);
	panel1.add(Filter2);
	panel1.add(Filter3Label);
	panel1.add(Filter3);
	panel1.add(Filter4Label);
	panel1.add(Filter4);
	panel1.add(Filter5Label);
	panel1.add(Filter5);
	panel1.add(Filter6Label);
	panel1.add(Filter6);
	
	add(panel0);
	add(FlowEntry);
	add(panel1);
	add(Apply);
	add(Exit);
	
  }

  /** Main Function to create the Frame*/

  public static void main(String[] args) {

	//Initialize the frame  
	final JFrame frame = new JFrame("Flow Tapping");
	JButton Exit = new JButton("Exit");
	//Get the content and configure the frame 
    frame.getContentPane().add(new FlowTapping(Exit));
  
    /** Action Button for Closing the frame*/
    
	Exit.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			try {
				frame.dispose();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			//JOptionPane.showMessageDialog(null, "   Still under Constructions !!!");
		}
	});
    
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(400, 540);
    frame.setVisible(true);
  }
}
