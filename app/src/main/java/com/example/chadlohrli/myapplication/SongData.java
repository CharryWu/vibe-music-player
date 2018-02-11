package com.example.chadlohrli.myapplication;

/**
 * Created by sungeun on 2/9/18.
 */

public class SongData {

    private String id;
    private int song_length;
    private String album_title;
    private String song_title;
    private String song_path;

    public SongData(String songId, int songLength, String albumTitle, String songTitle, String songPath) {

        id = songId;
        song_length = songLength;
        album_title = albumTitle;
        song_title = songTitle;
        song_path = songPath;


    }

    public String getID() {return id;}
    public String getTitle() {return song_title;}
    public String getAlbum() {return album_title;}
}
