package com.example.leluun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


import com.example.leluun.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class BlueToothFragment extends Fragment {
	
	private final int ID_NOT1 = 0;
	private final int ID_NOT2 = 1;
	NotificationManager notMan;
   private Button On,Off,Visible,list,refresh;
   private TextView Verbindung, Entfernung;
   private BluetoothAdapter BA;
   private Set<BluetoothDevice>pairedDevices;
   private ListView lv;
   private UUID uuid = UUID.fromString("b1414730-ad49-11e3-a5e2-0800200c9a66");
   private long entf;
   private ArrayAdapter<BluetoothDevice> aa;
   private ArrayList<BluetoothDevice> foundDevices;
   private BluetoothSocket socket;
   

   
   private  Handler handler = new Handler() {

       public void handleMessage(Message msg) {
            
           long aResponse = msg.getData().getLong("message");

           if ((0 != aResponse)) {

        	   setEntf(((aResponse -entf)*0.000000001 * 299792458) + " m");
        	   
           }
           else
           {

          	 Entfernung.setText("nope");
           }    

       }
   };;
 
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        notMan = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        
        On = (Button)rootView.findViewById(R.id.button_Location);
        Off = (Button)rootView.findViewById(R.id.button_luggage);
        Visible = (Button)rootView.findViewById(R.id.button_listen);
        list = (Button)rootView.findViewById(R.id.button_search);
        refresh = (Button)rootView.findViewById(R.id.buttonRefresh);
        lv = (ListView)rootView.findViewById(R.id.list_discovered);
        Verbindung = (TextView)rootView.findViewById(R.id.txtlocX);
        Entfernung = (TextView)rootView.findViewById(R.id.txtlugX);
        BA = BluetoothAdapter.getDefaultAdapter();
        
        
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter1);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter2);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter3);
        
        
        
        On.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	on(v);
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
            }
        });
        
        Off.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	off(v);
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
            }
        });
        
        Visible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	visible(v);
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
            }
        });
        
        list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	list(v);
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
            }
        });
        
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	refresh(v);
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
            }
        });
        
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            { 
            	liste(arg0, arg1, position, arg3);

            }
        });
        
         
        return rootView;
    }
    
    
    
    public void liste(AdapterView<?> arg0, View arg1,int position, long arg3){
		aa = new ArrayAdapter<BluetoothDevice>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1 , foundDevices);
		lv.setAdapter(aa);

		AsyncTask<Integer, Void, Void> connectTask = new AsyncTask<Integer, Void, Void>() {
		@Override
		protected Void doInBackground(Integer...params) {
		try {
		BluetoothDevice device = foundDevices.get(params[0]);
		socket = device.createRfcommSocketToServiceRecord(uuid);
		socket.connect();
		} catch (IOException e) {
		Log.d("BLUETOOTH_CLIENT", e.getMessage());
		}
		return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			BluetoothSocketListener bsl = new BluetoothSocketListener(socket, handler);
	    	Thread messageListener = new Thread(bsl);
	    	messageListener.start();
		}
		};
		connectTask.execute(position);

    }
    
      private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
              String action = intent.getAction();

              if (BluetoothDevice.ACTION_FOUND.equals(action)) {
              }
              else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
           	   showNotification("Verbunden mit Transmitter",false,ID_NOT1);
           	   Verbindung.setText("OK");
           	   refresh.setEnabled(true);
           	   lv.setEnabled(false);
           	   
              }
              else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
              }
              else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                  showNotification("Verbindung zum Transmitter Verloren",false,ID_NOT2);
                  Verbindung.setText("NO");
                  lv.setEnabled(true);
              }      
              
              if (intent.getAction() != null
                      && intent.getAction().equalsIgnoreCase("my_action")) {
            	  Entfernung.setText(intent.getStringExtra("message"));
              }
              
          }
      };
      
      public void on(View view){
          if (!BA.isEnabled()) {
             Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(turnOn, 0);
             Toast.makeText( getActivity().getApplicationContext(),"Turned on" 
             ,Toast.LENGTH_LONG).show();

          }
          else{
             Toast.makeText( getActivity().getApplicationContext(),"Already on",
             Toast.LENGTH_LONG).show();
             }
       }
      

      public void refresh(View view){

    	  sendMessage(socket, "hall0");

       }
       

      public void setEntf(String bla){
    	  
    	  Entfernung.setText(bla);
		  
       }
		private void sendMessage(BluetoothSocket socket, String msg) {
		OutputStream outStream;
		try {
		outStream = socket.getOutputStream();
		byte[] byteString = (msg + " ").getBytes();
		byteString[byteString.length - 1] = 0;
		entf = System.nanoTime();
		outStream.write(byteString);
		
		} catch (IOException e) {
		Log.d("BLUETOOTH_COMMS", e.getMessage());
		}
		}
		
      
      
       public void list(View view){
          pairedDevices = BA.getBondedDevices();
          foundDevices =  new ArrayList<BluetoothDevice>();
          ArrayList<String> list = new ArrayList<String>();
          
          
          for(BluetoothDevice bt : pairedDevices){
             list.add(bt.getName());
             foundDevices.add(bt);
          }
         
          

          Toast.makeText( getActivity().getApplicationContext(),"Showing Paired Devices",
          Toast.LENGTH_SHORT).show();
          final ArrayAdapter<String> adapter = new ArrayAdapter<String>
          (getActivity(),android.R.layout.simple_list_item_1, list);
          lv.setAdapter(adapter);

       }
      
       
  
		
		
       
       public void off(View view){
          BA.disable();
          Toast.makeText( getActivity().getApplicationContext(),"Turned off" ,
          Toast.LENGTH_LONG).show();
       }
       
       public void visible(View view){
          Intent getVisible = new Intent(BluetoothAdapter.
          ACTION_REQUEST_DISCOVERABLE);
          startActivityForResult(getVisible, 0);

       }

       
       
    	private void showNotification(String text, boolean ongoing, int id) {

    		Notification notification = new Notification(
    				android.R.drawable.stat_notify_sync, text,
    				System.currentTimeMillis());
    		if (ongoing) {
    			notification.flags = Notification.FLAG_ONGOING_EVENT;

    		} else {
    			notification.flags = Notification.FLAG_AUTO_CANCEL;
    		}
    		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    		notification.ledOnMS=2000;
    		notification.ledOffMS=1000;
    		PendingIntent contentIntent = PendingIntent
    				.getActivity( getActivity().getApplicationContext(), 0, new Intent( getActivity().getApplicationContext(),
    						MainActivity.class),
    						PendingIntent.FLAG_UPDATE_CURRENT);

    		notification.setLatestEventInfo(this.getActivity(), "NotificationActivity", text,
    				contentIntent);
    		notMan.notify(id, notification);

    	}
    	
    	

}
