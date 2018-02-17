package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chadlohrli on 2/11/18.
 */


public class SharedPrefs {

    public static void saveData(Context context, String id, float latitude, float longitude, int day, int time, int rating, int state, int timesPlayed, String lastPlayed) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();

        songadd.putFloat("Latitude", latitude);
        songadd.putFloat("Longitude", longitude);
        songadd.putInt("Day", day);
        songadd.putInt("Time", time);
        songadd.putFloat("Rating", rating);
        songadd.putInt("State",state);
        songadd.putInt("Times played", timesPlayed);
        songadd.putString("Last played", lastPlayed);

        Log.i("latitude", Float.toString(latitude));
        Log.i("longitude", Float.toString(longitude));

        songadd.commit();

    }

    public static void updateFavorite(Context context, String id, int state) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("State", state);

        songadd.commit();
    }

    public static Map<String, ?> getData(Context context, String id) {

        SharedPreferences songsList = context.getSharedPreferences(id, MODE_PRIVATE);
        return songsList.getAll();

    }

}
