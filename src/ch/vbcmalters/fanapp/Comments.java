package ch.vbcmalters.fanapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Comments extends Activity{

	// ArrayList which contains our HashMap's with different objects
	private ArrayList<HashMap<String, Object>> myList;
	
	// All static variables
	private final String URL = "http://hslu.ath.cx/boli/GetComments.php?commentID=";
	  // XML node keys
	private final String KEY_ITEM = "entry"; // parent node
	
	private static final String PREFS_NAME = "VBCMaltersFanAppPreferences";
	
	SharedPreferences settings;
    boolean m_isAdmin;
    boolean m_isAuthor;
	
	XMLParser parser;
	String xml;
	
	ListView _mainListView;
	TextView txtTitle;
	TextView txtBody;
	TextView txtDate;
	String referenceID;
	int commentID;
	ProgressDialog progressDialog;


	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.comments);
	    	
	    init();
	    
	    commentID = 0;
        Bundle extras = getIntent().getExtras();
        if (extras == null) 
        {
        		return;
        }
        else
        {
	        // Get data via the key
	        referenceID = extras.getString("newsID");
	        String title = extras.getString("title");
	        String date = extras.getString("date");
	        String body = extras.getString("body");
	        
	        if (referenceID != null)
	        {
	        	commentID = Integer.parseInt(referenceID);;
	        }
	        if (title != null) 
	        {
	        	txtTitle.setText(title);
	        }
	        if (date != null)
	        {
	        	txtDate.setText(date);
	        }
	        if (body != null)
	        {
	        	txtBody.setText(body);
	        }
	        
	        
	        getComment(commentID);
        }
	}
	
	private void init()
	{
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        m_isAdmin = settings.getBoolean("IsAdmin", false);
        m_isAuthor = settings.getBoolean("IsAuthor", false);
		
		txtBody = (TextView)findViewById(R.id.txt_body);
		txtTitle = (TextView)findViewById(R.id.txt_title);
		txtDate = (TextView)findViewById(R.id.txt_date);
		_mainListView = (ListView) findViewById(R.id.comments_listview);
			
        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
        Button actionbar_AddComment = (Button)findViewById(R.id.actionbarFunk1);
        Button actionbar_Reload = (Button)findViewById(R.id.actionbarFunk2);
        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
        actionbar_AddComment.setBackgroundResource(android.R.drawable.ic_input_add);
        actionbar_Reload.setBackgroundResource(R.drawable.ic_refresh);
        actionbar_title.setText("Kommentare");
        
        actionbar_AddComment.setOnClickListener(btnAddCommentListener);
        actionbar_Reload.setOnClickListener(btnReloadListener);
        actionbar_home.setOnClickListener(btnHomeListener);
    	
    	
	}
	
	private void getComment(int commentID)
	{
		final int tmpCommentID = commentID;
		
		new AsyncTask<Integer, Integer, Boolean>()
        {

            @Override
            protected void onPreExecute()
            {
                /*
                 * This is executed on UI thread before doInBackground(). It is
                 * the perfect place to show the progress dialog.
                 */
                progressDialog = ProgressDialog.show(Comments.this, "", "Loading...", false, true);
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

               		parser = new XMLParser();
               		
                	//String xml = parser.getXmlFromUrl(URL + Integer.toString(commentID)); // getting XML
                	
   
               		xml = parser.getXmlFromUrl(URL + Integer.toString(tmpCommentID)); // getting XML

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
                	Document doc = parser.getDomElement(xml); // getting DOM element
                    
                    NodeList nl = doc.getElementsByTagName(KEY_ITEM);
                    
                    myList = new ArrayList<HashMap<String, Object>>();
                    
            	   	// View
            	      
            	       
            	   	// Adapter
            	       SimpleAdapter simpleAdapter = new SimpleAdapter(Comments.this, myList, R.layout.commentsrow,
            	               new String[] {"creator", "date", "body"}, 
            	               new int[] {R.id.txt_creator, R.id.txt_date, R.id.txt_body});
            	       _mainListView.setAdapter(simpleAdapter);
            	       
            	       
            	    if (nl.getLength() > 0)
            	    {
        	            // looping through all item nodes <item>
        	            for (int i = 0; i < nl.getLength(); i++) 
        	            {
        	            	Element e = (Element) nl.item(i);
        	                
        	            	HashMap<String, Object> map1 = new HashMap<String, Object>();
        	            	map1.put("creator", parser.getValue(e, "CommentCreator"));
	        	        	String date = parser.getValue(e, "CommentDate");
	        	        	String formatedDate = date.substring(0, date.length() - 7);
        	            	
        	            	map1.put("date", formatedDate);
        	            	map1.put("body", parser.getValue(e, "CommentBody"));
        	            	map1.put("object", e);
        	            	
        	            	myList.add(map1);
        	
        	            }
        	            if (m_isAdmin || m_isAuthor)
        	            {
        	            	_mainListView.setOnCreateContextMenuListener(commentsListViewContextMenuListener);
        	            }
            	    }
            	    else
            	    {
        	            	HashMap<String, Object> map1 = new HashMap<String, Object>();
        	            	map1.put("creator", "");
        	            	map1.put("date", "");
        	            	map1.put("body", "Keine Kommentare zu dieser News");
        	            	
        	            	myList.add(map1);	
            	    } 
                }
                else
                {
	                AlertDialog.Builder b = new AlertDialog.Builder(Comments.this);
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
	
    private OnClickListener btnAddCommentListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(Comments.this,PostComment.class);
        	lNextIntent.putExtra("newsID", referenceID);
        	startActivity(lNextIntent);
        }
    };
    
    private OnCreateContextMenuListener commentsListViewContextMenuListener = new OnCreateContextMenuListener() {
		

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) 
		{
			  if (v.getId()==R.id.comments_listview) {
				    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
				    
				    HashMap<String, Object> user = myList.get(info.position);
				    
				    menu.setHeaderTitle("Bearbeiten");
				    String[] menuItems = {"Löschen"}; 
				    for (int i = 0; i<menuItems.length; i++) {
				      menu.add(Menu.NONE, i, i, menuItems[i]);
				    }
				    
			  }				  

		}
    };

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
	   AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		  int menuItemIndex = item.getItemId();
		  
		  if (menuItemIndex == 0)
		  {
			  HashMap<String, Object> user = myList.get(info.position);
			  
		      // Create a new HttpClient and Post Header
		      final HttpClient httpclient = new DefaultHttpClient();
		      final HttpPost httppost = new HttpPost("http://hslu.ath.cx/boli/DeleteComment.php");
	       	  
		      
		      // Get Attribute name of the HashMap
	       	  Element e = (Element)user.get("object");
	       	  
	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("commentID",  parser.getValue(e, "CommentID").toString()));
	            
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
		  
		  return true;
	}
	
    private OnClickListener btnReloadListener = new OnClickListener() {
        public void onClick(View v) {
        	xml = null;
        	getComment(commentID);
        }
    };
    
    private OnClickListener btnHomeListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(Comments.this,VBCMaltersFanAppActivity.class);
        	startActivity(lNextIntent);
        }
    };

}
