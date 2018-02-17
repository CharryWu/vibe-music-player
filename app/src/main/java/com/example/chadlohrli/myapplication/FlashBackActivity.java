package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FlashBackActivity extends AppCompatActivity {
    private Location location;
    private ListView songlist;
    private TextView location_view;
    private TextView time_view;
    private ArrayList<SongData> flashbackList = new ArrayList<SongData>();
    private double ratings;
    private float lt = 0;
    private float lng2 = 0;
    double time = 0;
    double day = 0;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void songPicked(View view) {
        Intent intent = new Intent(FlashBackActivity.this, FlashBackActivity.class);
        intent.putExtra("SONGS", flashbackList);
        intent.putExtra("CUR", Integer.parseInt(view.getTag().toString()));
        FlashBackActivity.this.startActivity(intent);
        finish();
    }

    protected double matchTimeOfDay(double songTime) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour == songTime) {
            return 2;
        }
        return 0;
    }

    protected double matchDay(double songDate) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (songDate == day) {
            return 2;
        }
        return 0;
    }

    protected double matchLocation(double distance) {
        double locRating = 0;
        if (distance <= 304.8) {
            locRating = 2;
            double temp = ((304.8 - distance)/304.8);
            locRating += temp;
        }
        return locRating;
    }

    protected double tiebreaker(ArrayList<SongData> songs){
        Collections.sort(songs, new Comparator<SongData>()
        {
            @Override
            public int compare(SongData lhs, SongData rhs) {
                String lid = lhs.getID();
                String rid = rhs.getID();
                SharedPreferences lpref = getSharedPreferences(lid, MODE_PRIVATE);
                SharedPreferences rpref = getSharedPreferences(lid, MODE_PRIVATE);
                double lrate = (double) lpref.getFloat("Rating", 0);
                double rrate = (double) rpref.getFloat("Rating", 0);



                return Integer.valueOf(rhs.getDistance()).compareTo(lhs.getDistance());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashback);

        location_view = (TextView) findViewById(R.id.location);
        time_view = (TextView) findViewById(R.id.time);


        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lat = 0;
        double lng = 0;
        Date dp_hour = Calendar.getInstance().getTime();
        time_view.setText("Last Time Played:" + String.valueOf(dp_hour));

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }

        //get location name
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, lng, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                String loc_name = "Last Played Location: " + listAddresses.get(0).getAddressLine(0);
                location_view.setText(loc_name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //do address thing
        Field[] fields = R.raw.class.getFields();
        String path = "android.resource://" + getPackageName()+"/raw/";
        for (int i = 0; i < fields.length; i++) {
            String id = fields[i].getName();
            Map<String, ?> map = SharedPrefs.getData(getApplicationContext(), id);
            if (map.size() != 0) {
                Object latitude = map.get("Latitude");
                Object longitude = map.get("Longitude");
                Object t = map.get("Time");
                Object d = map.get("Day");

                lt = Float.valueOf(latitude.toString());
                lng2 = Float.valueOf(longitude.toString());
                time = (int)t;
                day = (int)d;
            }

            //get song's location
            Location loc = new Location("Song Location");
            loc.setLatitude(lt);
            loc.setLongitude(lng2);

            time = (int) matchTimeOfDay(time);
            day = (int) matchDay(day);
            double dist = location.distanceTo(loc);
            dist = matchLocation(dist);
            ratings = time+day+dist;
            SharedPreferences pref = getSharedPreferences(id, MODE_PRIVATE);
            pref.edit().putFloat("Rating", (float)ratings);
            pref.edit().commit();

            if (ratings >= 2) {
                SongData song = SongParser.parseSong(path, id, getApplicationContext());
                flashbackList.add(song);
            }
        }

        orderSongs(flashbackList);
        songlist = (ListView) findViewById(R.id.song_list);
        SongAdapter songadt = new SongAdapter(this, flashbackList);
        songlist.setAdapter(songadt);
    }

    public void orderSongs(ArrayList<SongData> songs){

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

    /**
     * TODO PART 2:
     *   1. Flashback button with unchangable playlist
     *   2. Updae location, daya and time and get new Flashback Mix
     *   3. Sort and find the right songs
     */
    /*public class sort implements Comparable<sort>{

        private double rating;
        private int fav;
        private double timestamp;

        public sort(Song)
        @Override
        public int compareTo(sort f){

        }
    }*/
}
