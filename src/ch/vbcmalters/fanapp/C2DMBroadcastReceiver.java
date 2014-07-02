package ch.vbcmalters.fanapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class C2DMBroadcastReceiver extends BroadcastReceiver {

	
	private static String KEY = "VBCMaltersFanAppPreferences";
	private static String REGISTRATION_KEY = "registrationKey";
	private static String GENERALLNOTIFICATION = "generallNotification";
	
	
	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
	    this.context = context;
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
	        handleRegistration(context, intent);
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	        handleMessage(context, intent);
	    }
	 }

	private void handleRegistration(Context context, Intent intent) {
	    String registration = intent.getStringExtra("registration_id");
	    if (intent.getStringExtra("error") != null) {
	        // Registration failed, should try again later.
		    Log.d("c2dm", "registration failed");
		    String error = intent.getStringExtra("error");
		    if(error == "SERVICE_NOT_AVAILABLE"){
		    	Log.d("c2dm", "SERVICE_NOT_AVAILABLE");
		    }else if(error == "ACCOUNT_MISSING"){
		    	Log.d("c2dm", "ACCOUNT_MISSING");
		    }else if(error == "AUTHENTICATION_FAILED"){
		    	Log.d("c2dm", "AUTHENTICATION_FAILED");
		    }else if(error == "TOO_MANY_REGISTRATIONS"){
		    	Log.d("c2dm", "TOO_MANY_REGISTRATIONS");
		    }else if(error == "INVALID_SENDER"){
		    	Log.d("c2dm", "INVALID_SENDER");
		    }else if(error == "PHONE_REGISTRATION_ERROR"){
		    	Log.d("c2dm", "PHONE_REGISTRATION_ERROR");
		    }
	    } else if (intent.getStringExtra("unregistered") != null) {
	        // unregistration done, new messages from the authorized sender will be rejected
	    	Log.d("c2dm", "unregistered");

	    } else if (registration != null) {
	    	
	    	
	    	final SharedPreferences settings = context.getSharedPreferences(KEY, 0);
	    	final String oldRegKey = settings.getString(REGISTRATION_KEY, "asdf");   
	    	final String newRegkey = registration;
	    	
		    // Send the registration ID to the 3rd party site that is sending the messages.
	    	if (!oldRegKey.equals(registration))
	    	{
		    	Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		            editor.putString(REGISTRATION_KEY, registration);
		    		editor.commit();
		    		
		    	new Thread(new Runnable() {
	    		    public void run() {	
	    		    	registerOnServer(oldRegKey, newRegkey);
	    		    }
	    		  }).start();
	    	}
	    }
	}

	private void handleMessage(Context context, Intent intent)
	{
		//Do whatever you want with the message

    	final SharedPreferences settings = context.getSharedPreferences(KEY, 0);
    	
    	if (settings.getBoolean(GENERALLNOTIFICATION, true))
    	{
    		createNotification(context, "test");
    	}
		
	}
	
	public void createNotification(Context context, String payload)
	{
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.logo_notifi,
				"News vom VBC Malters", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		
		//notification.ledARGB = 0xffff0000;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		Intent intent = new Intent(context, VBCMaltersFanAppActivity.class);
		intent.putExtra("payload", payload);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, "News vom VBC Malters",
				"Es gibt News im VBC Malters Fan App", pendingIntent);
		notificationManager.notify(0, notification);

	}
	
    private void registerOnServer(String oldRegKey, String newRegKey)
    {

    		try 
    		{
	            // Create a new HttpClient and Post Header
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost("http://hslu.ath.cx/boli/RegisterRegistrationID.php");
	            
	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("registrationID", newRegKey));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            
	            
	            
	            // Execute HTTP Post Request
	            HttpResponse response = httpclient.execute(httppost);
			} 
    		catch (UnsupportedEncodingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    }
}