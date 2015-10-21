package com.example.nataliajastrzebska.lab1;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by nataliajastrzebska on 17/10/15.
 */
public class MyLocationListener implements LocationListener{

    Context mContext;
    public MyLocationListener(Context context) {
        mContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(mContext, "Changed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
