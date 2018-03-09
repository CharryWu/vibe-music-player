package com.example.chadlohrli.myapplication;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class DownloadActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private DownloadManager downloadManager;
    private Uri[] uriArray;
    private String[] addressArray;
    private EditText editText;
    private Button downloadButton;

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast toast = Toast.makeText(DownloadActivity.this,
                    "Music Download Complete", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

            File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            Log.v("Files",musicDirectory.exists()+"");
            Log.v("Files",musicDirectory.isDirectory()+"");
            Log.v("Files",musicDirectory.listFiles()+"");
            File[] files = musicDirectory.listFiles();
            int s = files.length;


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        editText = (EditText) findViewById(R.id.download_form);
        downloadButton = (Button) findViewById(R.id.download_button);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_library:
                        Intent searchIntent = new Intent(DownloadActivity.this, MainActivity.class);
                        DownloadActivity.this.startActivity(searchIntent);
                        break;
                }
                return true;
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();
                download(url);
            }
        });

        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);

    }

    public void download(String url) {
        //http://soundbible.com/grab.php?id=2200&type=mp3
        File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        Log.v("Files",musicDirectory.exists()+"");
        Log.v("Files",musicDirectory.isDirectory()+"");
        Log.v("Files",musicDirectory.listFiles()+"");
        File[] files = musicDirectory.listFiles();
        int s = files.length;

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Kyle");

        request.setDescription("Downloading Song");

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "test.mp3");

        request.allowScanningByMediaScanner();
        request.setMimeType("audio/MP3");
        downloadManager.enqueue(request);
    }
}
