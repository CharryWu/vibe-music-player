package Tests;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.MainActivity;
import com.example.chadlohrli.myapplication.MusicPlayer;
import com.example.chadlohrli.myapplication.SharedPrefs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.Assert.assertEquals;

/**
 * Created by charry on 1/18/18.
 */

public class SharedPrefTest {
    String id = "everglow";
    float latitude = (float) 32.886100;
    float longitude = (float) -117.243945;
    int day = Calendar.MONDAY;
    int time = 4;
    int rating = 2;
    int s = 0;
    int timesPlayed = 3;
    String lastPlayed = "1995.12.21.21.12.32";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void clear(){
        mainActivity.getActivity().getSharedPreferences(id, 0).edit().clear().commit();
    }

    @Test
    public void saveDataTest(){


        SharedPrefs.saveData(mainActivity.getActivity().getApplicationContext(), id, latitude, longitude, day, time, rating,
                s,timesPlayed, lastPlayed);

        Map<String, ?> map = SharedPrefs.getData(mainActivity.getActivity().getApplicationContext(), id);
        assertEquals(latitude, map.get("Latitude"));
        assertEquals(longitude, map.get("Longitude"));
        assertEquals(day, map.get("Day"));
        assertEquals(time, map.get("Time"));
        assertEquals((float)rating, map.get("Rating"));
        assertEquals(s, map.get("State"));
        assertEquals(timesPlayed, map.get("Times played"));
        assertEquals(lastPlayed, map.get("Last played"));
        assertEquals(0, map.get("fav"));

        SharedPrefs.updateFavorite(mainActivity.getActivity().getApplicationContext(),
                id, 2);
        map = SharedPrefs.getData(mainActivity.getActivity().getApplicationContext(), id);
        assertEquals(1, map.get("fav"));

        SharedPrefs.updateRating(mainActivity.getActivity().getApplicationContext(),
                id, 30);
        map = SharedPrefs.getData(mainActivity.getActivity().getApplicationContext(), id);
        assertEquals((float)30, map.get("Rating"));
    }
}
