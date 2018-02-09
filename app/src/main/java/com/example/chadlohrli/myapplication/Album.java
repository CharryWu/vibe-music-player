package com.example.chadlohrli.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungeun on 2/9/18.
 */

public class Album {

    private String title;
    private ArrayList<SongData> songs;
    private Bitmap album_image;

    public Album(String albumTitle, List<SongData> songList, Bitmap image) {

        title = albumTitle;
        songs = songList;
        album_image = image;

    }

    public String getTitle() {return title;}
    public List<SongData> getAlbum() {return songs;}
    public Bitmap getImage() {return album_image;}
}



