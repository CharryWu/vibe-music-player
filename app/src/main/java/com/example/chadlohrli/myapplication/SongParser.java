package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

/**
 * Created by chadlohrli on 2/9/18.
 */

public class SongParser {


    public static Bitmap albumCover(SongData song, Context context) {

        if (song.getPath() == null)
            return null;

        Bitmap album_image = null;
        byte[] art;

        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();

            Uri uri = Uri.parse(song.getPath());
            mmr.setDataSource(context, uri);

            art = mmr.getEmbeddedPicture();
            album_image = BitmapFactory.decodeByteArray(art, 0, art.length);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return album_image;

    }

    public static SongData parseSong(String path, String Id, Context context) {


        String id;
        String song_length;
        String album_title = null;
        String song_title;
        String song_path = null;
        String song_artist;
        SongData song = null;


        //for(int i = 0; i < songId.size(); i++){

        //String path = f.getName();
        //String songID = String.valueOf(songId.get(i));
        //String pth = "res.raw." + songID;

        //Resources.getSystem().getIdentifier(songId.get(i), "raw", Resources.getSystem().getResourcePackageName());
        //String sid = songId.get(i);

        //Log.d("name is :", songId.get(i));

        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();

            song_path = path + Id;

            Uri uri = Uri.parse(song_path);
            mmr.setDataSource(context, uri);

            album_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            song_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            song_length = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            song_artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            id = Id;

            Log.d("song title:", song_title);

            song = new SongData(id, song_length, album_title, song_title, song_artist, song_path/*,album_image*/);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return song;
    }

}

