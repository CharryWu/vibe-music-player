package com.example.chadlohrli.myapplication;

import java.util.Calendar;

/**
 * Created by Kyle on 2/17/2018.
 */

public class MockDateHelperMorning extends DateHelper {
    public MockDateHelperMorning() {}

    @Override
    public Calendar getCalendar() {
        return MockCalendarMorning.getInstance();
    }
}
