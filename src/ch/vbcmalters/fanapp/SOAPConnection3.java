package ch.vbcmalters.fanapp;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class SOAPConnection3 {
	

	
	String CallWebService(String url, String soapAction)  {
		
		String envelope="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"<SOAP-ENV:Envelope " +
						"xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
						" xmlns:typens=\"http://myvolley.swissvolley.ch\"" +
						" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
						" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"" +
						" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
						" xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"" +
						" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >"+
	                 "<SOAP-ENV:Body>" +
		                 "<mns:getGamesTeam " +
		                 "xmlns:mns=\"http://myvolley.swissvolley.ch\" " +
		                 "SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
		                 "<team_ID xsi:type=\"xsd:int\">17481</team_ID>" +
		                 "</mns:getGamesTeam>" +
	                 "</SOAP-ENV:Body>" +
                 "</SOAP-ENV:Envelope>"; 
		
		  final DefaultHttpClient httpClient=new DefaultHttpClient();
		  // request parameters
		  HttpParams params = httpClient.getParams();
		     HttpConnectionParams.setConnectionTimeout(params, 10000);
		     HttpConnectionParams.setSoTimeout(params, 15000);
		     // set parameter
		  HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);

		  // POST the envelope
		  HttpPost httppost = new HttpPost(url);
		  // add headers
		     //httppost.setHeader("soapaction", soapAction);
		     httppost.setHeader("Content-Type", "text/xml; charset=utf-8");
		     //httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
		     
		     String responseString="";
		     try {

		      // the entity holds the request
		   HttpEntity entity = new StringEntity(envelope);
		   httppost.setEntity(entity);

		   // Response handler
		   ResponseHandler<String> rh=new ResponseHandler<String>() {
		    // invoked when client receives response
		    public String handleResponse(HttpResponse response)
		      throws ClientProtocolException, IOException {

		     // get response entity
		     HttpEntity entity = response.getEntity();

		     // read the response as byte array
		           StringBuffer out = new StringBuffer();
		           byte[] b = EntityUtils.toByteArray(entity);

		           // write the response byte array to a string buffer
		           out.append(new String(b, 0, b.length));
		           return out.toString();
		    }
		   };

		   responseString=httpClient.execute(httppost, rh); 
		   
		   

		  }
		     catch (Exception e) {
		      Log.v("exception", e.toString());
		  }

		     // close the connection
		  httpClient.getConnectionManager().shutdown();
		  return responseString;
		  
		 
		
		/*
		  try
		  {
			  

	      //Create socket
	      String hostname = "http://80.74.154.73/SwissVolley.wsdl";
	      int port = 80;
	      //InetAddress  addr = InetAddress.getByName(hostname);
	      Socket sock = new Socket("80.74.154.73", port);
				
	      //Send header
	      String path = "/svserver.php";
	      BufferedWriter  wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-8"));
	      // You can use "UTF8" for compatibility with the Microsoft virtual machine.
	      wr.write("POST " + path + " HTTP/1.0\r\n");
	      wr.write("Host: www.swissvolley.ch\r\n");
	      wr.write("Content-Length: " + envelope.length() + "\r\n");
	      wr.write("Content-Type: text/xml; charset=\"utf-8\"\r\n");
	      wr.write("\r\n");
				
	      //Send data
	      wr.write(envelope);
	      wr.flush();
			
	      String response = "";
	      // Response
	      BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	      String line;
	      while((line = rd.readLine()) != null)
	      {
	    	  response += line + "\r\n";
	      }
	      
	      return response;
		  }
		  catch (Exception ex)
		  {	
			return "dini muetter";  
		  }
		  
		  */
	      
	}
	    

	

}
