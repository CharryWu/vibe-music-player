package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.content.pm.PackageManager;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Button songButton;
    Button albumButton;
    private boolean canSend = false;

    ImageButton flashBackButton;
    String loc_name;
    String time;
    private ArrayList<SongData> completeList = new ArrayList<SongData>();

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences lp = getSharedPreferences("last song", MODE_PRIVATE);
        Map<String, ?> map = SharedPrefs.getData(getApplicationContext(), "last song");
        String title = lp.getString("song", "");
        TextView song = (TextView)findViewById(R.id.textView2);

        if(title.isEmpty()) {
            song.setText("No song played yet");
            return;
        }

        Object t = map.get("Last played");

        double lt = (double)lp.getFloat("Latitude", 0);
        double lng2 = (double)lp.getFloat("Longitude", 0);

        //Log.i("song last played", title);
        if(t != null) {
            time = t.toString();
        }
        song.setText(title);
        TextView deets = (TextView)findViewById(R.id.textView3) ;

        //get song's location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation((double)lt,(double) lng2, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                loc_name = "Last Played Location: " + listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deets.setText(loc_name + " at " + time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocationPermission();
        LocationHelper.getLatLong(getApplicationContext());

        songButton = (Button) findViewById(R.id.song_button);
        albumButton = (Button) findViewById(R.id.album_button);
        flashBackButton = (ImageButton) findViewById(R.id.flashback_button);

        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSend) {
                    Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                    MainActivity.this.startActivity(intent);
                }else{
                    checkLocationPermission();
                }
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSend) {
                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                    MainActivity.this.startActivity(intent);
                }else{
                    checkLocationPermission();
                }
            }
        });


        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSend) {
                    Intent intent = new Intent(MainActivity.this, FlashBackActivity.class);
                    MainActivity.this.startActivity(intent);
                }else{
                    checkLocationPermission();
                }
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        LocationHelper.getLatLong(getApplicationContext());
    }


    //https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime-on-android-6
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Can we use your location?")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            canSend = false;
            return false;
        } else {
            canSend = true;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        canSend = true;
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    canSend = false;

                }
                return;
            }

        }
    }
}

