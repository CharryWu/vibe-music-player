package com.example.chadlohrli.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Map;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

/**
 * Created by Kartik on 2/16/2018.
 */

public class VibeSongSorter implements Comparator<SongData> {

    Context curContext;
    public VibeSongSorter(Context context) {
        curContext = context;
    }

    // takes in urls and sorts songs based on vibe
    Context context;
    public int compare(SongData lhs, SongData rhs) {
        //String lid = lhs;
        //String rid = rhs;

        //String lid = String.valueOf(lhs.hashCode());
        //String rid = String.valueOf(rhs.hashCode());

        String lid = lhs.getID();
        String rid = rhs.getID();

        SharedPreferences lpref = curContext.getSharedPreferences(lid, Context.MODE_PRIVATE);
        SharedPreferences rpref = curContext.getSharedPreferences(rid, Context.MODE_PRIVATE);

        int lrate = lpref.getInt("Rating", 0);
        int rrate = rpref.getInt("Rating", 0);

        Log.i("Left song rating", Integer.toString(lrate) );
        Log.i("Right song rating", Integer.toString(rrate) );

        int lLoc = lpref.getInt("Loc Played", 0);
        int rLoc = lpref.getInt("Loc Played", 0);

        int lWeek = lpref.getInt("Played this week", 0);
        int rWeek = rpref.getInt("Played this week", 0);

        int lFr = lpref.getInt("Friend Played", 0);
        int rFr = lpref.getInt("Friend Played", 0);

        if (lrate != rrate) {
            Log.i("Rating", "1" );
            return ((rrate > lrate)?1:-1);
        } else if (lLoc != rLoc) {
            Log.i("Loc", "2" );
            return ((rLoc > lLoc)?1:-1);
        }else if (lWeek != rWeek) {
            Log.i("Week", "3" );
            return ((rWeek > lWeek)?1:-1);
        }else if (lFr != rFr) {
            Log.i("Friend", "4" );
            return ((rFr > lFr)?1:-1);
        }
        Log.i("Default", "5" );
        return 1;
    }

}
