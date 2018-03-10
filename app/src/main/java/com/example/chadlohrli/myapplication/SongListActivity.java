package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class SongListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView listView;
    private ArrayList<SongData> songs;
    private ArrayList<SongData> sendSongs;
    private int songState;
    private Button undislikeBtn;
    private BottomNavigationView bottomNav;
    private Spinner spinner;




    public ArrayList<SongData> createSongs() {



        File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        Log.v("Files",musicDirectory.exists()+"");
        Log.v("Files",musicDirectory.isDirectory()+"");
        Log.v("Files",musicDirectory.listFiles()+"");
        File[] fields = musicDirectory.listFiles();
        //int s = files.length;
        //files[0].getName();

        //Field[] fields = R.raw.class.getFields();
        ArrayList<SongData> songs = new ArrayList<SongData>();

        for (int count = 0; count < fields.length; count++) {


            Log.i("Raw Asset:", fields[count].getName());
            //String path = "android.resource://" + getPackageName() + "/raw/";
            String path  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
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

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        Intent searchIntent = new Intent(SongListActivity.this, DownloadActivity.class);
                        SongListActivity.this.startActivity(searchIntent);
                        break;
                    case R.id.my_library:
                        Intent homeIntent = new Intent(SongListActivity.this, MainActivity.class);
                        SongListActivity.this.startActivity(homeIntent);
                        break;
                }
                return true;
            }
        });

        songs = createSongs();

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drop_down, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();

        listView = (ListView) findViewById(R.id.songlist);
        SongAdapter songadt = new SongAdapter(this, songs);
        listView.setAdapter(songadt);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i) {
            case 0:
                Collections.sort(songs, new Comparator<SongData>() {
                    @Override
                    public int compare(SongData a, SongData b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                SongAdapter songadt1 = new SongAdapter(this, songs);
                listView.setAdapter(songadt1);
                break;
            case 1:
                Collections.sort(songs, new Comparator<SongData>() {
                    @Override
                    public int compare(SongData a, SongData b) {
                        return a.getArtist().compareTo(b.getArtist());
                    }
                });
                SongAdapter songadt2 = new SongAdapter(this, songs);
                listView.setAdapter(songadt2);
                break;
            case 2:
                Collections.sort(songs, new Comparator<SongData>() {
                    @Override
                    public int compare(SongData a, SongData b) {
                        return a.getAlbum().compareTo(b.getAlbum());
                    }
                });
                SongAdapter songadt3 = new SongAdapter(this, songs);
                listView.setAdapter(songadt3);
                break;
            case 3:
                Collections.sort(songs, new Comparator<SongData>() {
                    @Override
                    public int compare(SongData a, SongData b) {
                        int songStateA;
                        int songStateB;
                        Map<String,?> map = SharedPrefs.getSongData(getApplicationContext(), a.getID());
                        Map<String,?> map1 = SharedPrefs.getSongData(getApplicationContext(), b.getID());

                        if(map.get("State") != null){
                            songStateA = ((Integer) map.get("State")).intValue();
                        }else{
                            songStateA = state.NEUTRAL.ordinal();
                        }

                        if(map1.get("State") != null){
                            songStateB = ((Integer) map1.get("State")).intValue();
                        }else{
                            songStateB = state.NEUTRAL.ordinal();
                        }

                        if (songStateA < songStateB) {
                            return 1;
                        }
                        else if (songStateA > songStateB) {
                            return -1;
                        }
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                SongAdapter songadt4 = new SongAdapter(this, songs);
                listView.setAdapter(songadt4);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //empty because spinner selections will never disappear
    }
}