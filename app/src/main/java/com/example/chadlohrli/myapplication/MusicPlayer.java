package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

>>>>>>> 24f2f82c4eed91401256a428f98ce1e18087dba3
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.MediaPlayer;
<<<<<<< HEAD
=======
import android.support.v4.app.ActivityCompat;

>>>>>>> 24f2f82c4eed91401256a428f98ce1e18087dba3
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MusicPlayer extends AppCompatActivity {

    private Button backBtn;
    private ImageView albumCover;
    private TextView locationTitle;
    private TextView songTitle;
    private TextView artistTitle;
    private ImageButton playBtn;
    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private ImageButton seekBar;
    private Button favBtn;

    private MediaPlayer mediaPlayer;
    private boolean isPlayingMusic = true;

    private final int MORNING = 0;
    private final int AFTERNOON = 1;
    private final int NIGHT = 2;

    private final int MONDAY = 0;
    private final int TUESDAY = 1;
    private final int WEDNESDAY = 2;
    private final int THURSDAY = 3;
    private final int FRIDAY = 4;
    private final int SATURDAY = 5;
    private final int SUNDAY = 6;

    private ArrayList<SongData> songs;
    private int cur_song;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lkl;

    public void setSong(int songIndex){
        cur_song = songIndex;
    }

    public void setupPlayer(SongData song){

        albumCover = (ImageView) findViewById(R.id.albumCover);
        songTitle = (TextView) findViewById(R.id.songTitle);
        artistTitle = (TextView) findViewById(R.id.artistTitle);

        Bitmap bp = SongParser.albumCover(song,getApplicationContext());
        if(bp != null) {
            albumCover.setImageBitmap(bp);
        }
        songTitle.setText(song.getTitle());
        artistTitle.setText(song.getArtist());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        backBtn = (Button) findViewById(R.id.backBtn);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        favBtn = (Button) findViewById(R.id.favBtn);
        prevBtn = (ImageButton) findViewById(R.id.prevBtn);

        //grab data from intent
        songs = (ArrayList<SongData>) getIntent().getSerializableExtra("SONGS");
        cur_song = getIntent().getIntExtra("CUR",0);

        //display song for now to ensure data has correctly been passed
        Toast toast = Toast.makeText(getApplicationContext(), songs.get(cur_song).getTitle(), Toast.LENGTH_SHORT);
        toast.show();

        setupPlayer(songs.get(cur_song));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicPlayer.this, SongListActivity.class);
                MusicPlayer.this.startActivity(intent);
                finish();
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayingMusic == true) {
                    mediaPlayer.pause();
                    isPlayingMusic = false;
                    playBtn.setImageResource(android.R.drawable.ic_media_play);
                }
                else {
                    mediaPlayer.start();
                    isPlayingMusic = true;
                    playBtn.setImageResource(android.R.drawable.ic_media_pause);
                }

            }
        });
        Resources res = this.getResources();
        int soundId = res.getIdentifier(songs.get(cur_song).getID(), "raw", this.getPackageName());
        Log.d("raw", Integer.toString(R.raw.gottagetoveryou));
        Log.d("soundId", Integer.toString(soundId));
        loadMedia(soundId);

        /**Listen for location update */

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //save data in shared preferences
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Chenged", location.toString());
                int timeOfDay = getTimeOfDay();
                int day = getDay();
                locationManager.removeUpdates(this);
                saveSongData(location, timeOfDay, day);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1","ins");
            return;
        }

        String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);






    }

<<<<<<< HEAD
=======
    public void loadMedia(int id) {
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(id);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
        catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    protected int getTimeOfDay() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour >= 17 && hour <= 5)
            return NIGHT;
        else if (hour >= 6 && hour <= 10 )
            return MORNING;
        else
            return AFTERNOON;
    }

    protected int getDay() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case 0: return MONDAY;
            case 1: return TUESDAY;
            case 2: return WEDNESDAY;
            case 3: return THURSDAY;
            case 4: return FRIDAY;
            case 5: return SATURDAY;
            default: return SUNDAY;

        }

    }

    protected void saveSongData(Location location, int timeOfDay, int day) {
        SharedPreferences sharedPreferences = getSharedPreferences(Integer.toString(cur_song), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.d("latitude", Double.toString(location.getLatitude()));
        Log.d("longitude", Double.toString(location.getLongitude()));
        editor.putString("latitude", Double.toString(location.getLatitude()));
        editor.putString("longitude", Double.toString(location.getLongitude()));
        editor.putString("timeOfDay", Integer.toString(timeOfDay));
        editor.putString("day", Integer.toString(day));

        editor.apply();
    }



>>>>>>> 24f2f82c4eed91401256a428f98ce1e18087dba3

    /* TODO:
    1) get location, time of day, and day of week
    2) save song data in shared preferences
    3) add all music player functionality (play,stop,seek,next)
    4) tap to favorite/dislike (changes button state and sharedPreferences
    5) handle next song and play in background (optional) ?




     */

}
