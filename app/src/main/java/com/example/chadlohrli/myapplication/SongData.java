package com.example.chadlohrli.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sungeun on 2/9/18.
 */

public class SongData implements Serializable, Parcelable {

    private String id;
    private String song_length;
    private String album_title;
    private String song_title;
    private String song_artist;
    private String song_path;
    private String isDownloaded = "False";
    private String url;
    private int priority;
    //private Bitmap album_image;

    public SongData(String songId, String songLength, String albumTitle, String songTitle, String songArtist, String songPath, String url /*Bitmap albumImage*/) {

        id = songId;
        song_length = songLength;
        album_title = albumTitle;
        song_title = songTitle;
        song_artist = songArtist;
        song_path = songPath;
        this.url = url;
        priority = 0;
        //album_image = albumImage;
    }

    protected SongData(Parcel in) {
        id = in.readString();
        song_length = in.readString();
        album_title = in.readString();
        song_title = in.readString();
        song_artist = in.readString();
        song_path = in.readString();
    }

    public static final Creator<SongData> CREATOR = new Creator<SongData>() {
        @Override
        public SongData createFromParcel(Parcel in) {
            return new SongData(in);
        }

        @Override
        public SongData[] newArray(int size) {
            return new SongData[size];
        }
    };

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

    public String getUrl() {
        return url;
    }

    public String checkIfDownloaded() {return isDownloaded;}

    public void setIfDownloaded(String downloaded) {this.isDownloaded = downloaded;}

    public void setUrl(String url) {this.url = url;}

    //public Bitmap getImage() {return album_image;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(song_length);
        dest.writeString(album_title);
        dest.writeString(song_title);
        dest.writeString(song_artist);
        dest.writeString(song_path);
        //dest.writeString(isDownloaded);
    }

    public void setPriority(int priority){
        priority = priority;
    }

    @Override
    public boolean equals(Object other){
        SongData sec = (SongData) other;
        if(id.equals(sec.getID())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
