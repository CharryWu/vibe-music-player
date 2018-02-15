package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Double.valueOf;

public class FlashBackActivity extends AppCompatActivity {

    private Button backbtn;
    private ListView songlist;
    private TextView location_view;
    private TextView time;
    private ArrayList<SongData> flashbackList = new ArrayList<SongData>();
    private Location curr_location;

    private int ratings;
    SongParser parser;
    SongData temp;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashback);

        location_view = (TextView) findViewById(R.id.location);

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
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //curr_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        /*String loc = String.valueOf(curr_location.getLatitude());
        if (curr_location == null) {
            Log.i("location not set", "hi");
        }
        location.setText(loc);*/

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, lng, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                String loc_name = listAddresses.get(0).getAddressLine(0);
                location_view.setText("Last Played Location: " + loc_name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        /*
        //do address thing
        Field[] fields = R.raw.class.getFields();
        String path = "android.resource://" + getPackageName()+"/raw/";

        for (int i = 0; i < fields.length; i++) {

            String id = fields[i].getName();
            Map<String, ?> map = SharedPrefs.getData(getApplicationContext(), id);

            //location distance
            //double lat;
            //String str = "Latitude";
            Object latitude = map.get("Latitude");
            Object longtitude = map.get("Longitude");
            Object t = map.get("Time");
            Object d = map.get("Day");
            Log.i("lat is:", latitude.toString());

            double lt = Double.valueOf(latitude.toString());
            double lng2 = Double.valueOf(longtitude.toString());
            int time = Integer.valueOf(t.toString());
            int day = Integer.valueOf(d.toString());

            Location loc = new Location("Song Location");
            loc.setLatitude(lt);
            loc.setLongitude(lng2);

            time = matchTime(time);
            day = matchDay(day);
            double dist = location.distanceTo(loc);

            //check w/n what range dist is at

            ratings = time+day+(int)dist;

            if (ratings >= 2) {

                SongData song = SongParser.parseSong(path, id, getApplicationContext());
                flashbackList.add(song);
            }

        }
        */


        Field[] fields = R.raw.class.getFields();
        String path = "android.resource://" + getPackageName()+"/raw/";
        String id = fields[0].getName();
        SongData test = SongParser.parseSong(path, id, getApplicationContext());
        flashbackList.add(test);

        Log.d("crashed", "here");

        songlist = (ListView) findViewById(R.id.song_list);

        SongAdapter songadt = new SongAdapter(this, flashbackList);
        songlist.setAdapter(songadt);

        Log.d("crashed", "here1");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlashBackActivity.this, MainActivity.class);
                FlashBackActivity.this.startActivity(intent);
                finish();
            }
        });

    }
}
