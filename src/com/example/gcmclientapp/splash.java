package com.example.gcmclientapp;

import android.app.ActionBar;
import android.app.Activity; import android.content.Context;
import android.content.Intent; import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable; import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer; import android.os.Bundle; import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class splash extends Activity { 
	String TAG="User";
	@Override 
	protected void onCreate(Bundle bundle) {  
		super.onCreate(bundle); 
		setContentView(R.layout.splash); 
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		Thread timer = new Thread(){ 
			public void run(){ 
				try{ 
					sleep(2300); 
				}
				catch(InterruptedException e)
				{ 
					e.printStackTrace(); 
				} 
				finally{ 
					final SharedPreferences prefs = getSharedPreferences(
			                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
			        String registrationId = prefs.getString("REG_ID", "");
			        String Name = prefs.getString("NAME", "");
			        if (registrationId.isEmpty()) {
			            Log.i(TAG, "Registration not found.");
			            Intent a = new Intent(splash.this,MainActivity.class);
			            startActivity(a);
			        }
			        else
			        {
			        	Log.i(TAG, "Registration found.");
			            Intent b = new Intent(splash.this,ListUsers.class);
			            b.putExtra("NAME",Name);
			            startActivity(b);
			        }
				} 
				} 
			}; 
		timer.start(); } 
	@Override 
	protected void onPause() 
	{ // TODO Auto-generated method stub 
		super.onPause();  
		finish(); 
	} 
  }
