package com.example.chadlohrli.myapplication;

/**
 * Created by sungeun on 2/9/18.
 */

public class SongData {

    private int id;
    private String album_title;
    private String song_title;

    public SongData(int songId, String albumTitle, String songTitle) {

        id = songId;
        album_title = albumTitle;
        song_title = songTitle;

    }

    public long getID() {return id;}
    public String getTitle() {return song_title;}
    public String getAlbum() {return album_title;}
}
