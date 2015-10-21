package com.example.nataliajastrzebska.lab1;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager lm;
    MyLocationListener locationListener;
    TextView tvDisplayPosition;
    TextView tvDisplaySpeedAndAlt;
    Location oldLocation;

    List<Location> locationList;
    Button btnAddLocation;
    TextView tvDistanceToMyLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (isLocationEnabled()) {
            zad2();
            zad4();
        }



    }

    private boolean isLocationEnabled(){
            int locationMode = 0;
            String locationProviders;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                try
                {
                    locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                return locationMode != Settings.Secure.LOCATION_MODE_OFF;
            }
            else
            {
                locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }
    }

    public void zad2(){
        setTvsForZad2();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 30,this);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void zad4() {
        setTVsForZad4();
        locationList = new ArrayList<>();
    }

    public void onAddLocationClicked(View view){
        Location addingLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationList.add(addingLocation);
        Toast.makeText(this, "Added location", Toast.LENGTH_LONG).show();
    }

    public void setTvsForZad2() {
        tvDisplayPosition = (TextView) findViewById(R.id.textView);
        tvDisplaySpeedAndAlt = (TextView) findViewById(R.id.textView3);
    }

    public void setTVsForZad4() {
        btnAddLocation = (Button) findViewById(R.id.button);
        tvDistanceToMyLocation = (TextView) findViewById(R.id.textView4);
    }

    double getDeltaTimeInMilliSeconds(Location newLocation) {
        return newLocation.getTime() - oldLocation.getTime();
    }

    double getDeltaTimeInSeconds(Location newLocation){
        return getDeltaTimeInMilliSeconds(newLocation)/1000;
    }

    double getDistanceInMeters(Location newLocation){
        double dLat = Math.toRadians(newLocation.getLatitude() - oldLocation.getLatitude());
        double dLon = Math.toRadians(newLocation.getLongitude() - oldLocation.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(oldLocation.getLatitude()))
                * Math.cos(Math.toRadians(newLocation.getLatitude())) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Math.round(6371000 * c);
    }

    double getSpeed(Location newLocation){
       // Toast.makeText(this, "Distance: "+getDistanceInMeters(newLocation) + " Time: " + getDeltaTimeInSeconds(newLocation), Toast.LENGTH_LONG ).show();
        return getDistanceInMeters(newLocation)/getDeltaTimeInSeconds(newLocation);
    }

    String getLongitudeDirection (Location newLocation){
        //double deltaLong = newLocation.getLongitude() - oldLocation.getLongitude();
        if (newLocation.getLongitude() > oldLocation.getLongitude()){
            return "E";
        }
        else if ( newLocation.getLongitude() == oldLocation.getLongitude())
            return "";
        else
            return "W";
    }
    String getLatitudeDirection( Location newLocation){
        if (newLocation.getLatitude() > oldLocation.getLatitude()){
            return "N";
        }
        else if (newLocation.getLatitude() == oldLocation.getLatitude())
            return "";
        else
            return "S";
    }

    String getDirection (Location newLocation){
        return getLatitudeDirection(newLocation) + getLongitudeDirection(newLocation);
    }

    boolean isLocationProper(Location newLocation){
        if(oldLocation != null) {
            if( oldLocation.getTime() == newLocation.getTime()) {
                return false;
            }
            return true;
        }
        return  false;
    }

    Location getClosestLocation() {
        Location location = locationList.get(0);
        double distanceInM = getDistanceInMeters(location);
        double tmpDist;
        for (int i = 1; i < locationList.size(); i++) {
            tmpDist = getDistanceInMeters(locationList.get(i));
            if (tmpDist<distanceInM){
                location = locationList.get(i);
                distanceInM = tmpDist;
            }
        }
        return  location;
    }

    @Override
    public void onLocationChanged(Location location) {
        tvDisplayPosition.setText("Lat: " + String.valueOf(location.getLatitude()) + " Lon: " + String.valueOf(location.getLongitude()));
        if( !isLocationProper(location)){
            oldLocation = null;
        }
        if (oldLocation != null) {
            tvDisplaySpeedAndAlt.setText("Speed(m/s): " + String.valueOf(getSpeed(location)) + " Dir: " + getDirection(location));
        }
        oldLocation = location;
        if(locationList.size()>0) {
            Location closestLocation = getClosestLocation();
            tvDistanceToMyLocation.setText("Distance: " + getDistanceInMeters(closestLocation) + " Direction: " + getDirection(closestLocation));
        }
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
