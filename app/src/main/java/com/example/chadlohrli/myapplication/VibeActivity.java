package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VibeActivity extends AppCompatActivity {
    private DateHelper dateHelper;
    private Location location;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public double matchWeek(double songDate) {
        return 0;
    }

    public double matchLocation(double lat, double lng) {
        return 0;
    }

    public void setDateHelper(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    public Location getLoc(){
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            return loc;
        } else {
            Toast.makeText(getApplicationContext(), "Cannot Get Location", Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
            return null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.flashback);
        LocationHelper.getLatLong(getApplicationContext());
        vibe();
    }

    protected void vibe(){
        location = getLoc();

    }
}
