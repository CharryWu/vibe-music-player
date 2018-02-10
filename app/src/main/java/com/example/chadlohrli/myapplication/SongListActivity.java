package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SongListActivity extends AppCompatActivity {
    private ListView listView;
    private Button shuffleButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist);

        listView = (ListView) findViewById(R.id.songlist);
        shuffleButton = (Button) findViewById(R.id.shuffle);
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
                toast.setMargin(50,50);
                toast.show();

                //update song information
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this,);
            }
        });


    }
}
