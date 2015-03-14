/** Copyright (C) GIST 2015
 * This Software is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation.
 */

package flow.visibility.tapping;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import flow.visibility.pcap.CustomOutputStream;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
*
* The class for queryng inserting the flow entry to the OpenFlow Controller. 
* Developing by utilizing OpenDayLight REST API.
*
* @authors Aris Cahyadi Risdianto
*/

public class OpenDayLightUtils extends JFrame {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea FlowtextArea;
	 private JScrollPane scrollPane;
	 
	 /** Creating the Pane for Flow Entry */ 
	 
	 public OpenDayLightUtils() {
	      super("Installed Flow");

	      FlowtextArea = new JTextArea(50, 10);
	      FlowtextArea.setEditable(false);
	      FlowtextArea.setFont(new Font("Courier New", Font.BOLD,12));
	      PrintStream FlowprintStream = new PrintStream(new CustomOutputStream(FlowtextArea));

	      // re-assigns standard output stream and error output stream
	      System.setOut(FlowprintStream);
	      System.setErr(FlowprintStream);

	      // creates the GUI
	      setLayout(new GridBagLayout());
	      GridBagConstraints constraints = new GridBagConstraints();
	      constraints.gridx = 0;
	      constraints.gridy = 0;
	      constraints.insets = new Insets(10, 10, 10, 10);
	      constraints.anchor = GridBagConstraints.WEST;
	      constraints.gridx = 0;
	      constraints.gridy = 1;
	      constraints.gridwidth = 2;
	      constraints.fill = GridBagConstraints.BOTH;
	      constraints.weightx = 1.0;
	      constraints.weighty = 1.0;

	      /** Adding the Pane into Frame */ 
	      
	      scrollPane = new JScrollPane(FlowtextArea);
	      this.add(scrollPane, constraints);

          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      setSize(400, 600);
	      setLocationRelativeTo(null);    // centers on screen
	}

	
	/** The function for inserting the flow */ 
	 
    public static void FlowEntry(String ODPURL, String ODPAccount, String ODPPassword) throws Exception
    {

    //String user = "admin";
    //String password = "admin";
    String baseURL = "http://" + ODPURL + "/controller/nb/v2/flowprogrammer";
    String containerName = "default";

    /** Check the connection for ODP REST API */ 
    
    try {

        // Create URL = base URL + container
        URL url = new URL(baseURL + "/" + containerName);

        // Create authentication string and encode it to Base64
        String authStr = ODPAccount + ":" + ODPPassword;
        String encodedAuthStr = Base64.encodeBase64String(authStr.getBytes());

        // Create Http connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set connection properties
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthStr);
        connection.setRequestProperty("Accept", "application/json");

        // Get the response from connection's inputStream
        InputStream content = (InputStream) connection.getInputStream();

        /** The create the frame and blank pane */ 
        OpenDayLightUtils object = new OpenDayLightUtils();
	    object.setVisible(true);
        
	    /** Read the Flow entry in JSON Format */ 
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        String line = "";
        
        /** Print line by line in Pane with Pretty JSON */ 
        while ((line = in.readLine()) != null) {
  
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
        	
        	Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        	String json = gson.toJson(jsonObject);
        	System.out.println(json);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}