package com.example.chadlohrli.myapplication;

import java.io.Serializable;

/**
 * Created by sungeun on 2/9/18.
 */

public class SongData implements Serializable {

    private String id;
    private String song_length;
    private String album_title;
    private String song_title;
    private String song_artist;
    private String song_path;
    //private Bitmap album_image;

    public SongData(String songId, String songLength, String albumTitle, String songTitle, String songArtist, String songPath /*Bitmap albumImage*/) {

        id = songId;
        song_length = songLength;
        album_title = albumTitle;
        song_title = songTitle;
        song_artist = songArtist;
        song_path = songPath;
        //album_image = albumImage;
    }

    public String getID() {
        return id;
    }

    public String getLength() {
        return song_length;
    }

    public String getTitle() {
        return song_title;
    }

    public String getAlbum() {
        return album_title;
    }

    public String getArtist() {
        return song_artist;
    }

    public String getPath() {
        return song_path;
    }
    //public Bitmap getImage() {return album_image;}

}
