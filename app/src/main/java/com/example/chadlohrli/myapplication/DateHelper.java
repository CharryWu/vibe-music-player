package com.example.chadlohrli.myapplication;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public DateHelper() {}

    public Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public Date getDate() { return Calendar.getInstance().getTime(); }

}
