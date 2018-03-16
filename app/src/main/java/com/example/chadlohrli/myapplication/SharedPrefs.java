package com.example.chadlohrli.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.util.Log;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chadlohrli on 2/11/18.
 */


public class SharedPrefs {
    public static void saveServerCode(Context context, String code){
        SharedPreferences.Editor editor = context.getSharedPreferences(
                context.getString(R.string.sharedpref_servercode_key),MODE_PRIVATE).edit();
        editor.putString("code",code);
        editor.commit();
    }

    public static String getServerCode(Context context){
        return context
                .getSharedPreferences(context.getString(R.string.sharedpref_servercode_key),MODE_PRIVATE)
                .getString("code", "");
    }

    public static void saveSongData(Context context, String id, float latitude, float longitude, int day, int time, int rating, int state, int timesPlayed, String lastPlayed, int fav) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();


        songadd.putFloat("Latitude", latitude);
        songadd.putFloat("Longitude", longitude);
        songadd.putInt("Day", day);
        songadd.putInt("Time", time);
        songadd.putInt("Rating", rating);
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

    public static void updateRating(Context context, String id, int rating) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Rating", rating);

        Log.d("Rating Updated for song " + id, Integer.toString(rating));
        songadd.commit();
    }

    public static void updatePriority(Context context, String id, int p) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Priority", p);

        Log.d("A Priority Updated", "Yes");
        songadd.commit();
    }

    public static void updateFriendPlayed(Context context, String id, int played) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Friend Played", played);

        Log.d("A Friend played Updated", "Yes");
        songadd.commit();
    }

    public static void updateLocPlay(Context context, String id, int played) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Loc Played", played);

        Log.d("Loc played Updated", "Yes");
        songadd.commit();
    }

    public static void updateLastPlayedWeek(Context context, String id, int played) {

        SharedPreferences newSong = context.getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putInt("Played this week", played);

        Log.d("A Week played Updated", "yes");
        songadd.commit();
    }

    public static void updateURL(Context context, String id, String url){

        SharedPreferences newSong = context.getSharedPreferences(id,MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putString("URL",url);
        songadd.putBoolean("downloaded",true);

        Log.d("A URL Updated",url);
        songadd.commit();
    }

    public static void updateDownloaded(Context context, String id){

        SharedPreferences newSong = context.getSharedPreferences(id,MODE_PRIVATE);
        SharedPreferences.Editor songadd = newSong.edit();
        songadd.putBoolean("downloaded",true);

        Log.d("A Song Downloaded","done");
        songadd.commit();
    }

    public static Map<String, ?> getSongData(Context context, String id) {

        SharedPreferences songsList = context.getSharedPreferences(id, MODE_PRIVATE);
        return songsList.getAll();
    }

}
