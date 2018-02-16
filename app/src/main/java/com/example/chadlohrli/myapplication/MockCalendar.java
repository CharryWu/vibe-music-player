package com.example.chadlohrli.myapplication;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

/**
 * Created by marissahing on 2/16/18.
 */

public class MockCalendar extends Calendar {
    long millis;

    public MockCalendar() {
    }
    public MockCalendar(long millis) {
        this.millis = millis;
    }
    public static MockCalendar getInstance() {
        return new MockCalendar();
    }

    @Override
    public int get(int field) {
        if (field == Calendar.HOUR_OF_DAY) {
            return 5;
        }
        if (field == Calendar.DAY_OF_WEEK) {
            return 3;
        }
        return 0;
    }

    @Override
    protected void computeTime() {

    }

    @Override
    protected void computeFields() {

    }

    public long getTimeInMillis() {
        return millis;
    }
    public void setTimeInMillis(long ms) {
        millis = ms;
    }

    @Override
    public void add(int i, int i1) {

    }

    @Override
    public void roll(int i, boolean b) {

    }

    @Override
    public int getMinimum(int i) {
        return 0;
    }

    @Override
    public int getMaximum(int i) {
        return 0;
    }

    @Override
    public int getGreatestMinimum(int i) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int i) {
        return 0;
    }

}

