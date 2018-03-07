package com.example.chadlohrli.myapplication;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumAdapter extends ArrayAdapter<Album> {
    Context context;
    int layoutResourceId;
    private LayoutInflater albumInf;
    ArrayList<Album> data = new ArrayList<Album>();

    public AlbumAdapter(Context context, int layoutResourceId,
                        ArrayList<Album> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TextView albumTitle;
        TextView albumArtist;
        ImageView albumArt;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        albumTitle = (TextView) row.findViewById(R.id.album_title_textview);
        albumArtist = (TextView) row.findViewById(R.id.artist_name_textview);
        albumArt = (ImageView) row.findViewById(R.id.album_art_imageview);

        Album item = data.get(position);
        ArrayList<SongData> songs = item.getSongs();
        Bitmap bp = SongParser.albumCover(songs.get(0), this.context);
        albumTitle.setText(item.getAlbumTitle());
        albumArtist.setText(item.getArtistName());
        albumArt.setImageBitmap(bp);
        row.setTag(position);
        return row;

    }

}
