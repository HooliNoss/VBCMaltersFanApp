package ch.vbcmalters.fanapp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class PlayingScheduleAdapter extends SimpleAdapter 
{
	
	private ArrayList<HashMap<String, Object>> myList;
	/*
	* Alternating color list -- you could initialize this from anywhere.
	* Note that the colors make use of the alpha here, otherwise they would be
	* opaque and wouldn't give good results!
	*/
	private int[] colors = new int[] { 0x30ffffff, 0x30000000 };

	//@SuppressWarnings("unchecked")
	public PlayingScheduleAdapter(
			Context context, 
			ArrayList<HashMap<String, Object>> items, 
	        int resource, 
	        String[] from, 
	        int[] to) 
	{
	  super(context, items, resource, from, to);
	  this.myList =  items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  

	  try 
	  {
		  View view = super.getView(position, convertView, parent);
		
		  HashMap<String, Object> item = myList.get(position);
		  String dateString = (String)item.get("date");
		  String formatedDate = dateString.substring(0, 10); 
		  
		  Date now = new Date();  
		  Calendar cal = Calendar.getInstance();  
		  cal.setTime(now);  
		  cal.add(Calendar.DAY_OF_YEAR, -1); // <--  
		  Date yesterday = cal.getTime(); 
		  
	    
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  Date convertedDate = dateFormat.parse(formatedDate);
		  
		  int colorPos;
		  if (yesterday.after(convertedDate))
		  {
			  colorPos = 1;
		  }
		  else
		  {
			  colorPos = 0;
		  }
		  
		  view.setBackgroundColor(colors[colorPos]);
		  return view;
	  } 
	  catch (ParseException e) 
	  {
		return super.getView(position, convertView, parent);
	  } 
	  
	}
}
