package com.example.chadlohrli.myapplication;

import java.util.Calendar;

/**
 * Created by Kyle on 2/17/2018.
 */

public class MockDateHelperAfternoon extends DateHelper {
    public MockDateHelperAfternoon() {}

    @Override
    public Calendar getCalendar() {
        return MockCalendarAfternoon.getInstance();
    }
}
