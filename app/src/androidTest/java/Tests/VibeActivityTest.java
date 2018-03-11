package Tests;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.MockDateHelper;
import com.example.chadlohrli.myapplication.VibeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        //activity.setDateHelper(new MockDateHelper());
    }

    //match location
    @Test
    public void testMatchLocation() {
        double result = vibeActivity.getActivity().matchLocation(curr_loc);
    }
}
