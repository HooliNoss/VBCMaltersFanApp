package ch.vbcmalters.fanapp;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class UploadImage extends Activity 
{
	InputStream is;		
	Intent photoPickerIntent;
	
		@Override
		public void onCreate(Bundle icicle) 
		{
	
			super.onCreate(icicle);

			photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, 1);
			
		
		}
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			final Intent tmpData = data;
			if (tmpData != null)
			{
				new Thread(new Runnable() 
				{
					public void run() 
					{	
								
				            //Uri selectedImage = photoPickerIntent.getData();
							Uri selectedImage = tmpData.getData();
				            InputStream imageStream;
							try 
							{
								imageStream = getContentResolver().openInputStream(selectedImage);
								Bitmap bitmapOrg = BitmapFactory.decodeStream(imageStream);
								
								
								//bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
				    			
								/*
				                int width = bitmapOrg.getWidth();
				                int height = bitmapOrg.getWidth();
				    			
				                // calculate the scale
				                float scaleWidth = 0.09f;
				                float scaleHeight = 0.09f;
				                */
								
					            int width = bitmapOrg.getWidth();
					            int height = bitmapOrg.getHeight();
					            
					            int newWidth = width;
					            int newHeight = height;
					            
					            if (height > 900)
					            {
						            newWidth = width / 4;
						            newHeight = height / 4;
					            }
					            
					            // calculate the scale - in this case = 0.4f
					            float scaleWidth = ((float) newWidth) / width;
					            float scaleHeight = ((float) newHeight) / height;
					            
								
				                // createa matrix for the manipulation
				                Matrix matrix = new Matrix();
				                // resize the bit map
				                matrix.postScale(scaleWidth, scaleHeight);
				         
				                // recreate the new Bitmap
				                Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
				                                  width, height, matrix, true);
				                
				    			ByteArrayOutputStream bao = new ByteArrayOutputStream();
					    		
				    			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				    		
				    			byte [] ba = bao.toByteArray();
				    		
				    			String ba1=Base64.encodeBytes(ba);
				    		
				    			ArrayList<NameValuePair> nameValuePairs = new
				    		
				    			ArrayList<NameValuePair>();
				    		
				    			nameValuePairs.add(new BasicNameValuePair("image",ba1));
				    		
				    			try
				    			{
				    		
				    				HttpClient httpclient = new DefaultHttpClient();
				    			
				    				HttpPost httppost = new
				    			
				    				HttpPost("http://hslu.ath.cx/boli/UploadImage.php");
				    			
				    				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				    			
				    				HttpResponse response = httpclient.execute(httppost);
				    				
				    				HttpEntity entity = response.getEntity();
				    			
				    				is = (InputStream) entity.getContent();
				    				
									UploadImage.this.runOnUiThread(new Runnable() {
									    public void run() {
									       	  // Create new Dialog
									       	  final Dialog dialog = new Dialog(UploadImage.this);
									       	  dialog.setTitle("Info");
									       	  TextView txtDescription = new TextView(UploadImage.this);
									       	  txtDescription.setPadding(10, 0, 0, 10);
									       	  		       	  
									       	  txtDescription.setText("Bild erfolgreich auf den Server geladen.");
									       	  dialog.setContentView(txtDescription);
									             
									          dialog.setCanceledOnTouchOutside(true);
									          dialog.show();
									          
									    }
									});
				    		
				    			}
				    			catch(Exception e)
				    			{
				    				Log.e("log_tag", "Error in http connection "+e.toString());
				    			}
							} 
							catch (FileNotFoundException e1) 
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}		            		
					}
				}).start();
			}
		}

}
