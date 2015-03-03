package com.example.gcmclientapp;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListUsers extends Activity implements OnItemClickListener {
	
	private CardArrayAdapter cardArrayAdapter;
	String regPassToServer;
	String regId,userName,userName1;
	ListView lv;
	InputStream is = null;
	List<String> listRegId;
	List<String> listNames;
	Bundle b;
	Button bb;
	String Arr[]=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listusers);
    	ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336E7B")));
        lv = (ListView) findViewById(R.id.card_listView);
        bb = (Button) findViewById(R.id.brefresh);  
        regId = getIntent().getStringExtra("REGID");
        userName = getIntent().getStringExtra("USER");
        userName1 = getIntent().getStringExtra("NAME");
        
        lv.addHeaderView(new View(this));
        lv.addFooterView(new View(this));
        
        listNames = new ArrayList<String>();
        listRegId = new ArrayList<String>();
        
        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);
        //ArrayAdapter<String> str = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // showToast(regId);
        
        listNames = getTableValues();
        for(int i=0; i<listNames.size();i++)
        {
        	Card c = new Card(listNames.get(i).toUpperCase());
        	cardArrayAdapter.add(c);
        }
        lv.setAdapter(cardArrayAdapter); 
        
        lv.setOnItemClickListener(this);
  
        bb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent idk = new Intent(ListUsers.this,MainActivity.class);
				startActivity(idk);
			}
		});
        
    }

 	public List<String> getTableValues() {	
 		InputStream iss=null;
 		String line=null;
 		String result=null;
 		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			  
			  HttpClient httpClient=new DefaultHttpClient();
			  HttpPost httpPost=new HttpPost("http://sunaina.net84.net/retrieve.php");
			  HttpResponse response=httpClient.execute(httpPost);
			  HttpEntity entity=response.getEntity();
			  iss=entity.getContent();
			}
		catch(ClientProtocolException e)
		{
			 System.out.println("exception 1  caught");	
		}
		catch(IOException e)
		{
			 Log.e("log_tag","ioexception");
			 e.printStackTrace();
		}
 		try{
 			 BufferedReader reader=new BufferedReader(new InputStreamReader(iss,"iso-8859-1"),8);
 			 StringBuilder sb=new StringBuilder();
 			 while((line=reader.readLine())!=null)		
				sb.append(line+"\n");
				result=sb.toString();
				//result now contains the data in the form of json
				iss.close();
				System.out.println("here is my data");
				System.out.println(result);
 		}
 		catch(Exception e)
 		{
 			System.out.println("exception 2 caught");
 		}
 		List<String> list = new ArrayList<String>();
 		try{
 			
 			JSONArray jArray=new JSONArray(result);
 			int count=jArray.length();
 			
 			 for(int i=0;i<count;i++)
             {
                 JSONObject json_data=jArray.getJSONObject(i);
                 list.add(json_data.getString("name"));   // name is the name of attribute
                 listRegId.add(json_data.getString("regId")); //regId is the name of attribute in mysql database table
             }
 		}
 		catch(Exception e)
 		{
 			System.out.println("Ok");
 		}
 		return list;
 	}
 	
 	void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
 	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		regPassToServer=listRegId.get(position-1);
		Intent n = new Intent(ListUsers.this,GcmServer.class);
		n.putExtra("regforserver", regPassToServer);
		n.putExtra("userforserver", listNames.get(position-1));
		if(userName!=null){
			n.putExtra("fromuser", userName);
		}else{
			n.putExtra("fromuser", userName1);
		}
		if(n != null){
	    	try {
				startActivity(n);
				finish();
		    }catch (Exception x){
	    	    x.printStackTrace();
	    	 }
	    }	
	}
 }