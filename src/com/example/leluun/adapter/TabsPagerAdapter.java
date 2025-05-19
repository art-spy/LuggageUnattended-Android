package com.example.leluun.adapter;

import com.example.leluun.BlueToothFragment;
import com.example.leluun.MapsFragment;
import com.example.leluun.GpsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new GpsFragment();
        case 1:
            // Games fragment activity
            return new BlueToothFragment();
        case 2:
            // Movies fragment activity
            return new MapsFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}