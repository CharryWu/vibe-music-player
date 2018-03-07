package com.example.chadlohrli.myapplication;

import android.os.Bundle;
import android.app.Fragment;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SongProgressFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song_progress, container, false);
    }


}
