package com.example.chadlohrli.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Previous screen: {@link AlbumActivity}
 * Next screen: {@link MusicPlayer}
 * Displays details of a selected album = album cover + album song list
 */
public class PickedAlbumActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView albumCover;
    private TextView albumName;
    private TextView artistName;
    private FrameLayout layout;

    private ArrayList<Album> albums;
    private Album cur_album;
    private ArrayList<SongData> songs;
    private Button undislikeBtn;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // UI setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picked_album);
        listView = (ListView) findViewById(R.id.songlist_activity_picked_album);
        albumCover = (ImageView) findViewById(R.id.album_art_picked_album);
        albumName = (TextView) findViewById(R.id.album_name_textview_picked_album);
        artistName = (TextView) findViewById(R.id.artist_name_textview_picked_album);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        Intent searchIntent = new Intent(PickedAlbumActivity.this, DownloadActivity.class);
                        PickedAlbumActivity.this.startActivity(searchIntent);
                        break;
                    case R.id.my_library:
                        Intent homeIntent = new Intent(PickedAlbumActivity.this, MainActivity.class);
                        PickedAlbumActivity.this.startActivity(homeIntent);
                        break;
                }
                return true;
            }
        });

        //grab data from intent
        albums = (ArrayList<Album>) getIntent().getSerializableExtra("ALBUMS");
        cur_album = albums.get(getIntent().getIntExtra("CUR", 0));
        songs = cur_album.getSongs();

        //display song for now to ensure data has correctly been passed
        Toast toast = Toast.makeText(getApplicationContext(), cur_album.getAlbumTitle(), Toast.LENGTH_SHORT);
        toast.show();

        Bitmap bp = SongParser.albumCover(songs.get(0), getApplicationContext());
        albumCover.setImageBitmap(SongParser.albumCover(songs.get(0), getApplicationContext()));
        albumName.setText(cur_album.getAlbumTitle());
        artistName.setText(cur_album.getArtistName());



    }

    @Override
    protected void onStart() {
        super.onStart();


        listView.setAdapter(new SongAdapter(this, songs));


    }

    public void dislikeAction(View view){

        Log.d("TAG",view.getTag().toString());
        SongData song = songs.get(Integer.parseInt(view.getTag().toString()));
        SharedPrefs.updateFavorite(getApplicationContext(),song.getID(),state.NEUTRAL.ordinal());
        undislikeBtn = view.findViewById(R.id.undislikeBtn);
        undislikeBtn.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(getApplicationContext(), "UnDisliked!", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void songPicked(View view) {
        Intent intent = new Intent(this, MusicPlayer.class);
        intent.putExtra("SONGS", songs);
        intent.putExtra("CUR", Integer.parseInt(view.getTag().toString()));
        intent.putExtra("caller", "PickedAlbumActivity");

        //if a disliked song is picked, it is no longer disliked
        SongData song = songs.get(Integer.parseInt(view.getTag().toString()));
        Map<String,?> map = SharedPrefs.getSongData(this.getApplicationContext(),song.getID());
        if(map.get("State") != null){
            if( ((Integer)map.get("State")).intValue() == state.DISLIKE.ordinal() ){
                SharedPrefs.updateFavorite(getApplicationContext(),song.getID(),state.NEUTRAL.ordinal());
            }
        }


        this.startActivity(intent);
    }
}
