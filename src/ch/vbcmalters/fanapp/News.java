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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



public class News extends Activity
{
	private static final String PREFS_NAME = "VBCMaltersFanAppPreferences";
	
	// ArrayList which contains our HashMap's with different objects
	private ArrayList<HashMap<String, Object>> myList;
	
	XMLParser parser;
    String xml;
    ProgressDialog progressDialog;
    ListView _mainListView;
    
	
	// All static variables
	private final String URL = "http://volley.pulse-guild.ath.cx/GetNews.php";
	  // XML node keys
	private final String KEY_ITEM = "entry"; // parent node
	
	SharedPreferences settings;
    boolean m_isAdmin;
    boolean m_isAuthor;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
       

        init();
        getNews();	  
        
    }
    
    private void init()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        m_isAdmin = settings.getBoolean("IsAdmin", false);
        m_isAuthor = settings.getBoolean("IsAuthor", false);
        
        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
        Button actionbar_AddNews = (Button)findViewById(R.id.actionbarFunk1);
        Button actionbar_Reload = (Button)findViewById(R.id.actionbarFunk2);
        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
        actionbar_AddNews.setBackgroundResource(android.R.drawable.ic_input_add);
        actionbar_Reload.setBackgroundResource(R.drawable.ic_refresh);
        actionbar_title.setText("News");
        
        actionbar_AddNews.setOnClickListener(btnAddNewsListener);
        actionbar_Reload.setOnClickListener(btnReloadListener);
        actionbar_home.setOnClickListener(btnHomeListener);
        
    	_mainListView = (ListView) findViewById(R.id.news_listview);
    	
        if (m_isAdmin || m_isAuthor)
        {
        	actionbar_AddNews.setVisibility(View.VISIBLE);
        }
        else
        {
        	actionbar_AddNews.setVisibility(View.GONE);
        }   
    }
    
    private void getNews()
    {
    	try
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
	                progressDialog = ProgressDialog.show(News.this, "", "Loading...", false, true);
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
	        	        xml = parser.getXmlFromUrl(URL); // getting XML  
	                }
	                catch (Exception e)
	                {
	                    //Log.e("tag", e.getMessage());
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
	                if (result && xml != null)
	                {
	                    //b.setMessage("Download succeeded");
	        	        Document doc = parser.getDomElement(xml); // getting DOM element
	       	         
	        	        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
	        	        
	        	        myList = new ArrayList<HashMap<String, Object>>();
	        	        
	        		   	// View
	        		   	// Adapter
	        	        SimpleAdapter simpleAdapter = new SimpleAdapter(News.this , myList, R.layout.newsrow,
	        		               new String[] {"title", "date", "body"}, 
	        		               new int[] {R.id.txt_title, R.id.txt_date, R.id.txt_body});
	        	        
	        	        _mainListView.setAdapter(simpleAdapter);
	        		       
	        		       
	        	 	       // Listener
	        	        _mainListView.setOnItemClickListener(new OnItemClickListener() 
	        	 	       {
	        	 	         public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        		         {
	        			       	  // Get the HashMap of the clicked item
	        			       	  HashMap<String, Object> user = myList.get(position);
	        			       	  
	        			       	  // Get Attribute name of the HashMap
	        			       	  Element e = (Element)user.get("object");
	        			       	  
	        		         	Intent lNextIntent = new Intent(News.this,Comments.class);
	        		         	lNextIntent.putExtra("newsID", parser.getValue(e, "NewsID"));
	        		         	lNextIntent.putExtra("title", parser.getValue(e, "NewsTitle"));
		        	        	String date = parser.getValue(e, "NewsDate");
		        	        	String formatedDate = date.substring(0, date.length() - 7);
	        		         	
	        		         	lNextIntent.putExtra("date", formatedDate);
	        		         	lNextIntent.putExtra("body", parser.getValue(e, "NewsBody"));
	        		        	startActivity(lNextIntent);
	        		         }
	        		       });
	        	 	       
	        	 	       
	        	            if (m_isAdmin || m_isAuthor)
	        	            {
	        	            	_mainListView.setOnCreateContextMenuListener(newsListViewContextMenuListener);
	        	            }
	        	 	       
	        		       	       
	        	
	        	        // looping through all item nodes <item>
	        	        for (int i = 0; i < nl.getLength(); i++) 
	        	        {
	        	        	Element e = (Element) nl.item(i);
	        	                   	
	        	        	HashMap<String, Object> map1 = new HashMap<String, Object>();
	        	        	map1.put("newsID", parser.getValue(e, "NewsID"));
	        	        	map1.put("title", parser.getValue(e, "NewsTitle"));
	        	        	
	        	        	String date = parser.getValue(e, "NewsDate");
	        	        	String formatedDate = date.substring(0, date.length() - 7);
	        	        	
	        	        	
	        	        	map1.put("date", formatedDate);
	        	        	map1.put("body", parser.getValue(e, "NewsBody"));
	        	        	map1.put("object", e);
	        	        	
	        	        	myList.add(map1);
	        	
	        	        }
	                }
	                else
	                {
		                AlertDialog.Builder b = new AlertDialog.Builder(News.this);
		                b.setTitle(android.R.string.dialog_alert_title);
	                    b.setMessage("Es konnte keine Verbindung hergestellt werden");
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
    	catch(Exception ex)
    	{
    		
    		String msg = ex.getMessage();
    	}
    }
    
    private OnCreateContextMenuListener newsListViewContextMenuListener = new OnCreateContextMenuListener() {
		

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) 
			{
				  if (v.getId()==R.id.news_listview) {
					    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
					    
					    HashMap<String, Object> user = myList.get(info.position);
					    
					    menu.setHeaderTitle("Bearbeiten");
					    String[] menuItems = {"Löschen", "Bearbeiten" }; 
					    for (int i = 0; i<menuItems.length; i++) 
					    {
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
		      final HttpPost httppost = new HttpPost("http://volley.pulse-guild.ath.cx/DeleteNews.php");
	       	  
		      
		      // Get Attribute name of the HashMap
	       	  Element e = (Element)user.get("object");
	       	  
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("newsID",  parser.getValue(e, "NewsID").toString()));
                
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
		  else if (menuItemIndex == 1)
		  {
			
			  HashMap<String, Object> user = myList.get(info.position);
			         	  
		      // Get Attribute name of the HashMap
	       	  Element e = (Element)user.get("object");
	       	  
	       	  Intent lNextIntent = new Intent(News.this,UpdateNews.class);
	       	  lNextIntent.putExtra("newsID", parser.getValue(e, "NewsID"));
	       	  lNextIntent.putExtra("title", parser.getValue(e, "NewsTitle"));
	       	  lNextIntent.putExtra("body", parser.getValue(e, "NewsBody"));
	       	  startActivity(lNextIntent);
		  }
		  
		  return true;
    }
    
    private OnClickListener btnAddNewsListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(News.this,PostNews.class);
        	startActivity(lNextIntent);
        }
    };
    
    private OnClickListener btnReloadListener = new OnClickListener() {
        public void onClick(View v) {
        	xml = null;
        	getNews();
        }
    };
    
    private OnClickListener btnHomeListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(News.this,VBCMaltersFanAppActivity.class);
        	startActivity(lNextIntent);
        }
    };

}
