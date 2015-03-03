package com.example.gcmclientapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

	String user;
    String pass;
	Button btnlogin;
	EditText username,password;
	GoogleCloudMessaging gcm; 
	String PROJECT_NUMBER = "978727478";
	private static final String TAG = "SignUpActivity";
	String regId,temp;
	Bundle b;
	InputStream is = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336E7B")));
		
		btnlogin = (Button) findViewById(R.id.btnLogin);
		username = (EditText) findViewById(R.id.etUserName);
		password = (EditText) findViewById(R.id.etPassword);
		
		registerInBackground();
		
		btnlogin.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				user = username.getText().toString();  //change 1 in these two lines
		        pass = password.getText().toString();
		        if(user.length()>0 && pass.length()>0){
		        	addUser();
		        	storeRegistrationId(regId);
		        	Intent i = new Intent(MainActivity.this,ListUsers.class);
		        	i.putExtra("REGID", regId);
		        	i.putExtra("USER", user);
		        	i.putExtra("PASS", pass);
				
					if(i != null) {
			    	try {
						startActivity(i);
						finish();
				    }catch (Exception x) {
			    	    x.printStackTrace();
			    	 }
				   }
		        }
		        else{
		        	Toast.makeText(getApplicationContext(),"Empty Fields", Toast.LENGTH_SHORT).show();
		        }
			}
		});
	}
	
	protected void storeRegistrationId(String regId2) {
		final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("REG_ID", regId2);
        editor.putString("NAME",user);
        editor.commit();
	}
	
	 public void addUser(){
 		// TODO Auto-generated method stub
				StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    	StrictMode.setThreadPolicy(policy);
		    	List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(1);
		    	nameValuePairs.add(new BasicNameValuePair("regid", regId));
		    	nameValuePairs.add(new BasicNameValuePair("name", user));
			
			try {
				  //tv2.setText(regId);
				  HttpClient httpClient=new DefaultHttpClient();
				  HttpPost httpPost=new HttpPost("http://sunaina.net84.net/new.php");
				  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				  HttpResponse response=httpClient.execute(httpPost);
				  HttpEntity entity=response.getEntity();
				  is=entity.getContent();
				  Toast.makeText(getApplicationContext(), "Record Added", Toast.LENGTH_SHORT).show();
				 }
			catch(ClientProtocolException e)
			{
				  Log.e("clientProtocol","Log_tag");
				  e.printStackTrace();}
			catch(IOException e)
			{
				  Log.e("log_tag","ioexception");
				  e.printStackTrace();
			}
	 	}
	 
	 public void registerInBackground() {
			new AsyncTask<Void,Void,String>(){
				@Override
				protected String doInBackground(Void... arg0) {
					String msg = "";
					try{
						if(gcm == null){
							gcm =  GoogleCloudMessaging.getInstance(getApplicationContext());
						}
						
						regId = gcm.register(PROJECT_NUMBER);
						msg = "Device Registered, Reg id : " + regId;
						Log.i("GCM", "!!!!" + regId);
						//storeRegistrationId(regId);   
					}catch(IOException e){
						msg = "Error" + e.getMessage();		
					}
					Log.d(TAG, "AsyncTask completed: " + msg);
					return msg;
				}		
				protected void onPostExecute(String msg) {
	                Log.d(TAG, "Registered with GCM Server." + msg);
	            }
			}.execute(null,null,null);
			
		}
	
}
