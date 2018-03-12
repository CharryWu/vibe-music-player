package com.example.chadlohrli.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongProgressFragment extends Fragment {

    private ListView toFill;
    private ArrayList<SongData> songs;
    private SongAdapter songadt;
    private Button carrot;
    private boolean viewUp = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_song_progress, container, false);

        String song = this.getArguments().getString("SONG");
        String artist_album = this.getArguments().getString("ARTIST_ALBUM");

        songs = this.getArguments().getParcelableArrayList("SONGS");

        toFill = getActivity().findViewById(R.id.listView);


        TextView songTitle = layout.findViewById(R.id.song_title);
        TextView artistAlbum = layout.findViewById(R.id.artist_name);

        toFill.setVisibility(View.GONE);
        songadt = new SongAdapter(getActivity(), songs);
        toFill.setAdapter(songadt);

        carrot = layout.findViewById(R.id.carrot);

        carrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewUp) {
                    slideDown();
                    carrot.setText("\u2303");
                }
                else {
                    slideUp();
                    carrot.setText("\u2304");
                }
                viewUp = !viewUp;
            }
        });

        songTitle.setText(song);
        artistAlbum.setText(artist_album);
        return layout;
    }

    public void slideDown() {
        toFill.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                toFill.getHeight()); // toYDelta
        animate.setDuration(500);
        toFill.startAnimation(animate);
    }

    public void slideUp() {
        toFill.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                toFill.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        toFill.startAnimation(animate);
    }

}
