package ch.vbcmalters.fanapp;

import java.io.IOException;
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

import com.sun.xml.internal.bind.v2.model.core.NonElement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PostComment extends Activity{

    TextView lblCreator;
    TextView lblBody;
    
    EditText txtCreator;
    EditText txtBody;
    
    Button btnSend;
    
    String mnewsID = "0";
    
    HttpClient httpclient;
    HttpPost httppost;
    ProgressDialog progressDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postcomment);
        
        Bundle extras = getIntent().getExtras();
        if (extras == null) 
        {
        		return;
        }
        
        mnewsID = extras.getString("newsID");
        
        init();
    }
    
    private void init()
    {
    	// Capture our button from layout
        
        lblCreator = (TextView)findViewById(R.id.lblCreator);
        lblBody = (TextView)findViewById(R.id.lblBody);
        
        txtCreator = (EditText)findViewById(R.id.txtCreator);
        txtBody = (EditText)findViewById(R.id.txtBody);
        
        btnSend = (Button)findViewById(R.id.btnSenden);
        
        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
        Button actionbar_AddNews = (Button)findViewById(R.id.actionbarFunk1);
        Button actionbar_send = (Button)findViewById(R.id.actionbarFunk2);
        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
        actionbar_AddNews.setBackgroundResource(android.R.drawable.ic_input_add);
        actionbar_title.setText("Kommentar hinzufügen");
        
        actionbar_AddNews.setVisibility(View.GONE);
        actionbar_send.setVisibility(View.GONE);
        actionbar_home.setOnClickListener(btnHomeListener);
        btnSend.setOnClickListener(btnSendListener);
        
    }
    
    private OnClickListener btnSendListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	new AsyncTask<Integer, Integer, Boolean>()
	        {
	
	            @Override
	            protected void onPreExecute()
	            {
	                /*
	                 * This is executed on UI thread before doInBackground(). It is
	                 * the perfect place to show the progress dialog.
	                 */
	                progressDialog = ProgressDialog.show(PostComment.this, "", "Loading...", false, true);
	            }
	
	            @Override
	            protected Boolean doInBackground(Integer... params)
	            {
	                if (params == null)
	                {
	                    return false;
	                }
	                try
	                {
	                    /*
	                     * This is run on a background thread, so we can sleep here
	                     * or do whatever we want without blocking UI thread. A more
	                     * advanced use would download chunks of fixed size and call
	                     * publishProgress();
	                     */ 
		            	  
	                    //Thread.sleep(params[0]);
	                    // HERE I'VE PUT ALL THE FUNCTIONS THAT WORK FOR ME
		          	   	  
	                	// Create a new HttpClient and Post Header
	                    httpclient = new DefaultHttpClient();
	                    httppost = new HttpPost("http://hslu.ath.cx/boli/SetComment.php");
	                    
	                    String creator = txtCreator.getText().toString();
	                    String meldung = txtBody.getText().toString();
	                    
	                    //creator = creator.replaceAll("[_[^\\w\\däüöÄÜÖ\\+\\- ]]", "");
	                    //meldung = meldung.replaceAll("[_[^\\w\\däüöÄÜÖ\\+\\- ]]", "");
	                    
	                    if (creator.equals("") && meldung.equals(""))
	                    {
	        		       	  // Create new Dialog
	        		       	  final Dialog dialog = new Dialog(PostComment.this);
	        		       	  dialog.setTitle("Fehlerhafte Eingabe");
	        		       	  TextView txtDescription = new TextView(PostComment.this);
	        		       	  txtDescription.setPadding(10, 0, 0, 10);
	        		       	  		       	  
	        		       	  txtDescription.setText("Bitte geben Sie ihren Namen sowie eine Meldung ein");
	        		       	  dialog.setContentView(txtDescription);
	        		             
	        		          dialog.setCanceledOnTouchOutside(true);
	        		          dialog.show();
	        		          
	                    }
	                    else
	                    {
	        	            try 
	        	            {
	        	                // Add your data
	        	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	        	                nameValuePairs.add(new BasicNameValuePair("newsID", mnewsID));
	        	                nameValuePairs.add(new BasicNameValuePair("creator", creator));
	        	                nameValuePairs.add(new BasicNameValuePair("body", meldung));
	        	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        	

	        	                // Execute HTTP Post Request
	        		    		    	try 
	        		    		    	{
	        				                HttpResponse response = httpclient.execute(httppost);
	        		    		    	}
	        		    		    	catch (ClientProtocolException e) 
	        		    		    	{
	        		    		    		return false;
	        		    		    	}
	        		    		    	catch (IOException e) 
	        		    		    	{
	        								// TODO Auto-generated catch block
	        								e.printStackTrace();
	        							}   
	        	            } 
	        	            catch (Exception e) 
	        	            {

	        	            } 
	                    }  
	                }
	                catch (Exception e)
	                {
	                    Log.e("tag", e.getMessage());
	                    /*
	                     * The task failed
	                     */
	                    return false;
	                }
	
	                /*
	                 * The task succeeded
	                 */
	                return true;
	            }
	
	            @Override
	            protected void onPostExecute(Boolean result)
	            {
	            	
	                progressDialog.dismiss();
	                
	                /*
	                 * Update here your view objects with content from download. It
	                 * is save to dismiss dialogs, update views, etc., since we are
	                 * working on UI thread.
	                 */
	                if (result)
	                {
	                    //b.setMessage("Download succeeded");
				       	  // Create new Dialog
				       	  final Dialog dialog = new Dialog(PostComment.this);
				       	  dialog.setTitle("Info");
				       	  TextView txtDescription = new TextView(PostComment.this);
				       	  txtDescription.setPadding(10, 0, 0, 10);
				       	  		       	  
				       	  txtDescription.setText("Nachricht erfolgreich an Server geschickt");
				       	  dialog.setContentView(txtDescription);
				             
				          dialog.setCanceledOnTouchOutside(true);
				          dialog.show();
				          
					      PostComment.this.finish();
	                }
	                else
	                {
		                AlertDialog.Builder b = new AlertDialog.Builder(PostComment.this);
		                b.setTitle(android.R.string.dialog_alert_title);
	                    b.setMessage("Beim upload des Kommentars ist ein Fehler aufgetreten");
		                b.setPositiveButton(getString(android.R.string.ok),
		                        new DialogInterface.OnClickListener()
		                        {
		
		                            @Override
		                            public void onClick(DialogInterface dlg, int arg1)
		                            {
		                                dlg.dismiss();
		                            }
		                        });
		                b.create().show();
	                }
	            }
	        }.execute(30000);
        }
    };
    
    private OnClickListener btnHomeListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(PostComment.this,VBCMaltersFanAppActivity.class);
        	startActivity(lNextIntent);
        }
    };
}
