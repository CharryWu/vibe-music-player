package com.example.chadlohrli.myapplication;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private DownloadManager downloadManager;
    private Uri[] uriArray;
    private String[] addressArray;
    private EditText editText;
    private Button downloadButton;
    private Button downloadAlbumButton;
    private String id;


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast toast = Toast.makeText(DownloadActivity.this,
                    "Music Download Complete", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //directory that song has been stored in
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();


            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(referenceId);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);

            if (cursor.moveToFirst()) ;
            {
                //get description of download which is id if song was zip
                String id = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));


                //get title of column which is "zip" if file was zip file
                String typeOfDownload = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                if (typeOfDownload == "zip") {
                    try {
                        unzip(id);
                    }
                    catch (Exception e) {
                        Log.e("zip doesnt work", "zip");
                    }

                }
                //Log.d("songId", songId);


            }


            //renameSong();


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        editText = (EditText) findViewById(R.id.download_form);
        downloadButton = (Button) findViewById(R.id.download_button);
        downloadAlbumButton = (Button) findViewById(R.id.download_album_button);

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

        downloadAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();
                downloadAlbum(url);
            }
        });

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);

    }

    public void download(String url) {

        //http://soundbible.com/grab.php?id=2200&type=mp3
        //http://soundbible.com/grab.php?id=2190&type=mp3
        File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        Log.v("Files", musicDirectory.exists() + "");
        Log.v("Files", musicDirectory.isDirectory() + "");
        Log.v("Files", musicDirectory.listFiles() + "");
        File[] files = musicDirectory.listFiles();
        int s = files.length;


        //url = "https://firebasestorage.googleapis.com/v0/b/cse-110-team-project-team-5.appspot.com/o/song%2Fafter_the_storm?alt=media&token=9a6ac02b-0192-487a-85a1-4cf40b30e3c4";
        Uri uri = Uri.parse(url);
        id = String.valueOf(url.hashCode());

        try {

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(id);

            request.setDescription("Downloading Song");

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, id);


            request.allowScanningByMediaScanner();
            request.setMimeType("audio/MP3");

            downloadManager.enqueue(request);


            //save url for later pushing to firebase
            SharedPrefs.updateURL(this, id, uri.toString());


        } catch (IllegalArgumentException e) {

            Toast toast = Toast.makeText(DownloadActivity.this,
                    "Invalid URL", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

    }

    public void downloadAlbum(String url) {
        Uri uri = Uri.parse(url);
        id = String.valueOf(url.hashCode());

        try {

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("zip");

            request.setDescription(id);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, id);


            request.setMimeType("df.zip");

            downloadManager.enqueue(request);


            //save url for later pushing to firebase
            //SharedPrefs.updateURL(this, id, uri.toString());


        } catch (IllegalArgumentException e) {

            Toast toast = Toast.makeText(DownloadActivity.this,
                    "Invalid URL", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

    }


    public void renameSong() {

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();

        SongData song = SongParser.parseSong(path, id, this);

        Log.i("PREV SONG ID", song.getID());


        String newId = (song.getAlbum() + song.getTitle()
                + song.getArtist() + song.getLength());


        newId = String.valueOf(newId.hashCode());

        Log.i("NEW SONG ID", newId);

        File of = new File(song.getPath());


        //This Saves to internal storage
        File nf = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC
        ).getAbsolutePath() + "/" + newId + ".mp3");

        nf.getAbsolutePath();

        boolean f = of.renameTo(nf);


    }

    public void unzip(String zipId) throws IOException {
        String downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String zipFilePath = downloadDirectoryPath+zipId;

        String songDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();

        try {
            File f = new File(songDirectory);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = songDirectory + ze.getName();

                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    }
                    else {
                        FileOutputStream fout = new FileOutputStream(path, false);
                        try {
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e("Unzipping problem", "Unzip exception", e);
        }
    }

}
