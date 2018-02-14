package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.util.Pair;
import android.webkit.PermissionRequest;

/**
 * Created by chadlohrli on 2/12/18.
 */

public class LocationHelper {

    static private double lat;
    static private double lon;

    public static Pair<Double,Double> getLatLong(Context context) {

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //save data in shared preferences
        final LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.d("Changed", location.toString());
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("Lat:",String.valueOf(lat));
                Log.d("Long:",String.valueOf(lon));
                locationManager.removeUpdates(this);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }


        String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);


        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        return new Pair <Double,Double> (latitude,longitude);

    }



}
