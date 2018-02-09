package com.example.chadlohrli.myapplication;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class gridAdapter extends ArrayAdapter<gridItem> {
    Context context;
    int layoutResourceId;
    ArrayList<gridItem> data = new ArrayList<gridItem>();

    public gridAdapter(Context context, int layoutResourceId,
                             ArrayList<gridItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.albumTitle = (TextView) row.findViewById(R.id.album_title_textview);
            holder.artistName = (TextView) row.findViewById(R.id.artist_name_textview);
            holder.imageItem = (ImageView) row.findViewById(R.id.album_art_imageview);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        gridItem item = data.get(position);
        holder.albumTitle.setText(item.getAlbumTitle());
        holder.artistName.setText(item.getArtistName());
        holder.imageItem.setImageResource(item.getImage());
        return row;

    }

    static class RecordHolder {
        TextView albumTitle;
        TextView artistName;
        ImageView imageItem;

    }
}
