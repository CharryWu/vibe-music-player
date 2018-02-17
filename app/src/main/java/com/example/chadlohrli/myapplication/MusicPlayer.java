package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MusicPlayer extends AppCompatActivity {

    public static final String SONG_FINISHED = "SONG FINISHED";

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SONG_FINISHED)) {
                String serviceJsonString = intent.getStringExtra("hi");
                Log.d("Broadcast", serviceJsonString);

                setupPlayer(songs.get(cur_song++));

                Log.d("current index",String.valueOf(cur_song));

                playNextSong();


            }
        }
    };
    LocalBroadcastManager bManager;

    private Button backBtn;
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
    private Location location;
    private double lat = 0;
    private double lng = 0;

    private ArrayList<SongData> songs;
    private int cur_song;

    private MusicService musicService;
    private Intent playIntent;
    private boolean isBound = false;

    final Handler mHandler = new Handler();

    public void setSong(int songIndex){
        cur_song = songIndex;
    }

    public void playNextSong(){

        if (++cur_song > songs.size()-1)
            cur_song = 0;

        Log.d("new index",String.valueOf(cur_song));
        musicService.setCurrentSong(cur_song);
        musicService.playSong();
        setupPlayer(songs.get(cur_song));

    }

    public void playPrevSong() {

        if(--cur_song < 0)
            cur_song = songs.size()-1;

        Log.d("new index",String.valueOf(cur_song));
        musicService.setCurrentSong(cur_song);
        musicService.playSong();
        setupPlayer(songs.get(cur_song));

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

        //set up seeking
        final MediaPlayer mp = musicService.getPlayer();

        final int dur = mp.getDuration() / 1000;
        seekBar.setMax(dur);
        Log.d("DUR", String.valueOf(mp.getDuration()));
        Log.d("DUR",String.valueOf(mediaPlayer.getDuration()));

        endTime.setText(String.format("%02d:%02d", (dur % 36000) / 60, (dur % 60)));

        timeofday = getTimeOfDay();
        day = getDay();

        Log.i("time of day", String.valueOf(timeofday));
        Log.i("day", String.valueOf(day));

        SharedPrefs.saveData(getApplicationContext(), song.getID(), (float)lat, (float)lng, day, timeofday, 0, 0, 0);

        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(musicService != null) {
                    int curPos = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(curPos);
                    startTime.setText(String.format("%02d:%02d", (curPos % 36000) / 60, (curPos % 60)));
                    mHandler.postDelayed(this, 1000);
                }

            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        playBtn = (ImageButton) findViewById(R.id.playBtn);
        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        favBtn = (Button) findViewById(R.id.favBtn);
        prevBtn = (ImageButton) findViewById(R.id.prevBtn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);


        //grab data from intent
        songs = (ArrayList<SongData>) getIntent().getSerializableExtra("SONGS");
        cur_song = getIntent().getIntExtra("CUR",0);

        //display song for now to ensure data has correctly been passed
        Toast toast = Toast.makeText(getApplicationContext(), songs.get(cur_song).getTitle(), Toast.LENGTH_SHORT);
        toast.show();

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextSong();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPrevSong();
            }
        });



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        //location
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        lat = location.getLatitude();
        lng = location.getLongitude();


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayingMusic == true) {
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

        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SONG_FINISHED);
        bManager.registerReceiver(bReceiver, intentFilter);


        /**
        Resources res = this.getResources();
        int soundId = res.getIdentifier(songs.get(cur_song).getID(), "raw", this.getPackageName());
        Log.d("raw", Integer.toString(R.raw.gottagetoveryou));
        Log.d("soundId", Integer.toString(soundId));
        loadMedia(soundId);


        Listen for location update

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //save data in shared preferences
        final LocationListener locationListener = new LocationListener() {
        musicConnection = new ServiceConnection() {
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



        */


    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setSongList(songs);
            musicService.setCurrentSong(cur_song);
            musicService.playSong();
            isBound = true;

            mediaPlayer = musicService.getPlayer();
            setupPlayer(songs.get(cur_song));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

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




    /* TODO:
    1) get location, time of day, and day of week
    2) save song data in shared preferences
    3) add all music player functionality (play,stop,seek,next)
    4) tap to favorite/dislike (changes button state and sharedPreferences
    5) handle next song and play in background (optional) ?




     */

}
