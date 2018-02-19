package Tests;

import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.DateHelper;
import com.example.chadlohrli.myapplication.FlashBackActivity;
import com.example.chadlohrli.myapplication.MockDateHelper;
import com.example.chadlohrli.myapplication.MockDateHelperAfternoon;
import com.example.chadlohrli.myapplication.MockDateHelperMorning;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.MockCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by marissahing on 2/16/18.
 */

public class FlashBackActivityTest {
    private ArrayList<SongData> songs;
    private static Clock clock = Clock.systemDefaultZone();
    private float lat;
    private float lon;
    double time;
    double day;
    FlashBackActivity activity;
    @Rule
    public ActivityTestRule<FlashBackActivity> flashBackActivity = new ActivityTestRule<FlashBackActivity>(FlashBackActivity.class);

    @Before
    public void setup() {
        activity = flashBackActivity.getActivity();
    };

    @Test
    public void testMatchTimeOfDay() {
        activity.setDateHelper(new MockDateHelper());
        double result = flashBackActivity.getActivity().matchTimeOfDay(2);
        assertEquals(2, result, .01);
        activity.setDateHelper(new MockDateHelperAfternoon());
        result = flashBackActivity.getActivity().matchTimeOfDay(1);
        assertEquals(2, result, .01);
        activity.setDateHelper(new MockDateHelperMorning());
        result = flashBackActivity.getActivity().matchTimeOfDay(0);
        assertEquals(2, result, .01);
    }

    @Test
    public void testDay() {
        activity.setDateHelper(new MockDateHelper());
        double result = flashBackActivity.getActivity().matchDay(3);
        assertEquals(2, result, .01);
        result = flashBackActivity.getActivity().matchDay(1);
        assertEquals(0, result, .01);
    }

    @Test
    public void testLocation() {
        double result = flashBackActivity.getActivity().matchLocation(304.8);
        assertEquals(2, result, .01);
        result = flashBackActivity.getActivity().matchLocation(304.9);
        assertEquals(0, result, .01);
    }


}
