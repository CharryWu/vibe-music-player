package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class SongListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<SongData> songs;
    private ArrayList<SongData> sendSongs;
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

            /*
            Map<String,?> map = SharedPrefs.getSongData(getApplicationContext(),song.getID());
            if(map.get("State") != null){
                if( ((Integer)map.get("State")).intValue() != state.DISLIKE.ordinal() )
                    sendSongs.add(song);
            }
            */

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


        //if a disliked song is picked, it is no longer disliked
        SongData song = songs.get(Integer.parseInt(view.getTag().toString()));
        Map<String,?> map = SharedPrefs.getSongData(this.getApplicationContext(),song.getID());
        if(map.get("State") != null){
           if( ((Integer)map.get("State")).intValue() == state.DISLIKE.ordinal() ){
               SharedPrefs.updateFavorite(getApplicationContext(),song.getID(),state.NEUTRAL.ordinal());
           }
        }


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
        Toast toast = Toast.makeText(getApplicationContext(), "Un-Disliked!", Toast.LENGTH_SHORT);
        toast.show();
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

