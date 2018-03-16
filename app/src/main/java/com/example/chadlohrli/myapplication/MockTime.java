package com.example.chadlohrli.myapplication;

import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sungeun on 3/12/18.
 */

public class MockTime extends Date {

    static String currTime;

    static String date, time;

    public MockTime() {
        date = MainActivity.getDate();
        time = MainActivity.getTime();

       // Log.i("date returned", date);
       // Log.i("time returned", time);
    }


    public static String changeTime() {

        if (date != null && time != null) {
            currTime = (date + "." + time);
        }

        else {
                /*Date test = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

                try {
                    test = sdf.parse(String.valueOf(test));
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
            currTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            //currTime = String.valueOf(test.getTime());
            Log.i("currTime from mockTime", currTime);
        }

        return currTime;
    }

}