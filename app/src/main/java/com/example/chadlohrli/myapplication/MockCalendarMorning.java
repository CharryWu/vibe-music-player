package com.example.chadlohrli.myapplication;

import java.util.Calendar;

/**
 * Created by Kyle on 2/17/2018.
 */

public class MockCalendarMorning extends Calendar {
    long millis;
    int day;
    int hour;

    public MockCalendarMorning() {
    }
    public MockCalendarMorning(int day, int hour) {
        this.day = day;
        this.hour = hour;

    }
    public static MockCalendarMorning getInstance() {
        return new MockCalendarMorning();
    }

    @Override
    public int get(int field) {
        if (field == Calendar.HOUR_OF_DAY) {
            return 6;
        }
        if (field == Calendar.DAY_OF_WEEK) {
            return Calendar.SATURDAY;
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
