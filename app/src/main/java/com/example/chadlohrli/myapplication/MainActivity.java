package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlashBackActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}

