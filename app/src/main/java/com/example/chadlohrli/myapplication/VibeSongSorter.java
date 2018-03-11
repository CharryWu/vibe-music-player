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

/**
 * Created by Kartik on 2/16/2018.
 */

public class VibeSongSorter implements Comparator<String> {

    Context curContext;
    public VibeSongSorter(Context context) {
        curContext = context;
    }

    Context context;
    public int compare(String lhs, String rhs) {
        String lid = lhs;
        String rid = rhs;

        SharedPreferences lpref = curContext.getSharedPreferences(lid, Context.MODE_PRIVATE);
        SharedPreferences rpref = curContext.getSharedPreferences(rid, Context.MODE_PRIVATE);

        double lrate = (double) lpref.getFloat("Rating", 0);
        double rrate = (double) rpref.getFloat("Rating", 0);

        int lLoc = lpref.getInt("Loc Played", 0);
        int rLoc = lpref.getInt("Loc Played", 0);

        int lWeek = lpref.getInt("Played this week", 0);
        int rWeek = rpref.getInt("Played this week", 0);

        int lFr = lpref.getInt("Friend Played", 0);
        int rFr = lpref.getInt("Friend Played", 0);

        if (lrate != rrate) {
            return ((rrate > lrate)?1:-1);
        } else if (lLoc != rLoc) {
            return ((rLoc > lLoc)?1:-1);
        }else if (lWeek != rWeek) {
            return ((rWeek > lWeek)?1:-1);
        }else if (lFr != rFr) {
            return ((rFr > lFr)?1:-1);
        }
        return 1;
    }

}
