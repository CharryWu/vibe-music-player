package Tests;

import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.FlashBackActivity;
import com.example.chadlohrli.myapplication.SharedPrefs;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongSorter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mo on 2/18/18.
 */

public class SongSorterTest {
    private ArrayList<SongData> flashBackList = new ArrayList<SongData>();
    private SongData song1;
    private SongData song2;
    private String id;
    private float latitude;
    private float longitude;
    private int day;
    private int time;
    private int rating;
    private int state;
    private int timesPlayed;
    private String lastPlayed;
    private String rid;
    private float rlatitude;
    private float rlongitude;
    private int rday;
    private int rtime;
    private int rrating;
    private int rstate;
    private int rtimesPlayed;
    private String rlastPlayed;

    @Rule
    public ActivityTestRule<FlashBackActivity> flashBackActivity = new ActivityTestRule<FlashBackActivity>(FlashBackActivity.class);

    @Test
    public void testSongSorter () {
        id = "everglow";
        latitude = (float) 32.886100;
        longitude = (float) -117.243945;
        day = Calendar.MONDAY;
        time = 4;
        rating = 2;
        state = 0;
        timesPlayed = 3;
        lastPlayed = "1995.12.21.21.12.32";
        rid = "hymnfortheweekend";
        rlatitude = (float) 32.927315;
        rlongitude = (float) -117.102829;
        rday = Calendar.MONDAY;
        rtime = 4;
        rrating = 5;
        rstate = 0;
        rtimesPlayed = 3;
        rlastPlayed = "1995.12.21.21.12.32";

        song1 = new SongData(id, "10", "lala", "everglow", "coldplay", "a");
        song2 = new SongData(rid, "12", "lala2", "happier", "ed", "b");

        SharedPrefs.saveData(flashBackActivity.getActivity().getApplicationContext(),id,latitude,longitude,day,time,rating,state,timesPlayed, lastPlayed);
        SharedPrefs.saveData(flashBackActivity.getActivity().getApplicationContext(),rid, rlatitude,rlongitude,rday,rtime,rrating,rstate,rtimesPlayed, rlastPlayed);

        flashBackList.add(song1);
        flashBackList.add(song2);

        Collections.sort(flashBackList, new SongSorter(flashBackActivity.getActivity().getApplicationContext()));

        assertEquals(flashBackList.get(0), song2);
        assertEquals(flashBackList.get(1), song1);

    }
}
