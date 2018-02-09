package com.example.chadlohrli.myapplication;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by sungeun on 2/9/18.
 */

public class Album {

    private String title;
    private List<String> songs;
    private Bitmap album_image;

    public Album(String albumTitle, List<String> songList, Bitmap image) {

        title = albumTitle;
        songs = songList;
        album_image = image;

    }

    public String getTitle() {return title;}
    public List<String> getAlbum() {return songs;}
    public Bitmap getImage() {return album_image;}
}



