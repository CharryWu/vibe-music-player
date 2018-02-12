package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumActivity extends AppCompatActivity {

    private ArrayList<SongData> madeSongs;
    private ArrayList<gridItem> gridArray;

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

    public ArrayList<gridItem> buildAlbums(ArrayList<SongData> songs) {
        ArrayList<gridItem> gridItemArrayList = new ArrayList<gridItem>();
        gridItem obj;

        for (int i = 0; i < songs.size(); i++) {
            SongData songToAdd = songs.get(i);
            boolean matchFound = false;
            if (i == 0) {
                ArrayList<SongData> list = new ArrayList<SongData>();
                gridItem firstElement = new gridItem(songToAdd.getAlbum(), songToAdd.getArtist(), list);
                firstElement.getSongs().add(songToAdd);
                gridItemArrayList.add(firstElement);
            }
            else {
                for (int j = 0; j < gridItemArrayList.size(); j++) {
                    if (songToAdd.getAlbum().equals(gridItemArrayList.get(j).getAlbumTitle())) {
                        obj = gridItemArrayList.get(j);
                        obj.getSongs().add(songToAdd);
                        matchFound = true;
                    }
                }
                if (!matchFound){
                    ArrayList<SongData> list = new ArrayList<SongData>();
                    gridItem object = new gridItem(songToAdd.getAlbum(), songToAdd.getArtist(), list);
                    object.getSongs().add(songToAdd);
                    gridItemArrayList.add(object);
                }
            }

        }
        return gridItemArrayList;

    }

  /*  public void albumPicked(View view){
        //mp.setList(songs);
        //mp.setSong(Integer.parseInt(view.getTag().toString()));

        Intent intent = new Intent(AlbumActivity.this, PickedAlbumActivity.class);
        intent.putExtra("ALBUM", gridArray);
        intent.putExtra("CUR",Integer.parseInt(view.getTag().toString()));
        AlbumActivity.this.startActivity(intent);
        finish();

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Button backBtn = (Button) findViewById(R.id.button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumActivity.this, MainActivity.class);
                AlbumActivity.this.startActivity(intent);
            }
        });

        madeSongs = createSongs();
        gridArray = buildAlbums(madeSongs);

        GridView gridView = (GridView) findViewById(R.id.gv);
        gridAdapter customGrid = new gridAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGrid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlbumActivity.this, PickedAlbumActivity.class);
                intent.putExtra("ALBUMS", gridArray);
                intent.putExtra("CUR", view.getTag().toString());
                AlbumActivity.this.startActivity(intent);
                finish();
            }
        });

    }

}
