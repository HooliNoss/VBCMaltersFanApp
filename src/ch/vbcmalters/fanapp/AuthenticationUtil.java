package ch.vbcmalters.fanapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AuthenticationUtil {
	
	private static String AuthKey = "";
	
	private AuthenticationUtil() {
		// Util class cannot get instanziated
	}

	public static String getToken(final String email,final  String password)
			throws IOException {
		
    	new Thread(new Runnable() 
    	{
		    public void run() 
		    {	
		        try 
		        {
		        	// Create the post data
		    		// Requires a field with the email and the password
		    		StringBuilder builder = new StringBuilder();
		    		builder.append("Email=").append(email);
		    		builder.append("&Passwd=").append(password);
		    		builder.append("&accountType=GOOGLE");
		    		builder.append("&source=vbcmaltersfanapp");
		    		builder.append("&service=ac2dm");

		    		// Setup the Http Post
		    		byte[] data = builder.toString().getBytes();
		    		URL url = new URL("https://www.google.com/accounts/ClientLogin");
		    		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		    		con.setUseCaches(false);
		    		con.setDoOutput(true);
		    		con.setRequestMethod("POST");
		    		con.setRequestProperty("Content-Type",
		    				"application/x-www-form-urlencoded");
		    		con.setRequestProperty("Content-Length", Integer.toString(data.length));

		    		// Issue the HTTP POST request
		    		OutputStream output = con.getOutputStream();
		    		output.write(data);
		    		output.close();

		    		// Read the response
		    		BufferedReader reader = new BufferedReader(new InputStreamReader(
		    				con.getInputStream()));
		    		String line = null;
		    		String auth_key = null;
		    		while ((line = reader.readLine()) != null) {
		    			if (line.startsWith("Auth=")) {
		    				auth_key = line.substring(5);
		    			}
		    		}

		    		// Finally get the authentication token
		    		// To something useful with it
		    		AuthKey = auth_key;
				} 
		        catch (ClientProtocolException e) 
		        {
		        	String message = e.getMessage();
		        } 
		        catch (IOException e) 
		        {
		        	String message = e.getMessage();
		        }
		    }
		  }).start();
    	
    	while (AuthKey == "")
    	{
    		
    	}

    	return AuthKey;
	}
	
}
