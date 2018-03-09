package com.example.chadlohrli.myapplication;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class DownloadTestActivity extends AppCompatActivity {
    private DownloadManager downloadManager;
    private Uri[] uriArray;
    private String[] addressArray;

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast toast = Toast.makeText(DownloadTestActivity.this,
                    "Music Download Complete", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_test);
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        download();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);

    }

    public void download() {
        Uri uri = Uri.parse("http://soundbible.com/grab.php?id=2200&type=mp3");

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Kyle");

        request.setDescription("Downloading Song");

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_MUSIC, "test.mp3");
        request.allowScanningByMediaScanner();
        request.setMimeType("audio/MP3");
        downloadManager.enqueue(request);



    }
}
