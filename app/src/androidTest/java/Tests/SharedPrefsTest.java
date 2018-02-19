package Tests;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.MusicPlayer;
import com.example.chadlohrli.myapplication.SharedPrefs;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Kyle on 2/17/2018.
 */

import static org.junit.Assert.assertEquals;


public class SharedPrefsTest {
    private String id;
    private float latitude;
    private float longitude;
    private int day;
    private int time;
    private float rating;
    private int state;
    private int timesPlayed;
    private String lastPlayed;
    private ArrayList<SongData> songs;

    @Rule
    public ActivityTestRule<MusicPlayer> musicPlayerRule = new ActivityTestRule<MusicPlayer>(MusicPlayer.class, false, false);

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);
    @Before
    public void setup() {
        songs = songListActivity.getActivity().createSongs();
        Intent intent = new Intent();
        intent.putExtra("SONGS", songs);
        intent.putExtra("CUR", 0);
        musicPlayerRule.launchActivity(intent);
    }

    @Test
    public void testSaveData() {
        id = "everglow";
        latitude = (float) 32.886100;
        longitude = (float) -117.243945;
        day = Calendar.MONDAY;
        time = 4;
        rating = 0;
        state = 0;
        timesPlayed = 3;
        lastPlayed = "1995.12.21.21.12.32";
        SharedPrefs.saveData(musicPlayerRule.getActivity().getApplicationContext(),id,latitude,longitude,day,time,rating,state,timesPlayed, lastPlayed);

        Map<String, ?> map = SharedPrefs.getData(musicPlayerRule.getActivity().getApplicationContext(), id);
        float savedLatitude = (Float) map.get("Latitude");
        float savedLongitude = (Float) map.get("Longitude");
        int savedDay = (Integer) map.get("Day");
        int savedTime = (Integer) map.get("Time");
        float savedRating = (Float) map.get("Rating");
        int savedState = (Integer) map.get("State");
        int savedTimesPlayed = (Integer) map.get("Times played");
        String savedlastPlayed = (String) map.get("Last played");


        assertEquals(latitude, savedLatitude, .1);
        assertEquals(longitude, savedLongitude, .1);
        assertEquals(day, savedDay);
        assertEquals(time, savedTime);
        assertEquals(rating, savedRating, .1);
        assertEquals(state, savedState);
        assertEquals(timesPlayed, savedTimesPlayed);

    }

    @Test
    public void testUpdateFavorite() {
        SharedPrefs.updateFavorite(musicPlayerRule.getActivity().getApplicationContext(), id, 1);
        Map<String, ?> map = SharedPrefs.getData(musicPlayerRule.getActivity().getApplicationContext(), id);
        int savedState = (Integer)map.get("State");
        assertEquals(1, savedState);


    }

    @Test
    public void testGetData() {

    }
}
