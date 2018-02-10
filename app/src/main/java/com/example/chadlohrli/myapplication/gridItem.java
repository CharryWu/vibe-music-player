package com.example.chadlohrli.myapplication;

public class gridItem {
    private String albumTitle = "";
    private String artistName = "";
    private Integer image;

    public gridItem(String title, String name, Integer pic){
        albumTitle = title;
        artistName = name;
        image = pic;
    }

    public String getAlbumTitle() {
        return this.albumTitle;
    }
    public String getArtistName() {
        return this.artistName;
    }
    public Integer getImage() {
        return this.image;
    }

}
