package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Button songButton;
    Button albumButton;
    private boolean canSend = false;

    ImageButton flashBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocationPermission();

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

    public void setUpListeners() {

    }

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
                        .setTitle("Location Permssion")
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
            return false;
        } else {
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

