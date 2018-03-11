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
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class VibeActivity extends AppCompatActivity {
    private Location location;
    private ArrayList<String> vibeList = new ArrayList<String>();
    private ArrayList<String> vibeListURLs = new ArrayList<String>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        myRef.child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String lp = snapshot.child("lastPlayed").getValue(String.class);
                    //int newR = snapshot.child("rating").getValue(int.class) + matchWeek(lp);
                    //snapshot.child("rating").getRef().setValue(newR);
                    int wR = matchWeek(lp);
                    SharedPrefs.updateRating(VibeActivity.this.getApplicationContext(), snapshot.getKey(), (float)wR);
                    for(DataSnapshot locs: snapshot.child("location").getChildren()){
                        double lat = locs.child("lat").getValue(double.class);
                        double lngt = locs.child("lngt").getValue(double.class);
                        Location playLoc = new Location("any");
                        playLoc.setLatitude(lat);
                        playLoc.setLongitude(lngt);
                        if(matchLocation(playLoc) == 2){
                            //double rat = snapshot.child("rating").getValue(int.class) + matchLocation(playLoc);
                            //snapshot.child("rating").getRef().setValue(rat);
                            SharedPreferences pref = getSharedPreferences(snapshot.getKey(), MODE_PRIVATE);
                            int curRate = pref.getInt("Rating", 0);
                            double locR = 2;
                            SharedPrefs.updateRating(VibeActivity.this.getApplicationContext(),
                                    snapshot.getKey(), (float)curRate + (float)locR);
                            break;
                        }
                    }
                    SharedPreferences pref = getSharedPreferences(snapshot.getKey(), MODE_PRIVATE);
                    int curRate = pref.getInt("Rating", 0);
                    if(curRate > 0) {
                        vibeList.add(snapshot.getKey());
                        vibeListURLs.add(snapshot.child("url").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //get CURRENT USER ID HERE
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String curId = currentUser.getUid();
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.child(curId).child("friends").getChildren()) {
                    String friendid = snapshot.getKey();
                    for(DataSnapshot fSongs: dataSnapshot.child(friendid).child("songs").getChildren()){
                        // GET ORIGINAL RATING IN NEWR
                        // int newR = myRef.child("songs").child(snapshot.getKey()).child("rating")
                        // CHANGE RATINGS BY ADDING 2
                        SharedPreferences pref = getSharedPreferences(fSongs.getKey(), MODE_PRIVATE);
                        int curRate = pref.getInt("Rating", 0);
                        SharedPrefs.updateRating(VibeActivity.this.getApplicationContext(),
                                snapshot.getKey(), (float)curRate + 2);
                        vibeList.add(snapshot.getKey());
                        vibeListURLs.add(snapshot.child("url").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Get unique song ids only
        Set<String> set = new HashSet<String>(vibeList);
        ArrayList<String> finalRec = new ArrayList<String>(set);

        Set<String> seturl = new HashSet<String>(vibeListURLs);
        ArrayList<String> finalRecURL = new ArrayList<String>(seturl);

        //PASS finalRecURL to Download Service and start downloads


    }
}
