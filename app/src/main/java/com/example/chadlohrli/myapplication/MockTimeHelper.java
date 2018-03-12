package com.example.chadlohrli.myapplication;

import java.util.Date;

/**
 * Created by sungeun on 3/12/18.
 */

public class MockTimeHelper extends DateHelper {

    public MockTimeHelper() {}

    @Override
    public Date getDate() {
        return MockTime.getInstance();
    }
}
