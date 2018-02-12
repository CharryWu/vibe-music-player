package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button songButton;
    Button albumButton;

    ImageButton flashBackButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


<<<<<<< HEAD
=======

        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlashBackActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


/**
        SharedPreferences pref = getSharedPreferences("initial_setup", MODE_PRIVATE);
        Map<String, ?> setup = pref.getAll();
        Boolean v = pref.getAll().containsKey("completedSetUp");
        if (!v) {
            SharedPreferences songsList = getSharedPreferences("songslist", MODE_PRIVATE);
            SharedPreferences.Editor metaEdit = songsList.edit();

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();

            int id;
            int song_length;
            String album_title;
            String song_title;
            String song_path;

            File file = new File(R.raw.class.toString());
            File[] list = file.listFiles();

            for (File f : list) {
                String path = f.getName();
                mmr.setDataSource(path);
                if (path.endsWith(".mp3")) {
                    song_path = f.getAbsolutePath();

                    album_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    song_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    id = (album_title + song_title).hashCode();
                    song_length = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                    SharedPreferences newSong = getSharedPreferences(String.valueOf(id), MODE_PRIVATE);
                    SharedPreferences.Editor songadd = newSong.edit();

                    songadd.putFloat("Latitude", 0);
                    songadd.putFloat("Longitude", 0);
                    songadd.putInt("Day", 0);
                    songadd.putInt("Time", 0);
                    songadd.putString("Album Time", album_title);
                    songadd.putInt("Song Length", song_length);
                    songadd.putString("Song path", song_path);
                    songadd.putFloat("Rating", 0);
                    songadd.putInt("Times played", 0);
                    songadd.putInt("Last played", 0);

                    songadd.commit();
                }

            }
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("completedSetUp", "true").apply();
        }
    */

>>>>>>> 24f2f82c4eed91401256a428f98ce1e18087dba3
    }
}

