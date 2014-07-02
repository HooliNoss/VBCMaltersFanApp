package ch.vbcmalters.fanapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayingSchedule_Teams extends Activity {

	// ArrayList which contains our HashMap's with different objects
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingscheduleteams);
        
        
        init();

    }
    
    private void init()
    {
    	Button btnDamen1 = (Button)findViewById(R.id.btnDamen1);
    	btnDamen1.setText("Damen 1");
    	btnDamen1.setOnClickListener(btnListener);
    	
    	Button btnDamen2 = (Button)findViewById(R.id.btnDamen2);
    	btnDamen2.setText("Damen 2");
    	btnDamen2.setOnClickListener(btnListener);
    	
    	Button btnHerren1 = (Button)findViewById(R.id.btnHerren1);
    	btnHerren1.setText("Herren 1");
    	btnHerren1.setOnClickListener(btnListener);
    	
    	Button btnHerren2 = (Button)findViewById(R.id.btnHerren2);
    	btnHerren2.setText("Herren 2");
    	btnHerren2.setOnClickListener(btnListener);
    	
    	Button btnHerren3 = (Button)findViewById(R.id.btnHerren3);
    	btnHerren3.setText("Herren 3");
    	btnHerren3.setOnClickListener(btnListener);
    	
    	Button btnJuniorinnen1 = (Button)findViewById(R.id.btnJuniorinnen1);
    	btnJuniorinnen1.setText("Juniorinnen 1");
    	btnJuniorinnen1.setOnClickListener(btnListener);
    	
    	Button btnJuniorinnen2 = (Button)findViewById(R.id.btnJuniorinnen2);
    	btnJuniorinnen2.setText("Juniorinnen 2");
    	btnJuniorinnen2.setOnClickListener(btnListener);
    	
    	Button btnJuniorinnen3 = (Button)findViewById(R.id.btnJuniorinnen3);
    	btnJuniorinnen3.setText("Juniorinnen 3");
    	btnJuniorinnen3.setOnClickListener(btnListener);
    	
    	Button btnJunioren1 = (Button)findViewById(R.id.btnJunioren1);
    	btnJunioren1.setText("Junioren 1");
    	btnJunioren1.setOnClickListener(btnListener);
    	
    	Button btnNextGames = (Button)findViewById(R.id.btnNextGames);
    	btnNextGames.setText("Die nächsten Spiele");
    	btnNextGames.setOnClickListener(btnNextGamesListener);
    	
        Button actionbar_home = (Button)findViewById(R.id.actionbarHome);
        Button actionbar_AddNews = (Button)findViewById(R.id.actionbarFunk1);
        Button actionbar_Reload = (Button)findViewById(R.id.actionbarFunk2);
        final TextView actionbar_title = (TextView)findViewById(R.id.actionbarTxt);
        actionbar_AddNews.setBackgroundResource(android.R.drawable.ic_input_add);
        actionbar_Reload.setBackgroundResource(R.drawable.ic_refresh);
        actionbar_title.setText("Spielpläne");
        
        actionbar_AddNews.setVisibility(View.GONE);
        actionbar_Reload.setVisibility(View.GONE);
        actionbar_home.setOnClickListener(btnHomeListener);
    	
    }
    
    
    private OnClickListener btnListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(PlayingSchedule_Teams.this,PlayingSchedule.class);
        	lNextIntent.putExtra("sender", ((Button)v).getText());
        	startActivity(lNextIntent);
        }
    };
    
    private OnClickListener btnNextGamesListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(PlayingSchedule_Teams.this,PlayingSchedule_NextGames.class);
        	startActivity(lNextIntent);
        }
    };
    
    private OnClickListener btnHomeListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent lNextIntent = new Intent(PlayingSchedule_Teams.this,VBCMaltersFanAppActivity.class);
        	startActivity(lNextIntent);
        }
    };
	
}
