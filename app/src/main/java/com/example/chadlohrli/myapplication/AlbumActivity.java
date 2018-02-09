package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

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

        String[] albumNames = {"album1", "album2", "album3"};
        String[] artistNames = {"artist1", "artist2", "artist3"};
        Integer[] ImageArray = {R.drawable.album_art, R.drawable.album_art, R.drawable.album_art};

        ArrayList<gridItem> gridArray = new ArrayList<gridItem>();

        for (int i = 0; i < albumNames.length; i++) {
            gridItem object = new gridItem(albumNames[i], artistNames[i], ImageArray[i]);
            gridArray.add(object);
        }

        GridView gridView = (GridView) findViewById(R.id.gv);
        gridAdapter customGrid = new gridAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGrid);
    }
}
