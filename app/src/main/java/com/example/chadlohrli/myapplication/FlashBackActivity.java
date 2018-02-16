package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

public class FlashBackActivity extends AppCompatActivity {
    Location curLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashback);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.d("Chenged", location.toString());
                curLocation = location;
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            //Log.d("test1", "ins");
            return;
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        //curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(locationProvider, 0, 10, locationListener);
        //Assuming Location location stores correct location

    }
        /**
        TODO:
          1. Track last known location
          2. Use locations distance method to store all songs played in the given radius, track
             minimum distance
          3. Get time of day, day of week
          4. Give ratings to all songs, create arraylist of selected songs (Unfavorated songs
             should not be considered)
          5. Location: within 1000 feet: give rating 2 to 3
          6. Time of Day: Give rating of 2 if it matches
          7. Day of Week: Give rating of 2 if it matches
          8. Favorited: tiebreaker
          Time stamp: tiebreaker
          9. Add ratings, consider songs with > 2 rating
          10. Sort by highest ratings to give recommendations
          11. Handle location changes (automatic),time of day and day of week (manual)
         **/

        protected double matchTimeOfDay(int songTime) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour == songTime) {
                return 2;
            }
            return 0;
        }

        protected double matchDay(int songDate) {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (songDate == day) {
                return 2;
            }
            return 0;
        }

        protected double matchLocation(Location songLocation) {
            double distance = curLocation.distanceTo(songLocation);
            double locRating = 0;
            if (distance <= 304.8) {
                locRating = 2;
                double temp = ((304.8 - distance)/304.8);
                locRating += temp;
            }
            return locRating;
        }
}
