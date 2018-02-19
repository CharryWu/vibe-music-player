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

class SongSorter implements Comparator<SongData> {

    Context curContext;
    SongSorter(Context context) {
        curContext = context;
    }

    Context context;
    public int compare(SongData lhs, SongData rhs) {
        String lid = lhs.getID();
        String rid = rhs.getID();

        SharedPreferences lpref = curContext.getSharedPreferences(lid, Context.MODE_PRIVATE);
        SharedPreferences rpref = curContext.getSharedPreferences(rid, Context.MODE_PRIVATE);

        double lrate = (double) lpref.getFloat("Rating", 0);
        double rrate = (double) rpref.getFloat("Rating", 0);

        String ltime = lpref.getString("Last played", "");
        String rtime = lpref.getString("Last played", "");

        int lfav = lpref.getInt("fav", 0);
        int rfav = rpref.getInt("fav", 0);

        /*Map<String,?> lpref = SharedPrefs.getData(curContext,lid);
        Map<String,?> rpref = SharedPrefs.getData(curContext,rid);

        double lrate = (Double) lpref.get("Rating");
        double rrate = (Double) rpref.get("Rating");

        String ltime = lpref.get("Rating").toString();
        String rtime = lpref.get("Rating").toString();

        int lfav = (Integer) lpref.get("fav");
        int rfav = ((Integer) rpref.get("fav"));
        */

        if (lrate != rrate) {
            return ((rrate > lrate)?1:-1);
        } else if (lfav != rfav) {
            return ((rfav > lfav)?1:-1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Boolean check = false;
        try {
            check = sdf.parse(rtime).before(sdf.parse(ltime));
        }catch (ParseException e){
            e.printStackTrace();
        }

        if(check){
            return -1;
        }
        return 1;
    }

}
