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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;

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
import java.text.SimpleDateFormat;

public class FlashBackActivity extends AppCompatActivity {
    private Location location;
    private DateHelper dateHelper;
    private ListView songlist;
    private ImageButton playFB;
    private TextView location_view;
    private TextView time_view;
    private ArrayList<SongData> flashbackList = new ArrayList<SongData>();
    private double ratings;
    private float lt = 0;
    private float lng2 = 0;
    double time = 0;
    double day = 0;
    String timestamp;
    //also add fav

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void songPicked(View view) {
        Intent intent = new Intent(FlashBackActivity.this, MusicPlayer.class);
        intent.putExtra("SONGS", flashbackList);
        intent.putExtra("CUR", Integer.parseInt(view.getTag().toString()));
        intent.putExtra("caller", "FlashBackActivity");
        FlashBackActivity.this.startActivity(intent);
        finish();
    }

    public void setDateHelper(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    public double matchTimeOfDay(double songTime) {
        //int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int hour = dateHelper.getCalendar().get(Calendar.HOUR_OF_DAY);
        //Log.i("TIMECURR", Integer.toString(hour));
        //Log.i("TIMESONG", Integer.toString((int)songTime));
        if (hour >= 5 && hour < 11) {
            hour = 0;
        } else if (hour > 17 && hour < 5){
            hour = 2;
        } else {
            hour = 1;
        }
        if(hour == (int)songTime){
            return 2;
        }
        return 0;
    }


    public double matchDay(double songDate) {
        //int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int day = dateHelper.getCalendar().get(Calendar.DAY_OF_WEEK);
        //Log.i("DAYCURR", Integer.toString(day));
        //Log.i("DAYSONG", Integer.toString((int)songDate));
        if ((int)songDate == day) {
            return 2;
        }
        return 0;
    }

    public double matchLocation(double distance) {
        double locRating = 0;
        if (distance <= 304.8) {
            locRating = 2;
            double temp = ((304.8 - distance)/304.8);
            locRating += temp;
        }
        return locRating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashback);

        location_view = (TextView) findViewById(R.id.location);
        time_view = (TextView) findViewById(R.id.time);

        setDateHelper(new DateHelper());
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(getApplicationContext(), "permissions messed up", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);

            //return;
        }

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lat = 0;
        double lng = 0;
        Date dp_hour = Calendar.getInstance().getTime();
        String timeShow = "Last Time Played:" + String.valueOf(dp_hour);
        time_view.setText(timeShow);
        //Toast.makeText(getApplicationContext(), timeShow, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "abc", Toast.LENGTH_LONG).show();
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
            if (map.size() != 0 && (map.get("Time") != null)) {
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
            SharedPrefs.updateRating(FlashBackActivity.this.getApplicationContext(), id, (float)ratings);
            //SharedPreferences pref = getSharedPreferences(id, MODE_PRIVATE);

            if (ratings >= 2) {
                SongData song = SongParser.parseSong(path, id, getApplicationContext());
                flashbackList.add(song);
            }
        }
        Collections.sort(flashbackList, new SongSorter(getApplicationContext()));

        //commented out listview of all flashback songs
        /*songlist = (ListView) findViewById(R.id.song_list);
        SongAdapter songadt = new SongAdapter(this, flashbackList);
        songlist.setAdapter(songadt);*/
        playFB = (ImageButton) findViewById(R.id.playfb);

        playFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlashBackActivity.this, MusicPlayer.class);
                intent.putExtra("SONGS", flashbackList);
                intent.putExtra("CUR", 0);
                intent.putExtra("caller", "FlashBackActivity");
                FlashBackActivity.this.startActivity(intent);
                finish();
            }
        });

        /* flashback debugger
           for(SongData songelem: flashbackList){
            Log.i("Song", songelem.getTitle());
            String id = songelem.getID();
            SharedPreferences pref = getSharedPreferences(id, MODE_PRIVATE);
            float ls = pref.getFloat("Rating", 0);
            Log.i("Rating", Float.toString(ls));
            String lp = pref.getString("Last played", "");
            Log.i("timestamp", lp );
            Log.i("fav", Integer.toString(pref.getInt("fav", 0)));
        }*/

        if (flashbackList.size() == 0) {
            location_view.setVisibility(View.INVISIBLE);
            time_view.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(getApplicationContext(), "Play Songs First Before Using Flashback!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}


