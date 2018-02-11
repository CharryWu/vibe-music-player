package com.example.chadlohrli.myapplication;

import android.content.Intent;
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


public class SongListActivity extends AppCompatActivity {
    private ListView listView;
    private Button shuffleButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist);

        listView = (ListView) findViewById(R.id.songlist);
        backButton = (Button) findViewById(R.id.go_back);

        //sample array
        String[] songTitleArray = new String[] {"Song 1", "Song 2", "Song 3", "Song 4","Song 5", "Song 6", "Song 7", "Song 8",
        "Song 9", "Song 10", "Song 11", "Song 12", "Song 13", "Song 14", "Song 15", "Song 16", "Song 17"};

        //adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songTitleArray);
        //set adapter for listview
        listView.setAdapter(adapter);

        //when item in list is clicked, song should play
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get String stored in selected item, later pass to hashtable to retrieve Song object
                String songAndAlbumAndArtist = (String) listView.getItemAtPosition(i);

                //placeholder
                Toast toast = Toast.makeText(getApplicationContext(), songAndAlbumAndArtist, Toast.LENGTH_SHORT);
                toast.show();

                //update song information
            }
        });

        //get id
        Field[] fields = R.raw.class.getFields();
        ArrayList<SongData> songs = new ArrayList<SongData>();

        for(int count =0;count < fields.length; count++){

            Log.i("Raw Asset:",fields[count].getName());

            try {

                int resID = fields[count].getInt(fields[count]);


                String path = "android.resource://" + getPackageName()+"/raw/";
                String Id = fields[count].getName();


                SongData album = SongParser.createAlbum(path, Id,getApplicationContext());

                Log.i("Raw Asset ID:",String.valueOf(resID));


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

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

