package com.example.chadlohrli.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chadlohrli on 2/9/18.
 */

public class SongParser {

    public static Album createAlbum(String albumPath){

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        int id;
        int song_length;
        String album_title;
        String song_title;
        String song_path;
        Bitmap album_image;
        byte[] art;
        ArrayList<SongData> songs = new ArrayList<SongData>();


        File file = new File(albumPath);
        File[] list = file.listFiles();

        for(File f:list){
            String path = f.getName();
            mmr.setDataSource(path);
            if(path.endsWith(".mp3")){

                song_path = f.getAbsolutePath();
                album_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                song_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                id = (album_title+song_title).hashCode();
                song_length = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                SongData song = new SongData(id,album_title,song_title);
                songs.add(song);

            }

        }

        art = mmr.getEmbeddedPicture();
        Bitmap songImage = BitmapFactory.decodeByteArray(art,0,art.length);

        Album album = new Album(album_title,songs,song)





    }







}
