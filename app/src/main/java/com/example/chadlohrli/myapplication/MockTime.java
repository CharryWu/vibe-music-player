package com.example.chadlohrli.myapplication;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sungeun on 3/12/18.
 */

public class MockTime extends Date {

    String currTime;

    String date = MainActivity.getDate();
    String time = MainActivity.getTime();

    public MockTime() {}


    public String test() {

        if (date != null && time != null) {
            currTime = (date + "." + time);
        }

        else {
            Date test = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

            try {
                test = sdf.parse(String.valueOf(test));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currTime = String.valueOf(test.getTime());
        }

        return currTime;
    }

}
