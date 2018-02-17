package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

enum state {NEUTRAL,DISLIKE,FAVORITE};

public class MusicPlayer extends AppCompatActivity {


    public static final String SONG_FINISHED = "SONG FINISHED";

    private ImageView albumCover;
    private TextView locationTitle;
    private TextView songTitle;
    private TextView artistTitle;
    private ImageButton playBtn;
    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private SeekBar seekBar;
    private Button favBtn;
    private TextView startTime;
    private TextView endTime;

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

    private int timeofday = 0;
    private int day = 0;
    private double lat = 0;
    private double lng = 0;

    private ArrayList<SongData> songs;
    private int cur_song;

    private MusicService musicService;
    private Intent playIntent;
    private boolean isBound = false;

    private Handler mHandler = new Handler();
    private LocalBroadcastManager bManager;
    private Location location;

    //private enum state {NEUTRAL,DISLIKE,FAVORITE};
    private int songState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //grab data from intent
        songs = (ArrayList<SongData>) getIntent().getSerializableExtra("SONGS");
        cur_song = getIntent().getIntExtra("CUR",0);

        //display song for aesthetics
        Toast toast = Toast.makeText(getApplicationContext(), songs.get(cur_song).getTitle(), Toast.LENGTH_SHORT);
        toast.show();


        initViews();
        initListeners();
        initBroadcast();
        initLocation();


    }

    // -- inner class variables -- //
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicService = binder.getService();
            musicService.setSongList(songs);
            mediaPlayer = musicService.getPlayer();
            isBound = true;

            playSong();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SONG_FINISHED)) {

                String serviceJsonString = intent.getStringExtra("hi");
                Log.d("Broadcast", serviceJsonString);
                Log.d("current index",String.valueOf(cur_song));

                playNextSong();

            }
        }
    };

    // -- class specific methods -- //
    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);

        }
    }

    @Override
    protected void onDestroy() {
        bManager.unregisterReceiver(bReceiver);
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
        unbindService(musicConnection);
    }


    // -- functional methods -- //

    public void setStateButton(){

        if(songState == state.NEUTRAL.ordinal()){
            favBtn.setText("+");
            favBtn.setBackgroundColor(Color.WHITE);
        }else if(songState == state.DISLIKE.ordinal()){
            favBtn.setText("x");
            favBtn.setBackgroundColor(Color.RED);
        }else if(songState == state.FAVORITE.ordinal()){
            favBtn.setText("\u2714");
            favBtn.setBackgroundColor(Color.GREEN);
        }

    }

    public void checkSongState(SongData song){

        Map<String,?> map = SharedPrefs.getData(this.getApplicationContext(),song.getID());

        if(map.get("State") != null){
            songState = ((Integer) map.get("State")).intValue();
        }else{
            songState = state.NEUTRAL.ordinal();
        }

        setStateButton();

    }

    public void setSong(int songIndex){
        cur_song = songIndex;
    }

    public void playSong() {

        checkSongState(songs.get(cur_song));

        //This code ensures that no disliked songs will play
        /*
        int count = 0;
        while(songState == state.DISLIKE.ordinal()){
            if(count >= songs.size())
                break;
            playNextSong();
            checkSongState(songs.get(cur_song));
            count++;
        }
        */

        musicService.setCurrentSong(cur_song);
        musicService.playSong();
        setupPlayer(songs.get(cur_song));

    }

    public void playNextSong(){

        if (++cur_song > songs.size()-1)
            cur_song = 0;

        Log.d("new index",String.valueOf(cur_song));

        playSong();


    }

    public void playPrevSong() {

        if(--cur_song < 0)
            cur_song = songs.size()-1;

        Log.d("new index",String.valueOf(cur_song));

        playSong();


    }

    public void trackSong() {

        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int curPos = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(curPos);
                    startTime.setText(String.format("%02d:%02d", (curPos % 36000) / 60, (curPos % 60)));
                    mHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void saveSong(SongData song) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        Map<String,?> map = SharedPrefs.getData(this.getApplicationContext(),song.getID());
        int timesPlayed = ((Integer)map.get("Times played")).intValue();
        timesPlayed++;


        SharedPrefs.saveData(getApplicationContext(), song.getID(), (float)lat, (float)lng, day, timeofday, 0, songState, timesPlayed, timeStamp);

    }


    public void setupPlayer(SongData song){

        //set up song UI
        Bitmap bp = SongParser.albumCover(song,getApplicationContext());
        if(bp != null) {
            albumCover.setImageBitmap(bp);
        }
        songTitle.setText(song.getTitle());
        artistTitle.setText(song.getArtist());

        //set up seeking UI
        final int dur = mediaPlayer.getDuration() / 1000;
        seekBar.setMax(dur);
        Log.d("DUR", String.valueOf(mediaPlayer.getDuration()));
        endTime.setText(String.format("%02d:%02d", (dur % 36000) / 60, (dur % 60)));

        trackSong(); //thread to update seek bar
        initLocation(); //refresh lat/long and display location
        initTimeDay(); //get formatted time and date

        //saveSong(song); //save data to shared preferences

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initViews() {

        playBtn = findViewById(R.id.playBtn);
        nextBtn = findViewById(R.id.nextBtn);
        favBtn = findViewById(R.id.favBtn);
        prevBtn = findViewById(R.id.prevBtn);
        favBtn =  findViewById(R.id.favBtn);
        seekBar =  findViewById(R.id.seekBar);
        startTime =  findViewById(R.id.startTime);
        endTime =  findViewById(R.id.endTime);
        albumCover =  findViewById(R.id.albumCover);
        songTitle =  findViewById(R.id.songTitle);
        artistTitle =  findViewById(R.id.artistTitle);
        locationTitle = findViewById(R.id.locationTitle);

    }

    @SuppressWarnings("ClickableViewAccessibility")
    @SuppressLint("ClickableViewAccessibility")
    public void initListeners() {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlayingMusic = true;
                playBtn.setImageResource(android.R.drawable.ic_media_pause);
                playNextSong();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlayingMusic = true;
                playBtn.setImageResource(android.R.drawable.ic_media_pause);
                playPrevSong();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayingMusic) {
                    musicService.pauseSong();
                    isPlayingMusic = false;
                    playBtn.setImageResource(android.R.drawable.ic_media_play);
                }
                else {
                    musicService.resumeSong();
                    isPlayingMusic = true;
                    playBtn.setImageResource(android.R.drawable.ic_media_pause);
                }

            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        favBtn.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(MusicPlayer.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    if(songState == state.NEUTRAL.ordinal())
                        songState = state.DISLIKE.ordinal();
                    else if(songState == state.DISLIKE.ordinal())
                        songState = state.NEUTRAL.ordinal();

                    Log.d("STATE", String.valueOf(songState));

                    setStateButton();

                    SharedPrefs.updateFavorite(MusicPlayer.this.getApplicationContext(),songs.get(cur_song).getID(),songState);

                    return super.onDoubleTap(e);

                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    Log.d("TEST", "onSingleTap");
                    if(songState == state.NEUTRAL.ordinal())
                        songState = state.FAVORITE.ordinal();
                    else if(songState == state.FAVORITE.ordinal())
                        songState = state.NEUTRAL.ordinal();

                    setStateButton();

                    Log.d("STATE", String.valueOf(songState));

                    SharedPrefs.updateFavorite(MusicPlayer.this.getApplicationContext(),songs.get(cur_song).getID(),songState);

                    return false;
                }

            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetector.onTouchEvent(event);
                return true;

            }
        });

    }

    public void initLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat = location.getLatitude();
        lng = location.getLongitude();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, lng, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                String loc_name = String.valueOf(listAddresses.get(0).getAddressLine(0));
                locationTitle.setText(loc_name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initTimeDay() {
        timeofday = getTimeOfDay();
        day = getDay();
        Log.i("time of day", String.valueOf(timeofday));
        Log.i("day", String.valueOf(day));

    }

    public void initBroadcast() {
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SONG_FINISHED);
        bManager.registerReceiver(bReceiver, intentFilter);
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

}