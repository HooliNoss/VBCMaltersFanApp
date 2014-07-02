package ch.vbcmalters.fanapp;


import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Results extends Activity {
	
	// ArrayList which contains our HashMap's with different objects
	private ArrayList<HashMap<String, Object>> myList;
	private ProgressDialog progressDialog;
	SoapObject mySoapObject;
	ListView _mainListView;
	
	private Team team;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        		return;
        		}
        // Get data via the key
        String sender = extras.getString("sender");
        if (sender != null) 
        {
        	team = TeamFactory.getTeamByName(sender);
        }
        
    	Init();
    	handleSoapConnection();

    }
    
    private void Init()
    {
 	   	// View
	       _mainListView = (ListView) findViewById(R.id.result_listview);
	       
	        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
	        Button actionbar_AddNews = (Button)findViewById(R.id.actionbarFunk1);
	        Button actionbar_Reload = (Button)findViewById(R.id.actionbarFunk2);
	        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
	        actionbar_AddNews.setBackgroundResource(android.R.drawable.ic_input_add);
	        actionbar_Reload.setBackgroundResource(R.drawable.ic_refresh);
	        actionbar_title.setText("Rangliste " + team.get_Name());
	        
	        actionbar_AddNews.setVisibility(View.GONE);
	        actionbar_Reload.setVisibility(View.GONE);
	        actionbar_home.setOnClickListener(btnHomeListener);
    }
    
    private void handleSoapConnection()
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
                progressDialog = ProgressDialog.show(Results.this, "", "Loading...", false, true);
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
	          	   	  
                	SOAPConnection myConnection = new SOAPConnection();
                    //String teamID = Integer.toString(team.get_Team_ID());
                    String groupID = Integer.toString(team.get_Group_ID());
                     
                    mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getTable", "getTable", "group_ID", groupID );  
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
                
                
                if (mySoapObject != null && result == true)
                {
             	    	   
         	           // Fill data
             	   addDataToList(mySoapObject);

         	       
         	   	// Adapter
         	       SimpleAdapter aa = new SimpleAdapter(Results.this, myList, R.layout.resultsrow,
         	               new String[] {"rank", "team", "numberOfGames", "points" }, 
         	               new int[] {R.id.txt_rank, R.id.txt_team, R.id.txt_numberOfGames, R.id.txt_points});
         	      _mainListView.setAdapter(aa);
         	       
         	       
         	       // Listener
         	     _mainListView.setOnItemClickListener(new OnItemClickListener() {
         	         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         	       	  // Get the HashMap of the clicked item
         	       	  HashMap<String, Object> user = myList.get(position);
         	       	  
         	       	  // Get Attribute name of the HashMap
         	       	  

         	       	  
         	       	  SoapObject entry = (SoapObject)user.get("object");
         	       	  
         	       	  String teamName = (String)entry.getProperty("Caption");
         	       	  String setsWon = entry.getProperty("SetsWon").toString();
         	       	  String setsLost = entry.getProperty("SetsLost").toString();
         	       	  String setsQuotient = entry.getProperty("SetQuotient").toString();
         	       	  String ballsWon = entry.getProperty("BallsWon").toString();
         	       	  String ballsLost = entry.getProperty("BallsLost").toString();
         	       	  String ballsQuotient = entry.getProperty("BallsQuotient").toString();
         	       		
         	       		
         	       	  // Create new Dialog
         	       	  final Dialog dialog = new Dialog(Results.this);
         	       	  dialog.setTitle("Detail of " + teamName);
         	       	  TextView txtDescription = new TextView(Results.this);
         	       	  txtDescription.setPadding(10, 0, 0, 10);
         	       	  txtDescription.setText("Sätze:   " + setsWon + " : " + setsLost + " \r\n" +
         	       			  				 "Satzquotient:     " + setsQuotient +  "\r\n" + 
         	       			  				 "Punkte:   " + ballsWon + " : " + ballsLost  + " \r\n" +
         	       			  				 "Ballquotient:     " + ballsQuotient
         	       			  
         	       			  );
         	       	  dialog.setContentView(txtDescription);
         	             
         	             dialog.setCanceledOnTouchOutside(true);
         	             dialog.show();
         	         }
         	       
         	         
         	       }); 
                }
                else
                {
	                AlertDialog.Builder b = new AlertDialog.Builder(Results.this);
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
    
    
    /**
     * This method is used, to fill data into our List
     */
    private void addDataToList(SoapObject data){
    	
    	//txtTeamName.setText(((SoapObject)mySoapObject.getProperty(0)).getPropertyAsString("Caption"));
    	myList = new ArrayList<HashMap<String, Object>>();
    	
    	try
    	{
	    	if ((SoapObject)data.getProperty(0) != null)
	    	{    		
	    		SoapObject table = (SoapObject)data.getProperty(0);
		    	int i = 0;
		    	while(i < table.getPropertyCount())
		    	{
		    		SoapObject entry = (SoapObject)table.getProperty(i);
		    		
		        	HashMap<String, Object> map1 = new HashMap<String, Object>();
		        	map1.put("rank", entry.getProperty("Rank"));
		        	map1.put("team", entry.getProperty("Caption"));
		        	map1.put("numberOfGames", entry.getProperty("NumberOfGames"));
		        	map1.put("points", entry.getProperty("Points"));
		        	map1.put("object", entry);
		        	
		        	myList.add(map1);
		    		
		    		i++;
		    	}
	    	}
    	}
    	catch(Exception ex)
    	{
    		
    		String message = ex.getMessage();
    	}
    	
    }
    
    private OnClickListener btnHomeListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(Results.this,VBCMaltersFanAppActivity.class);
        	startActivity(lNextIntent);
        }
    };
    
}
