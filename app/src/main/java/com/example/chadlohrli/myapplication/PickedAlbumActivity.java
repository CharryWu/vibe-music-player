package com.example.chadlohrli.myapplication;

<<<<<<< Updated upstream
=======
import android.content.Intent;
import android.content.SharedPreferences;
>>>>>>> Stashed changes
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
<<<<<<< Updated upstream
=======
import android.widget.Button;
import android.widget.ImageView;
>>>>>>> Stashed changes
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Previous screen: {@link AlbumActivity}
 * Next screen: {@link MusicPlayer}
 * Displays details of a selected album = album cover + album song list
 */
public class PickedAlbumActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView albumCover;

    private ArrayList<gridItem> albums;
    private gridItem cur_album;
    private ArrayList<SongData> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // UI setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picked_album);
<<<<<<< Updated upstream

        listView = (ListView) findViewById(R.id.songlist);
=======
        listView = (ListView) findViewById(R.id.songlist_activity_picked_album);
        albumCover = (ImageView) findViewById(R.id.album_art_picked_album);
>>>>>>> Stashed changes

        //grab data from intent
        albums = (ArrayList<gridItem>) getIntent().getSerializableExtra("ALBUMS");
        cur_album = albums.get(getIntent().getIntExtra("CUR",0));
        songs = cur_album.getSongs();

        //display song for now to ensure data has correctly been passed
        Toast toast = Toast.makeText(getApplicationContext(), cur_album.getAlbumTitle(), Toast.LENGTH_SHORT);
        toast.show();

<<<<<<< Updated upstream
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

=======
        albumCover.setImageBitmap(SongParser.albumCover(songs.get(0),getApplicationContext()));

        listView.setAdapter(new SongAdapter(this,songs));
        Button backButton = (Button) findViewById(R.id.back_button_picked_album);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickedAlbumActivity.this, AlbumActivity.class);
                PickedAlbumActivity.this.startActivity(intent);
                finish();
            }
        });

>>>>>>> Stashed changes
    }

    public void songPicked(View view){
        //mp.setList(songs);
        //mp.setSong(Integer.par seInt(view.getTag().toString()));

        Intent intent = new Intent(this, MusicPlayer.class);
        intent.putExtra("SONGS",songs);
        intent.putExtra("CUR",Integer.parseInt(view.getTag().toString()));
        this.startActivity(intent);
        finish();

    }
}
