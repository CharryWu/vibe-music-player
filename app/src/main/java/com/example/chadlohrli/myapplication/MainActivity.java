package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
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
    Button songButton;
    Button albumButton;

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

        //loadLastSong();

        songButton = (Button) findViewById(R.id.song_button);
        albumButton = (Button) findViewById(R.id.album_button);
        flashBackButton = (ImageButton) findViewById(R.id.flashback_button);

        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlashBackActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}

