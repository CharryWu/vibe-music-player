package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class SongListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<SongData> songs;
    private int songState;
    private Button undislikeBtn;


    public ArrayList<SongData> createSongs() {

        Field[] fields = R.raw.class.getFields();
        ArrayList<SongData> songs = new ArrayList<SongData>();

        for (int count = 0; count < fields.length; count++) {

            Log.i("Raw Asset:", fields[count].getName());

            String path = "android.resource://" + getPackageName() + "/raw/";
            String Id = fields[count].getName();

            SongData song = SongParser.parseSong(path, Id, getApplicationContext());

            songs.add(song);
        }

        //sort songs
        Collections.sort(songs, new Comparator<SongData>() {
            @Override
            public int compare(SongData a, SongData b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        return songs;
    }


    public void songPicked(View view) {
        Intent intent = new Intent(SongListActivity.this, MusicPlayer.class);
        intent.putExtra("SONGS", songs);
        intent.putExtra("CUR", Integer.parseInt(view.getTag().toString()));
        intent.putExtra("caller", "SongListActivity");
        SongListActivity.this.startActivity(intent);
    }

    public void dislikeAction(View view){

        Log.d("TAG",view.getTag().toString());
        SongData song = songs.get(Integer.parseInt(view.getTag().toString()));
        SharedPrefs.updateFavorite(getApplicationContext(),song.getID(),state.NEUTRAL.ordinal());
        undislikeBtn = view.findViewById(R.id.undislikeBtn);
        undislikeBtn.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist);

        songs = createSongs();

    }

    @Override
    protected void onStart() {
        super.onStart();

        listView = (ListView) findViewById(R.id.songlist);
        SongAdapter songadt = new SongAdapter(this, songs);
        listView.setAdapter(songadt);


    }




}

