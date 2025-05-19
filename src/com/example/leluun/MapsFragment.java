package com.example.leluun;

import com.example.leluun.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

 
public class MapsFragment extends Fragment {
	
    // Google Map
    private GoogleMap googleMap;
	
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

    	View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
    	
        try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    	
        return rootView;
    }
    
    
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            
            googleMap.setMyLocationEnabled(true);
            
         // latitude and longitude
            double latitude = 17.385044;
            double longitude = 78.486671;
             
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your luggage.");
             
            // adding marker
            googleMap.addMarker(marker);
            
            
            
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText( getActivity().getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
	public void onResume() {
        super.onResume();
        initilizeMap();
    }
    
    public void onDestroyView() 
    {
            super.onDestroyView(); 
            Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
            FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
    }
 
}