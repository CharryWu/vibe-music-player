package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
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
        String album_title = null;
        String song_title;
        String song_path;
        Bitmap album_image;
        byte[] art;
        ArrayList<SongData> songs = new ArrayList<SongData>();

        Field[] fields = R.raw.class.getFields();
        for(int count =0;count < fields.length;count++){
            Log.i("Raw Asset:",fields[count].getName());
            try {
                int resID = fields[count].getInt(fields[count]);
                Log.i("Raw Asset ID:",String.valueOf(resID));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        File file = new File("app/src/main");
        File[] list = file.listFiles();

        for(File f:list){
            System.out.print(f);
            String path = f.getName();
            mmr.setDataSource(path);
            if(path.endsWith(".mp3")){

                song_path = f.getAbsolutePath();
                album_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                song_title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                id = (album_title+song_title).hashCode();
                song_length = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                SongData song = new SongData(id,song_length,album_title,song_title,song_path);
                songs.add(song);



            }

        }

        art = mmr.getEmbeddedPicture();
        album_image = BitmapFactory.decodeByteArray(art,0,art.length);

        return new Album(album_title,songs,album_image);

    }

}
