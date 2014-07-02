package ch.vbcmalters.fanapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends Activity 
{
	public static final String PREFS_NAME = "VBCMaltersFanAppPreferences";
	
	//Settings mySettings;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        init();
    }
    
    private void init()
    {
    	//mySettings = new Settings();
    	
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();

        // Commit the edits!
        editor.commit();
    	
    	CheckBox chkboxGetNotification = (CheckBox)findViewById(R.id.chkBox_GetNotification);
    	chkboxGetNotification.setChecked(settings.getBoolean("generallNotification", true));
    	
    	chkboxGetNotification.setOnCheckedChangeListener(new OnCheckedChangeListener()
    	{
    	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    	    {
    	    	editor.putBoolean("generallNotification", isChecked);
    	    	
    	        // Commit the edits!
    	        editor.commit();

    	    }
    	});

    	
    	
    }
    
}
