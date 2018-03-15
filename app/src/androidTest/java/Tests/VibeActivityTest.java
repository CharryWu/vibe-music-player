package Tests;

import android.app.Activity;
import android.location.Location;
import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.MainActivity;
import com.example.chadlohrli.myapplication.MockTime;
import com.example.chadlohrli.myapplication.VibeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sungeun on 3/11/18.
 */

public class VibeActivityTest {
    VibeActivity activity;
    MainActivity main;

    MockTime mockTime = new MockTime();
    Date date;

    @Rule
    public ActivityTestRule<VibeActivity> vibeActivity = new ActivityTestRule<VibeActivity>(VibeActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup() {
        main = mainActivity.getActivity();
        activity = vibeActivity.getActivity();
    };
    //Location curr_loc = activity.getLoc();

    @Test
    public void testMatchWeek() {
/*
        String testDate = mockTime.test();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        try {
            date = sdf.parse(testDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int result = vibeActivity.getActivity().matchWeek(String.valueOf(date.getTime()));
        assertEquals(2, result, .01);
*/
        int result = vibeActivity.getActivity().matchWeek("2018.01.10.16.02.49");
        assertEquals(0, result, .01);
    }

    //match location
   /* @Test
    public void testMatchLocation() {
        double result = vibeActivity.getActivity().matchLocation(curr_loc);
        assertEquals(2, result, .01);
    }
    */
}
