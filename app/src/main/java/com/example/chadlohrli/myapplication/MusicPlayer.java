package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
    private ImageButton favBtn;

    private ArrayList<SongData> songs;
    private int cur_song;

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

        backBtn = (Button) findViewById(R.id.backBtn);

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


    }


    /* TODO:
    1) get location, time of day, and day of week
    2) save song data in shared preferences
    3) add all music player functionality (play,stop,seek,next)
    4) tap to favorite/dislike (changes button state and sharedPreferences
    5) handle next song and play in background (optional) ?




     */

}
