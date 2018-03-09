package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumActivity extends AppCompatActivity {

    private ArrayList<SongData> madeSongs;
    private ArrayList<Album> albumArray;
    private BottomNavigationView bottomNav;

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

    public ArrayList<Album> buildAlbums(ArrayList<SongData> songs) {
        ArrayList<Album> albumArrayList = new ArrayList<Album>();
        Album obj;

        for (int i = 0; i < songs.size(); i++) {
            SongData songToAdd = songs.get(i);
            boolean matchFound = false;
            if (i == 0) {
                //Log.d("first song: building new album for ", songToAdd.getAlbum());
                ArrayList<SongData> list = new ArrayList<SongData>();
                Album firstElement = new Album(songToAdd.getAlbum(), songToAdd.getArtist(), list);
                firstElement.getSongs().add(songToAdd);
                //Log.d("first song: added song to album", songToAdd.getTitle());
                albumArrayList.add(firstElement);
            } else {
                for (int j = 0; j < albumArrayList.size(); j++) {
                    if (songToAdd.getAlbum().equals(albumArrayList.get(j).getAlbumTitle())) {
                        //Log.d("album exists for ", songToAdd.getAlbum());
                        obj = albumArrayList.get(j);
                        obj.getSongs().add(songToAdd);
                        //Log.d("adding to song list ", songToAdd.getTitle());
                        matchFound = true;
                    }
                }
                if (!matchFound) {
                    //Log.d("building new album for ", songToAdd.getAlbum());
                    ArrayList<SongData> list = new ArrayList<SongData>();
                    Album object = new Album(songToAdd.getAlbum(), songToAdd.getArtist(), list);
                    object.getSongs().add(songToAdd);
                    //Log.d("adding song to album ", songToAdd.getTitle());
                    albumArrayList.add(object);
                }
            }

        }
        Collections.sort(albumArrayList, new Comparator<Album>() {
            @Override
            public int compare(Album a, Album b) {
                return a.getAlbumTitle().compareTo(b.getAlbumTitle());
            }
        });

        return albumArrayList;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        madeSongs = createSongs();
        albumArray = buildAlbums(madeSongs);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        Intent searchIntent = new Intent(AlbumActivity.this, DownloadActivity.class);
                        AlbumActivity.this.startActivity(searchIntent);
                        break;
                    case R.id.my_library:
                        Intent homeIntent = new Intent(AlbumActivity.this, MainActivity.class);
                        AlbumActivity.this.startActivity(homeIntent);
                        break;
                }
                return true;
            }
        });

        GridView gridView = (GridView) findViewById(R.id.gv);
        AlbumAdapter customGrid = new AlbumAdapter(this, R.layout.row_grid, albumArray);
        gridView.setAdapter(customGrid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlbumActivity.this, PickedAlbumActivity.class);
                intent.putExtra("ALBUMS", albumArray);
                intent.putExtra("CUR", Integer.parseInt(view.getTag().toString()));
                AlbumActivity.this.startActivity(intent);
            }
        });

    }

}
