package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chadlohrli on 2/11/18.
 */


public class SharedPrefs {

    public static void saveSongData(Context context, String id, float latitude, float longitude, int day, int time, float rating, int state, int timesPlayed, String lastPlayed, int fav) {

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
        //need this for song sorter
        songadd.putInt("fav", fav);
        //Log.i("latitude", Float.toString(latitude));
        //Log.i("longitude", Float.toString(longitude));
        //
        // Log.i("Rating", Float.toString(rating));

        songadd.commit();

    }

    public static void saveDefault(Context context, String id) {
        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        //if you need defaults
        songadd.commit();

    }

    public static void updateFavorite(Context context, String id, int songState) {
        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("State", songState);

        if(songState == state.NEUTRAL.ordinal()){
            songadd.putInt("fav", 0);
        }else if(songState == state.DISLIKE.ordinal()){
            songadd.putInt("fav", -1);
        }else if(songState == state.FAVORITE.ordinal()){
            songadd.putInt("fav", 1);
        }
        songadd.commit();
    }

    public static void updateRating(Context context, String id, float rating) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putFloat("Rating", rating);

        Log.i("Rating Updated", Float.toString(rating));
        songadd.commit();
    }

    public static void updatePriority(Context context, String id, int p) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Priority", p);

        Log.i("Priority Updated", Integer.toString(p));
        songadd.commit();
    }

    public static void updateFriendPlayed(Context context, String id, int played) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Friend Played", played);

        Log.i("Friend played Updated", Integer.toString(played));
        songadd.commit();
    }

    public static void updateLocPlay(Context context, String id, int played) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Loc Played", played);

        Log.i("Loc played Updated", Integer.toString(played));
        songadd.commit();
    }

    public static void updateLastPlayedWeek(Context context, String id, int played) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Played this week", played);

        Log.i("Week played Updated", Integer.toString(played));
        songadd.commit();
    }

    public static void updateURL(Context context, String id, String url){

        SharedPreferences newSong = context.getSharedPreferences(id,MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putString("URL",url);
        songadd.putBoolean("downloaded",true);

        Log.i("URL Updated",url);
        songadd.commit();
    }

    public static Map<String, ?> getSongData(Context context, String id) {

        SharedPreferences songsList = context.getSharedPreferences(id, MODE_PRIVATE);
        return songsList.getAll();
    }

}
