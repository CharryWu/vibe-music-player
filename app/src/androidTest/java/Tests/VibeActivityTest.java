package Tests;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.MockDateHelper;
import com.example.chadlohrli.myapplication.VibeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sungeun on 3/11/18.
 */

public class VibeActivityTest {
    VibeActivity activity;


    @Rule
    public ActivityTestRule<VibeActivity> vibeActivity = new ActivityTestRule<VibeActivity>(VibeActivity.class);

    @Before
    public void setup() { activity = vibeActivity.getActivity();};
    Location curr_loc = activity.getLoc();

    @Test
    public void testMatchWeek() {
        activity.setDateHelper(new MockDateHelper());
        double result = vibeActivity.getActivity().matchWeek("2018.03.10.16.02.48");
        assertEquals(2, result, .01);

        result = vibeActivity.getActivity().matchWeek("2018.01.10.16.02.49");
        assertEquals(0, result, .01);
    }

    //match location
    @Test
    public void testMatchLocation() {
        double result = vibeActivity.getActivity().matchLocation(curr_loc);
        assertEquals(2, result, .01);
    }
}
