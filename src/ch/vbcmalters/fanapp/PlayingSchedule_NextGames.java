package ch.vbcmalters.fanapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PlayingSchedule_NextGames extends Activity {

	// ArrayList which contains our HashMap's with different objects
		private ArrayList<HashMap<String, Object>> myList;
		private ProgressDialog progressDialog;
		private SoapObject mySoapObject;
		private ListView _mainListView;
		
		private Club club;

		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.playingschedule);
	     
	        club = new Club();
	        
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
	        actionbar_title.setText("Die nächsten Spiele");
	        
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
	                progressDialog = ProgressDialog.show(PlayingSchedule_NextGames.this, "", "Loading...", false, true);
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
	    		       String clubID = Integer.toString(club.getClubID());
	    		        
	    		       mySoapObject = myConnection.getSOAPConnection("http://myvolley.swissvolley.ch/getGamesByClub", "getGamesByClub", "ID_club", clubID );  
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
				       SimpleAdapter simpleAdapter = new SimpleAdapter(PlayingSchedule_NextGames.this, myList, R.layout.playingschedulerow,
				               new String[] {"date", "time", "teamHome", "teamAway" }, 
				               new int[] {R.id.txt_date, R.id.txt_time, R.id.txt_teamHome, R.id.txt_teamAway});
				       
				       _mainListView.setAdapter(simpleAdapter);
			       
			       }
	               else
	               {
		                AlertDialog.Builder b = new AlertDialog.Builder(PlayingSchedule_NextGames.this);
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
			    	int gameCounter = 0;
			    	while(i < table.getPropertyCount())
			    	{
			    		SoapObject entry = (SoapObject)table.getProperty(i);
			    		
			    		
			    		//Only Show the next 20 Games...
			    		//So we have to format the dates and display only the games we want
			    		
				  		String dateString = (String)entry.getProperty("PlayDate");
						String formatedDate = dateString.substring(0, 10); 
						  
						//Date now = new Date();
						
						
						Date now = new Date();  
						Calendar cal = Calendar.getInstance();  
						cal.setTime(now);  
						cal.add(Calendar.DAY_OF_YEAR, -1); // <--  
						Date yesterday = cal.getTime(); 
						  
					    
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date convertedDate = dateFormat.parse(formatedDate);
			    		
						
						if (yesterday.before(convertedDate))
						{
				        	HashMap<String, Object> map1 = new HashMap<String, Object>();
				        	map1.put("date", entry.getProperty("PlayDate"));
				        	//map1.put("time", entry.getProperty("Caption"));
				        	
				        	
				        	String teamHomeString = entry.getProperty("TeamHomeID").toString();
				        	int homeTeamID = Integer.parseInt(teamHomeString);
				        	
				        	String teamAwayString = entry.getProperty("TeamAwayID").toString();
				        	int teamAwayID = Integer.parseInt(teamAwayString); 
				        	
				        	Team homeTeam = TeamFactory.getTeamByTeamID(homeTeamID);
				        	Team awayTeam = TeamFactory.getTeamByTeamID(teamAwayID);
				        	
				        	String homeTeamCaption = entry.getProperty("TeamHomeCaption").toString();
				        	String awayTeamCaption = entry.getProperty("TeamAwayCaption").toString();		
				        		
				        	if (homeTeam.get_Team_ID() != 0)
				        	{
				        		homeTeamCaption = homeTeam.get_Name();
				        	}
				        	if (awayTeam.get_Team_ID() != 0)
				        	{
				        		awayTeamCaption = awayTeam.get_Name();
				        	}
				        	
				        	map1.put("teamHome", homeTeamCaption);
				        	map1.put("teamAway", awayTeamCaption);
				        	map1.put("object", entry);
				        	
				        	myList.add(map1);
				        	
							gameCounter++;
						}
						
						if (gameCounter >= 20)
						{
							//leave the for loop to optimize performance
							break;
						}
						
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
	        	Intent lNextIntent = new Intent(PlayingSchedule_NextGames.this,VBCMaltersFanAppActivity.class);
	        	startActivity(lNextIntent);
	        }
	    };
}