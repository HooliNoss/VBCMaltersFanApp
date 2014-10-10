package ch.vbcmalters.fanapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowImages extends Activity 
{
	private int m_Offset = 0;
	private Drawable m_Drawable;
	private ImageView imgViewContent;
	private ImageView _img;
    ProgressDialog progressDialog;
	
	private static final String PREFS_NAME = "VBCMaltersFanAppPreferences";
	
	SharedPreferences settings;
    boolean m_isAdmin;
    boolean m_isAuthor;
    
    private int m_downX = 0;
    private int m_upX = 0;

    
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimage);
        
        init();
        ShowImage();
    }

    private void init()
    {
    	
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        m_isAdmin = settings.getBoolean("IsAdmin", false);
        m_isAuthor = settings.getBoolean("IsAuthor", false);
    	_img = (ImageView)findViewById(R.id.imgViewContent);
    	
    	// Capture our button from layout
    	
        imgViewContent = (ImageView)findViewById(R.id.imgViewContent);
        imgViewContent.setOnTouchListener(touchListener);
        
        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
        Button actionbar_Previous = (Button)findViewById(R.id.actionbarFunk1);
        Button actionbar_Next = (Button)findViewById(R.id.actionbarFunk2);
        Button actionbar_Upload = (Button)findViewById(R.id.actionbarFunk3);
        Button actionbar_Delete = (Button)findViewById(R.id.actionbarFunk4);
        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
        
        
        actionbar_Next.setBackgroundResource(android.R.drawable.ic_media_next);
        actionbar_Previous.setBackgroundResource(android.R.drawable.ic_media_previous);
        actionbar_Upload.setBackgroundResource(android.R.drawable.ic_input_add);
        actionbar_Delete.setBackgroundResource(android.R.drawable.ic_input_delete);
        actionbar_title.setText("Fotos");
        
        actionbar_Next.setOnClickListener(btnNextListener);
        actionbar_Previous.setOnClickListener(btnPreviousListener);
        actionbar_home.setOnClickListener(btnHomeListener);
        actionbar_Upload.setOnClickListener(btnUploadListener);
        actionbar_Delete.setOnClickListener(btnDeleteListener);
        
        
        if (m_isAdmin || m_isAuthor)
        {
        	actionbar_Upload.setVisibility(View.VISIBLE);
        	actionbar_Delete.setVisibility(View.VISIBLE);
        }
        else
        {
        	actionbar_Upload.setVisibility(View.GONE);
        	actionbar_Delete.setVisibility(View.GONE);
        }
    }
    
    private void ShowImage()
    {
	    	new AsyncTask<Integer, Integer, Boolean>()
	        {
	
	            @Override
	            protected void onPreExecute()
	            {
	                /*
	                 * This is executed on UI thread before doInBackground(). It is
	                 * the perfect place to show the progress dialog.
	                 */
	                progressDialog = ProgressDialog.show(ShowImages.this, "", "Loading...", false, true);
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
		          	   	  
		            	m_Drawable = LoadImageFromWebOperations("http://volley.pulse-guild.ath.cx/GetImage.php?imgOffset=" + m_Offset);  
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
	                	_img.setImageDrawable(m_Drawable);
	                }
	                else
	                {
		                AlertDialog.Builder b = new AlertDialog.Builder(ShowImages.this);
		                b.setTitle(android.R.string.dialog_alert_title);
	                    b.setMessage("Download failed");
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
    private Drawable LoadImageFromWebOperations(String url)
    {
 		try
 		{
 			final String tmpURL = url;
 			
 			InputStream is;

		    is = (InputStream) new URL(tmpURL).getContent();
			m_Drawable = Drawable.createFromStream(is, "src name");
			
	    	return m_Drawable;
 		}
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
 		catch (Exception e) 
 		{
 			System.out.println("Exc="+e);
 			return null;
 		}
 	}
    
    private OnClickListener btnNextListener = new OnClickListener() {
        public void onClick(View v) 
        {
        	m_Offset++;
        	ShowImage();
        }
    };
    
    private OnClickListener btnPreviousListener = new OnClickListener() {
        public void onClick(View v) 
        {
        	m_Offset--;
        	if (m_Offset < 0)
        	{
        		m_Offset = 0;
        	}
        	ShowImage();
        }
    };
    
    View.OnTouchListener touchListener = new View.OnTouchListener() 
    {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	        ImageView iv = (ImageView) v;
	
	        if (event.getAction() == MotionEvent.ACTION_DOWN) 
	        {
	        	m_downX = (int) event.getX();
	        	
	            return true;
	        } else if (event.getAction() == MotionEvent.ACTION_UP) {

	        	
                m_upX = (int) event.getX();
                
                int deltaX = m_upX - m_downX;
                
                if (deltaX > 0) 
                {
                	swipeRight();
                }
                
                if (deltaX < 0)
                {
                	swipeLeft();
                }

	        	
	            return true;
	        }
	
	        return false;
	    }
    };
    
    private void swipeLeft()
    {
    	m_Offset++;
    	ShowImage();
    }
    
    private void swipeRight()
    {
    	m_Offset--;
    	if (m_Offset < 0)
    	{
    		m_Offset = 0;
    	}
    	
    	ShowImage();    
    }
    
	
	private void deletePicture()
	{
	      // Create a new HttpClient and Post Header
	      final HttpClient httpclient = new DefaultHttpClient();
	      final HttpPost httppost = new HttpPost("http://volley.pulse-guild.ath.cx/DeleteImage.php");
     
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("imgOffset",  Integer.toString(m_Offset)));
          
          try 
          {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
              
		    	new Thread(new Runnable() 
		    	{
				    public void run() 
				    {	
				    	try 
				    	{
							HttpResponse response = httpclient.execute(httppost);
						} 
				    	catch (ClientProtocolException e) 
				    	{
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
				    	catch (IOException e) 
				    	{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				}).start();

			}
          catch (Exception ex)
          {
          	ex.printStackTrace();
          }
	}
	
    private OnClickListener btnUploadListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(ShowImages.this,UploadImage.class);
        	startActivity(lNextIntent);
        }
    };
    
    private OnClickListener btnDeleteListener = new OnClickListener() {
        public void onClick(View v) 
        {
        	
        	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ShowImages.this);
        	 myAlertDialog.setTitle("Bild löschen?");
        	 myAlertDialog.setMessage("Soll das Bild gelöscht werden?");
        	 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

        	  public void onClick(DialogInterface arg0, int arg1) 
        	  {
        		  deletePicture();
        	  }});
        	 myAlertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
        	 {
        	       
        	  public void onClick(DialogInterface arg0, int arg1) {

        	  }});
        	 myAlertDialog.show();
        }
    };
	
    private OnClickListener btnHomeListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(ShowImages.this,VBCMaltersFanAppActivity.class);
        	startActivity(lNextIntent);
        }
    };
}
