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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class VibeActivity extends AppCompatActivity {
    private Location location;
    private ArrayList<SongData> vibeList = new ArrayList<SongData>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public int matchWeek(String songTimestamp) {
        Date curtime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date songTime = null;

        try {
            songTime = sdf.parse(songTimestamp);
        } catch (ParseException e){
            e.printStackTrace();
        }

        if((curtime.getTime() - songTime.getTime()) < 604800000) {
            return 2;
        }
        return 0;
    }

    public double matchLocation(Location songLoc) {
        double distance = songLoc.distanceTo(location);
        double locRating = 0;
        if (distance <= 304.8) {
            locRating += 2;
        }
        return locRating;
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
        DatabaseReference songs = myRef.child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String lp = snapshot.child("lastPlayed").getValue(String.class);
                    snapshot.child("rating")
                    if() {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Set unique<
    }
}
