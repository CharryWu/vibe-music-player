package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by chadlohrli on 2/9/18.
 */

public class SongParser {

    public static SongData parseSong (String path, String Id, Context context){


        String id;
        int song_length;
        String album_title = null;
        String song_title;
        String song_path = null;
        Bitmap album_image;
        byte[] art;
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

                Uri uri = Uri.parse(path+Id);
                mmr.setDataSource(context,uri);
                Log.d("Parse: ", "here");

                //song_path = f.getAbsolutePath();

                album_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                song_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                Log.d("song title:", song_title);

                //id = (album_title+song_title).hashCode();
                id = Id;
                song_length = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                song = new SongData(id,song_length,album_title,song_title,song_path);
                art = mmr.getEmbeddedPicture();
                album_image = BitmapFactory.decodeByteArray(art,0,art.length);

                //songs.add(song);

            } catch (Exception e) {
                e.printStackTrace();
            }
                //if(path.endsWith(".mp3")){
            //}

        //}
        return song;
    }

}

