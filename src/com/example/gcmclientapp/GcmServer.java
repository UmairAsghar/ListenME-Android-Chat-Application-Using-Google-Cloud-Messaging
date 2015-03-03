package com.example.gcmclientapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class GcmServer extends Activity {
	private static final String TAG = "GcmServer";
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    Intent intent;
    private boolean side = false;
	String regId,user,from;
    String msg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    	ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336E7B")));
        regId = getIntent().getStringExtra("regforserver");
        user = getIntent().getStringExtra("userforserver");
        from = getIntent().getStringExtra("fromuser");
        intent = new Intent(this, GcmMessageHandler.class);
        registerReceiver(broadcastReceiver, new IntentFilter("chatmessage"));
        
        buttonSend = (Button) findViewById(R.id.buttonSend);

        listView = (ListView) findViewById(R.id.listViewchat);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);

        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }    
    
    private boolean sendChatMessage(){
        chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));
        sendToServer(chatText.getText().toString(),regId);
        chatText.setText("");
        //side = !side;
        return true;
    }
    
    public void sendToServer(final String message,final String ID){
    	
    	new AsyncTask<String, Void, String>(){
		// changes are needed here
		@Override
		protected String doInBackground(String... params) {
			try {
				HttpResponse response = null;
				HttpParams httpParameters = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(httpParameters);
				String encodedurl = URLEncoder.encode(message,"UTF-8");
			    Log.i("TEST", encodedurl);
			    msg=from+":"+encodedurl;
				String url="http://sunaina.net84.net/gcm.php?" + "&regID="+ ID + "&message="+ msg; // changes needed here
				Log.i("Send URL:", url);
				
				HttpGet request = new HttpGet(url);
				
				response = client.execute(request);
				Log.i("responce URL:"," ");
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String webServiceInfo = "";
				while ((webServiceInfo = rd.readLine()) != null) {
					Log.d("****Status Log***", "Webservice: " + webServiceInfo);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
   }.execute(null,null,null);
  } 		
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getStringExtra("CHATMESSAGE"));
            chatArrayAdapter.add(new ChatMessage(true, intent.getStringExtra("CHATMESSAGE")));
        }
    };
}