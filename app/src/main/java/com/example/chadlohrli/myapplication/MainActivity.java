package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button songButton;
    Button albumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songButton = (Button) findViewById(R.id.song_button);
        albumButton = (Button) findViewById(R.id.album_button);

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


    }
}

