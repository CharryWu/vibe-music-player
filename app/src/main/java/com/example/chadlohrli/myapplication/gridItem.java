package com.example.chadlohrli.myapplication;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class gridItem implements Serializable {
    private String albumTitle = "";
    private String artistName = "";
    //private Bitmap album_image;
    private ArrayList<SongData> songs;

    public gridItem(String title, String name /*Bitmap pic*/, ArrayList<SongData> songList){
        albumTitle = title;
        artistName = name;
        //album_image = pic;
        songs = songList;
    }

    public String getAlbumTitle() {
        return this.albumTitle;
    }
    public String getArtistName() {
        return this.artistName;
    }
    //public Bitmap getImage() { return this.album_image; }
    public ArrayList<SongData> getSongs() { return this.songs;}

}
