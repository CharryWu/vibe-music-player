package com.example.chadlohrli.myapplication;

import com.example.chadlohrli.myapplication.DateHelper;
import com.example.chadlohrli.myapplication.MockCalendar;

import java.util.Calendar;

public class MockDateHelper extends DateHelper {

    public MockDateHelper() {}

    @Override
    public Calendar getCalendar() {
        return MockCalendar.getInstance();
    }

}
