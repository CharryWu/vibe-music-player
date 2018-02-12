package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Button backButton;
    private ArrayList<SongData> songs;

    public ArrayList<SongData> createSongs(){

        Field[] fields = R.raw.class.getFields();
        ArrayList<SongData> songs = new ArrayList<SongData>();

        for(int count=0;count < fields.length; count++){

            Log.i("Raw Asset:",fields[count].getName());

            String path = "android.resource://" + getPackageName()+"/raw/";
            String Id = fields[count].getName();

            SongData song = SongParser.parseSong(path, Id,getApplicationContext());

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

    public void songPicked(View view){
        //mp.setList(songs);
        //mp.setSong(Integer.par seInt(view.getTag().toString()));

        Intent intent = new Intent(SongListActivity.this, MusicPlayer.class);
        intent.putExtra("SONGS",songs);
        intent.putExtra("CUR",Integer.parseInt(view.getTag().toString()));
        SongListActivity.this.startActivity(intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist);

        //sharedPrefs Example
        SharedPrefs.saveData(getApplicationContext(),"5",(float)1.2,(float)1.2,5,2,7,3,1);
        Map<String,?> mmap = SharedPrefs.getData(getApplicationContext(),"5");

        listView = (ListView) findViewById(R.id.songlist);
        backButton = (Button) findViewById(R.id.go_back);

        songs = createSongs();

        SongAdapter songadt = new SongAdapter(this,songs);
        listView.setAdapter(songadt);

        //sample array
//        String[] songTitleArray = new String[] {"Song 1", "Song 2", "Song 3", "Song 4","Song 5", "Song 6", "Song 7", "Song 8",
//        "Song 9", "Song 10", "Song 11", "Song 12", "Song 13", "Song 14", "Song 15", "Song 16", "Song 17"};
//
//        //adapter for listview
//        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songTitleArray);
//        //set adapter for listview
//        //listView.setAdapter(adapter);
//
//        //when item in list is clicked, song should play
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //get String stored in selected item, later pass to hashtable to retrieve Song object
//                String songAndAlbumAndArtist = (String) listView.getItemAtPosition(i);
//
//                //placeholder
//                Toast toast = Toast.makeText(getApplicationContext(), songAndAlbumAndArtist, Toast.LENGTH_SHORT);
//                toast.show();
//
//            }
//        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongListActivity.this, MainActivity.class);
                SongListActivity.this.startActivity(intent);
                finish();
            }
        });


    }
}

