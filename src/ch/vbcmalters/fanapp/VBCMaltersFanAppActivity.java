package ch.vbcmalters.fanapp;

//import ch.hslu.stefan.bachmann.R;
import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class VBCMaltersFanAppActivity extends Activity {
	
	String m_Authtoken;
	public static final String PREFS_NAME = "VBCMaltersFanAppPreferences";
	private ProgressDialog progressDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try
        {
	        init();
	        
			
	    	Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
	    	
	    	registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
	
	    	registrationIntent.putExtra("sender", "bachmann.stefan.ma@gmail.com");
	
	    	startService(registrationIntent);
        }
        catch(Exception ex)
        {
        	
        }

        new AsyncTask<Integer, Integer, Boolean>()
        {

            @Override
            protected void onPreExecute()
            {
                /*
                 * This is executed on UI thread before doInBackground(). It is
                 * the perfect place to show the progress dialog.
                 */
                progressDialog = ProgressDialog.show(VBCMaltersFanAppActivity.this, "", "Loading...", false, true);
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
                	
                	
                	//Authorisierungstoken holen
                	//String auth_token = AuthenticationUtil.getToken("bachmann.stefan.ma@gmail.com", "stefan1989");
                	//m_Authtoken = auth_token;
                	
                	//Server mit dem Token Updaten
                	//DAs mach ich manuell 
        	    	
        	    	AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        	    	Account[] list = manager.getAccounts();

        	    	
        	    	CheckPermission permissionChecker = new CheckPermission();
        	    	
    	    		boolean isAdmin = false;
    	    		boolean isAuthor = false;
    	    		
        	    	if (list != null)
        	    	{
        	    		for (int i = 0; i < list.length; i++)
        	    		{
        	    			if (list[i].type.equals("com.google"))
        	    			{
        	    				isAdmin = permissionChecker.CheckAdminPermission(list[i].name);
                	    		isAuthor = permissionChecker.CheckAuthorPermission(list[i].name);
                	    		
                	    		break;
        	    			}
        	    		}
        	    	}
        	    	
        	    	setPermissions(isAdmin, isAuthor);
                }
                catch (Exception e)
                {
                    //Log.e("tag", e.getMessage());
                    /*
                     * The task failed
                     */
                    setPermissions(false, false);
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
                }
                else
                {
	                AlertDialog.Builder b = new AlertDialog.Builder(VBCMaltersFanAppActivity.this);
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
        }.execute(15000);  
    }
    
    
    
    private void setPermissions(boolean isAdmin, boolean isAuthor)
    {
    	
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        
        editor.putBoolean("IsAdmin", isAdmin);
        editor.putBoolean("IsAuthor", isAuthor);
        
        // Commit the edits!
        editor.commit();
    }
    
    
    private void init()
    {
    	// Capture our button from layout
    	
        Button btnNews = (Button)findViewById(R.id.btnNews);
        Button btnRanglisten = (Button)findViewById(R.id.btnRanglisten);
        Button btnSpielplan = (Button)findViewById(R.id.btnSpielplan);
        Button btnFotos = (Button)findViewById(R.id.btnFotos);
        
        btnNews.setText("News");
        btnNews.setOnClickListener(btnNewsListener);
        
        
        btnRanglisten.setText("Ranglisten");
        btnRanglisten.setOnClickListener(btnRanglistenListener);
        
        btnSpielplan.setText("Spielpläne");
        btnSpielplan.setOnClickListener(btnSpielplanListener);
        
        
        btnFotos.setText("Fotos");
        btnFotos.setOnClickListener(btnFotosListener);

        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
        Button actionbar_AddNews = (Button)findViewById(R.id.actionbarFunk1);
        Button actionbar_Reload = (Button)findViewById(R.id.actionbarFunk2);
        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
        actionbar_AddNews.setBackgroundResource(android.R.drawable.ic_input_add);
        actionbar_Reload.setBackgroundResource(R.drawable.ic_refresh);
        actionbar_title.setText("VBC Malters Fan App ");
        
        actionbar_AddNews.setVisibility(View.GONE);
        actionbar_Reload.setVisibility(View.GONE);
        actionbar_home.setVisibility(View.GONE);
        
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	getMenuInflater().inflate(R.menu.main_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.menu_mPreferences:
        	Intent lNextIntent = new Intent(VBCMaltersFanAppActivity.this,SettingsActivity.class);
        	startActivity(lNextIntent);
        	
    		return true;

		default:
			return super.onOptionsItemSelected(item);
    	}
    }
    
    
    private OnClickListener btnNewsListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	new Thread(new Runnable() {
    		    public void run() {
    	        	Intent lNextIntent = new Intent(VBCMaltersFanAppActivity.this,News.class);
    	        	startActivity(lNextIntent);
    	        	
    		    }
    		  }).start();
        }
    };
    private OnClickListener btnRanglistenListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(VBCMaltersFanAppActivity.this,Results_Teams.class);
        	startActivity(lNextIntent);
        }
    };
    private OnClickListener btnSpielplanListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(VBCMaltersFanAppActivity.this,PlayingSchedule_Teams.class);
        	startActivity(lNextIntent);        	
        }
    };
    private OnClickListener btnFotosListener = new OnClickListener() {
        public void onClick(View v) {
	
        	Intent lNextIntent = new Intent(VBCMaltersFanAppActivity.this,ShowImages.class);
        	startActivity(lNextIntent);         
        }
    };
}