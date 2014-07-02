package ch.vbcmalters.fanapp;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class Notification 
{

    public static void SendPushNotification()
    {
    	
    	new Thread(new Runnable() 
    	{
		    public void run() 
		    {	
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpPost httpPost = new HttpPost("http://hslu.ath.cx/boli/SendPushNotification.php");
		        //HttpPost httpPost = new HttpPost("http://hslu.ath.cx/boli/TestToMyDevice.php");

		        try 
		        {
					HttpResponse httpResponse = httpClient.execute(httpPost);
				} 
		        catch (ClientProtocolException e) 
		        {
		        } 
		        catch (IOException e) 
		        {
		        }
		    }
		  }).start();
    }
}
