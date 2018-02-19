package com.example.chadlohrli.myapplication;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private ArrayList<SongData> songs;
    private int cur_song = 0;
    private int cur_song_id;
    private final IBinder musicBinder = new MusicBinder();

    private SeekBar sb;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        //possibly return false
        return super.onUnbind(intent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Intent RTReturn = new Intent(MusicPlayer.SONG_FINISHED);
        RTReturn.putExtra("hi", "song finished");
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);


    }

    public void setSongList(ArrayList<SongData> songList) {
        this.songs = songList;
    }



    public void setCurrentSong(int cur_song) {
        this.cur_song = cur_song;
        Resources res = this.getResources();
        cur_song_id = res.getIdentifier(songs.get(cur_song).getID(), "raw", this.getPackageName());

        Log.d("raw", Integer.toString(R.raw.gottagetoveryou));
        Log.d("soundId", Integer.toString(cur_song_id));

    }


    public void playSong() {
        mediaPlayer.reset();

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(cur_song_id);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepare();

        }
        catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    public MediaPlayer getPlayer() {
        return  mediaPlayer;
    }

    public void pauseSong() {
        mediaPlayer.pause();
    }

    public void resumeSong() {
        mediaPlayer.start();
    }


    public class MusicBinder extends Binder {
       public MusicService getService() {
            return MusicService.this;
        }
    }


}
