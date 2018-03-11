package com.example.chadlohrli.myapplication;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Kyle on 3/2/2018.
 */

public class SongDownloader {
    //array to store URI's of download addresses after parsing from string to URI
    private Uri[] uriArray;
    private DownloadManager downloadManager;
    private Context context;

    public SongDownloader(String[] addressArray, Context context) {
        this.context = context;
        //create uriArray
        uriArray = new Uri[addressArray.length];
       //iterate through addressArray and parse strings into URIs. Then store in uriArray
       for(int i = 0; i < uriArray.length; i++) {
           Uri currentSongUri = Uri.parse(addressArray[i]);
           uriArray[i] = currentSongUri;
       }
    }

    public void DownloadData(Uri[] uriArray) {
        downloadManager  = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        for(int i = 0; i < uriArray.length; i++) {
            Uri uri = uriArray[i];
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Song Download");

            request.setDescription("Downloading Song");



            //set destination of downloaded file
            //TODO check if uri.toString is ok
            request.setDestinationInExternalFilesDir(context, "android.resource://test/raw/", uri.toString());

            downloadManager.enqueue(request);
        }

    }





}
