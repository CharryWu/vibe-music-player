package com.example.chadlohrli.myapplication;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sungeun on 3/12/18.
 */

public class MockTime extends Date {

    Date currTime;

    public MockTime() {}

    public static MockTime getInstance() { return new MockTime();}

    @Override
    public Date getTime() {

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
