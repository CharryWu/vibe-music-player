package com.example.chadlohrli.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private String albumTitle = "";
    private String artistName = "";
    //private Bitmap album_image;
    private ArrayList<SongData> songs;

    public Album(String title, String name /*Bitmap pic*/, ArrayList<SongData> songList) {
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
    public ArrayList<SongData> getSongs() {
        return this.songs;
    }

}
