package com.example.chadlohrli.myapplication;

import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sungeun on 3/12/18.
 */

public class MockTime extends Date {

    Date currTime;

    public MockTime() {}


    public Date test() {

        //Intent input =

        //user set specific time
        if(input) {
            currTime = input;
            return currTime;
        }

        else {
            return Calendar.getInstance().getTime();
        }
    }

}
