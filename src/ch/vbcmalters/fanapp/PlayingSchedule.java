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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlayingSchedule extends Activity {

	// ArrayList which contains our HashMap's with different objects
		private ArrayList<HashMap<String, Object>> myList;
		private ProgressDialog progressDialog;
		private SoapObject mySoapObject;
		private ListView _mainListView;
		
		
		private Team team;

		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.playingschedule);
	     
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
	    	_mainListView = (ListView) findViewById(R.id.result_listview);
	    	
	        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
	        Button actionbar_AddNews = (Button)findViewById(R.id.actionbarFunk1);
	        Button actionbar_Reload = (Button)findViewById(R.id.actionbarFunk2);
	        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
	        actionbar_AddNews.setBackgroundResource(android.R.drawable.ic_input_add);
	        actionbar_Reload.setBackgroundResource(R.drawable.ic_refresh);
	        actionbar_title.setText("Spielplan " + team.get_Name());
	        
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
	                progressDialog = ProgressDialog.show(PlayingSchedule.this, "", "Loading...", false, true);
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
	    		       String teamID = Integer.toString(team.get_Team_ID());
	    		        
	    		       mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesTeam", "getGamesTeam", "team_ID", teamID );  
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
				       PlayingScheduleAdapter playingScheduleAdapter = new PlayingScheduleAdapter(PlayingSchedule.this, myList, R.layout.playingschedulerow,
				               new String[] {"date", "time", "teamHome", "teamAway" }, 
				               new int[] {R.id.txt_date, R.id.txt_time, R.id.txt_teamHome, R.id.txt_teamAway});
				       
				       _mainListView.setAdapter(playingScheduleAdapter);
				       
				       
				       
				       // Listener
				       _mainListView.setOnItemClickListener(new OnItemClickListener() {
				         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				       	  // Get the HashMap of the clicked item
				       	  HashMap<String, Object> user = myList.get(position);
				       	  
				       	  // Get Attribute name of the HashMap
				       	  SoapObject entry = (SoapObject)user.get("object");
				       	  
				       	  String teamHome = entry.getProperty("TeamHomeCaption").toString();
				       	  String teamAway = entry.getProperty("TeamAwayCaption").toString();
				       	  String hall = entry.getProperty("HallCaption").toString();
				       	  
				       	  
				       	  int[] setPointsHome = new int[5];
				       	  int[] setPointsAway = new int[5];
				       	  
				       	  setPointsHome[0] = ((Integer)entry.getProperty("Set1PointsHome")).intValue();
				       	  setPointsAway[0] = ((Integer)entry.getProperty("Set1PointsAway")).intValue();
				       	  setPointsHome[1] = ((Integer)entry.getProperty("Set2PointsHome")).intValue();
				       	  setPointsAway[1] = ((Integer)entry.getProperty("Set2PointsAway")).intValue();
				       	  setPointsHome[2] = ((Integer)entry.getProperty("Set3PointsHome")).intValue();
				       	  setPointsAway[2] = ((Integer)entry.getProperty("Set3PointsAway")).intValue();
				       	  setPointsHome[3] = ((Integer)entry.getProperty("Set4PointsHome")).intValue();
				       	  setPointsAway[3] = ((Integer)entry.getProperty("Set4PointsAway")).intValue();
				       	  setPointsHome[4] = ((Integer)entry.getProperty("Set5PointsHome")).intValue();
				       	  setPointsAway[4] = ((Integer)entry.getProperty("Set5PointsAway")).intValue();
				       	  
				       	  
				       	  String toDisplay = "";
				       	  int i = 1;
				       		
				       	  // Create new Dialog
				       	  final Dialog dialog = new Dialog(PlayingSchedule.this);
				       	  dialog.setTitle(teamHome + "  :  " + teamAway);
				       	  TextView txtDescription = new TextView(PlayingSchedule.this);
				       	  txtDescription.setPadding(10, 0, 0, 10);
				       	  
				       	  for (i = 0; i < 5; i++)
				       	  {
				       		  if (setPointsHome[i] != -1)
				       		  {
				       			  toDisplay += Integer.toString(setPointsHome[i]) + " : " + Integer.toString(setPointsAway[i]) + "   ";
				       		  }
				       	  }
				       	  
				       	  toDisplay += "\n\nHalle: " + hall;
				       	  
				       	  txtDescription.setText(toDisplay);
				       	  dialog.setContentView(txtDescription);
				             
				             dialog.setCanceledOnTouchOutside(true);
				             dialog.show();
				         }
				         
		    
				       });  
			       
			       }
	               else
	               {
		                AlertDialog.Builder b = new AlertDialog.Builder(PlayingSchedule.this);
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
			        	map1.put("date", entry.getProperty("PlayDate"));
			        	//map1.put("time", entry.getProperty("Caption"));
			        	map1.put("teamHome", entry.getProperty("TeamHomeCaption"));
			        	map1.put("teamAway", entry.getProperty("TeamAwayCaption"));
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
	        	Intent lNextIntent = new Intent(PlayingSchedule.this,VBCMaltersFanAppActivity.class);
	        	startActivity(lNextIntent);
	        }
	    };
}
