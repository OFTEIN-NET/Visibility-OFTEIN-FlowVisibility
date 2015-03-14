/** Copyright (C) GIST 2015
 * This Software is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation.
 */

package flow.visibility.tapping;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
*
* The class for inserting the flow to the OpenFlow Controller. 
* Developing by utilizing OpenDayLight REST API
*
* @authors Aris Cahyadi Risdianto
*/

public class OpenDaylightHelper {

   /** The function for inserting the flow */ 
	
	public static boolean installFlow(JSONObject postData, String user,
            String password, String baseURL) {

        StringBuffer result = new StringBuffer();
        
        /** Check the connection to ODP REST API page */
        
        try {

            if (!baseURL.contains("http")) {
                baseURL = "http://" + baseURL;
            }
            baseURL = baseURL
                    + "/controller/nb/v2/flowprogrammer/default/node/OF/"
                    + postData.getJSONObject("node").get("id") + "/staticFlow/"
                    + postData.get("name");

            /** Create URL = base URL + container */
            URL url = new URL(baseURL);

            /** Create authentication string and encode it to Base64*/
            String authStr = user + ":" + password;
            String encodedAuthStr = Base64.encodeBase64String(authStr
                    .getBytes());

            /** Create Http connection */
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            /** Set connection properties */
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Basic "
                    + encodedAuthStr);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            /** Set JSON Post Data */
            OutputStream os = connection.getOutputStream();
            os.write(postData.toString().getBytes());
            os.close();

            /** Get the response from connection's inputStream */
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    content));
            String line = "";
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /** checking the result of REST API connection */
        
        if ("success".equalsIgnoreCase(result.toString())) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void main(String ODPURL, String ODPAccount, String ODPPassword, String DPID, String Flow, String Priority, String Ingress, String Outgress, String Filter1, String Filter2, String Filter3, String Filter4, String Filter5, String Filter6) throws JSONException {
        //OpenDaylight Data
    	//String ODPURL = "103.22.221.152:8080";
    	//String ODPAccount = "admin";
    	//String ODPPassword = "admin";
    	
    	/**Sample post data (refer to ODP REST API) */
    	
        JSONObject postData = new JSONObject();
        postData.put("installInHw", "true");
        postData.put("name", Flow);
        postData.put("ingressPort", Ingress);
        postData.put("priority", Priority);
        postData.put("etherType", "0x800");
        
        //postData.put("nwSrc", Filter1);
        if (! Filter1.equals("")) {
        	postData.put("nwSrc", Filter1);
        }
        
        //postData.put("nwDst", Filter2);
        if (! Filter2.equals("")) {
        	postData.put("nwDst", Filter2);
        }
        
        if (! Filter3.equals("")) {
        	postData.put("protocol", Filter3);
        }
        if (! Filter4.equals("")) {
        	postData.put("tpSrc", Filter4);
        }
        if (! Filter5.equals("")) {
        	postData.put("tpDst", Filter5);
        }
        if (! Filter6.equals("")) {
        	postData.put("vlanId", Filter6);
        }

        /** Make string for OpenFlow Action */
        String Action = "OUTPUT=" + Outgress;
        postData.put("actions", new JSONArray().put(Action));
        
        /** Select Node on which this flow should be installed */
        JSONObject node = new JSONObject();
        node.put("id", DPID);
        node.put("type", "OF");
        postData.put("node", node);
        
        /** Actual flow installation */
        boolean result = OpenDaylightHelper.installFlow(postData, ODPAccount, ODPPassword, ODPURL);
        
        /** The Result of the Flow Installation */
        
        if(result){
            System.out.println("Flow installed Successfully");
            JOptionPane.showMessageDialog(null, "Flow installed Successfully.", "Successful Message", JOptionPane.PLAIN_MESSAGE);
        }else{
            System.err.println("Failed to install flow!");
            JOptionPane.showMessageDialog(null, "Failed to install flow!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }

    }
}