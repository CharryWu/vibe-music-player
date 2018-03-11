package com.example.chadlohrli.myapplication;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class DownloadService extends Service {
    private ArrayList<SongData> songList;
    private DownloadManager downloadManager;
    public DownloadService() {
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setSongList(ArrayList<SongData> songList) {
        this.songList = songList;
    }

    public void downloadVibeSongsPlaylist(ArrayList<SongData> songList) {
        Iterator<SongData> it = songList.iterator();

        //position of song in arraylist
        int position = 0;
        while (it.hasNext()) {
            SongData song = it.next();
            String songId = song.getID();

            Map<String, ?> map = SharedPrefs.getSongData(getApplicationContext(), songId);
            //if map size is 0, then song has not yet been downloaded
            if(map.size() == 0)
                downloadSong(song, position);
            position++;

        }
    }

    public void downloadSong(SongData song, int position) {
        Uri uri = Uri.parse("REPLACE WITH REAL URL");
        //Uri uri = Uri.parse(song.getUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        String songId = song.getID();

        request.setTitle(songId);
        request.setDescription(Integer.toString(position));

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, songId+".mp3");


        request.allowScanningByMediaScanner();
        request.setMimeType("audio/MP3");

        downloadManager.enqueue(request);


        //TODO update shared prefs
    }


}
