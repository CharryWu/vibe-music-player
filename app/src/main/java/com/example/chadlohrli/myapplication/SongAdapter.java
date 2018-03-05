package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by chadlohrli on 2/11/18.
 */

public class SongAdapter extends BaseAdapter {

    private ArrayList<SongData> songs;
    private LayoutInflater songInf;
    private Context context;
    private int songState;

    public SongAdapter(Context c, ArrayList<SongData> songs) {
        this.songs = songs;
        songInf = LayoutInflater.from(c);
        context = c;
    }

    public void checkSongState(SongData song){


        Map<String,?> map = SharedPrefs.getSongData(context,song.getID());

        if(map.get("State") != null){
            songState = ((Integer) map.get("State")).intValue();
        }else{
            songState = state.NEUTRAL.ordinal();
        }

    }


    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getSongState() {return this.songState;}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //map to song layout
        LinearLayout songLay = (LinearLayout) songInf.inflate
                (R.layout.song, viewGroup, false);

        //get title and artist views
        TextView songView = (TextView) songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
        Button undislikeBtn = (Button) songLay.findViewById(R.id.undislikeBtn);
        undislikeBtn.setVisibility(View.INVISIBLE);
        //get song using position
        SongData currSong = songs.get(i);
        checkSongState(currSong);
        if(songState == state.DISLIKE.ordinal()){
            undislikeBtn.setVisibility(View.VISIBLE);
            undislikeBtn.setBackgroundColor(Color.RED);
            undislikeBtn.setText("X");
        }
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(i);
        undislikeBtn.setTag(i);
        return songLay;
    }
}
