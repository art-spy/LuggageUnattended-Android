package com.example.leluun;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.leluun.R;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
 

public class GpsFragment extends Fragment {
 
	private Button Location, Luggage, Entfernung;
	gps gps;
	private TextView LocX, LocY, LugX, LugY, Ergebnis;
	Orthodrome Berechne = new Orthodrome();
	double Xloc , Yloc, Xlug, Ylug; 
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        
        Location = (Button)rootView.findViewById(R.id.button_Location);
        Luggage = (Button)rootView.findViewById(R.id.button_luggage);
        Entfernung = (Button)rootView.findViewById(R.id.button_entfernung);
        LocX = (TextView)rootView.findViewById(R.id.txtlocX);
        LocY = (TextView)rootView.findViewById(R.id.txtlocY);
        LugX = (TextView)rootView.findViewById(R.id.txtlugX);
        LugY = (TextView)rootView.findViewById(R.id.txtlugY);
        Ergebnis = (TextView)rootView.findViewById(R.id.txtEntfern);
        
        Location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Location(v);
            }
        });
        
        Luggage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Luggage(v);
            }
        });
        
        Entfernung.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Entfernung(v);
            }
        });
        
        return rootView;
    }
    
    public void Location(View view){
    	gps = new gps(getActivity().getApplicationContext());
    	 if(gps.canGetLocation()){
             
             double latitude = gps.getLatitude();
             double longitude = gps.getLongitude();
             
             Xloc = latitude;
             Yloc = longitude;
             LocX.setText("" + latitude);
             LocY.setText("" + longitude);

         }else{

             gps.showSettingsAlert();
         }
     }
    
    
    public void Luggage(View view){
    	
    	 
     	
     	AsyncTask<Integer, Void, ArrayList<Double>> connectTask = new AsyncTask<Integer, Void, ArrayList<Double>>() {
     		
     		StringBuilder sb = null;
     		JSONArray jArray = null;
     		String result = null;
     		ArrayList<Double> ergebnis = new ArrayList<Double>();
         	InputStream is = null;

    		@Override
    		protected ArrayList<Double> doInBackground(Integer...params) {
    		try {
    	     	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	     	try{
    	     	     HttpClient httpclient = new DefaultHttpClient();
    	     	     HttpPost httppost = new HttpPost("http://192.168.178.24/android_connect/myFile.php");
    	     	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     	     HttpResponse response = httpclient.execute(httppost);
    	     	     HttpEntity entity = response.getEntity();
    	     	     is = entity.getContent();
    	     	     }catch(Exception e){
    	     	         Log.e("log_tag", "Error in http connection"+e.toString());
    	     	    }
    	     	
    	     	try{
    	     	      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	     	       sb = new StringBuilder();
    	     	       String line= null;
    	     	       while ((line = reader.readLine()) != null) {
    	     	                      sb.append(line + "\n");
    	     	        }
    	     	        is.close();
    	     	        result=sb.toString();
    	     	        }catch(Exception e){
    	     	              Log.e("log_tag", "Error converting result "+e.toString());
    	     	        }
    	     	
    	     	try{
    	     	      jArray = new JSONArray(result);
    	     	      JSONObject json_data=null;
    	     	      for(int i=0;i<jArray.length();i++){
    	     	             json_data = jArray.getJSONObject(i);
    	     	            ergebnis.add(0, json_data.getDouble("posx"));
    	     	            ergebnis.add(1, json_data.getDouble("posy"));
    	     	         }
    	     	      }
    	     	      catch(JSONException e1){
    	     	      } catch (ParseException e1) {
    	     	   e1.printStackTrace();
    	     	 }
    	     	
    	     	 
    		} catch (Exception e) {
    			Log.d("b", e.getMessage());	
    		}
    		return ergebnis;
    		}
    		@Override
    		protected void onPostExecute(ArrayList<Double> result) {
    			LugX.setText("" + result.get(0));
    			LugY.setText("" + result.get(1));
    			Xlug = result.get(0);
                Ylug = result.get(1);
    			
    		}
    		};
    		connectTask.execute();
    	
	}
    
    public void Entfernung(View view){
    	DecimalFormat f = new DecimalFormat("#0.000"); 
    	
    	Ergebnis.setText(f.format(Berechne.entfernung(Xloc, Yloc, Xlug, Ylug )) + "Km" ); 
    }
    

   }
    
    

