/*package Tests;

import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.SharedPrefs;
import com.example.chadlohrli.myapplication.SongAdapter;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;
import com.example.chadlohrli.myapplication.state;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class SongAdapterTest {
    private SongAdapter adapter;
    private ArrayList<SongData> songs = new ArrayList<SongData>();
    private SongData song1;
    private SongData song2;
    private String id;
    private float latitude;
    private float longitude;
    private int day;
    private int time;
    private int rating;
    private int lstate;
    private int timesPlayed;
    private String lastPlayed;
    private int fave;
    private String rid;
    private float rlatitude;
    private float rlongitude;
    private int rday;
    private int rtime;
    private int rrating;
    private int rstate;
    private int rtimesPlayed;
    private String rlastPlayed;
    private int rfave;

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Before
    public void setUp() {
        id = "everglow";
        latitude = (float) 32.886100;
        longitude = (float) -117.243945;
        day = Calendar.MONDAY;
        time = 4;
        rating = 2;
        lstate = state.NEUTRAL.ordinal();
        timesPlayed = 3;
        lastPlayed = "1995.12.21.21.12.32";
        fave = 0;
        rid = "hymnfortheweekend";
        rlatitude = (float) 32.927315;
        rlongitude = (float) -117.102829;
        rday = Calendar.MONDAY;
        rtime = 4;
        rrating = 5;
        rstate = state.DISLIKE.ordinal();
        rtimesPlayed = 3;
        rlastPlayed = "1995.12.21.21.12.32";
        rfave = 0;

        SharedPrefs.saveSongData(songListActivity.getActivity().getApplicationContext(),id,latitude,longitude,day,time,rating,lstate,timesPlayed, lastPlayed, fave);
        SharedPrefs.saveSongData(songListActivity.getActivity().getApplicationContext(),rid, rlatitude,rlongitude,rday,rtime,rrating,rstate,rtimesPlayed, rlastPlayed, rfave);

        song1 = new SongData(id, "10", "lala", "everglow", "coldplay", "a");
        song2 = new SongData(rid, "12", "lala2", "happier", "ed", "b");

        songs.add(song1);
        songs.add(song2);

        adapter = new SongAdapter(songListActivity.getActivity().getApplication(), songs);
    }

    @Test
    public void testCheckSongState() {
        adapter.checkSongState(song1);
        int songState = adapter.getSongState();
        assertEquals(state.NEUTRAL.ordinal(), songState);
        adapter.checkSongState(song2);
        songState = adapter.getSongState();
        assertEquals(state.DISLIKE.ordinal(), songState);
    }

}
*/