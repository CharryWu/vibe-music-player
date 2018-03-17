package Tests;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.chadlohrli.myapplication.MockDateHelper;
import com.example.chadlohrli.myapplication.MockDateHelperAfternoon;
import com.example.chadlohrli.myapplication.MockDateHelperMorning;
import com.example.chadlohrli.myapplication.MusicPlayer;
import com.example.chadlohrli.myapplication.MusicService;
import com.example.chadlohrli.myapplication.R;
import com.example.chadlohrli.myapplication.SharedPrefs;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;
import com.example.chadlohrli.myapplication.SongParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Kyle on 2/16/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MusicPlayerTest {
    private ArrayList<SongData> songs;
    private MusicPlayer musicPlayer;

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
    public void testGetTimeOfDay() {
        musicPlayerRule.getActivity().setDateHelper(new MockDateHelper());
        int timeOfDay = musicPlayerRule.getActivity().getTimeOfDay();
        assertEquals(2, timeOfDay);


        musicPlayerRule.getActivity().setDateHelper(new MockDateHelperAfternoon());
        timeOfDay = musicPlayerRule.getActivity().getTimeOfDay();
        assertEquals(1, timeOfDay);

        musicPlayerRule.getActivity().setDateHelper(new MockDateHelperMorning());
        timeOfDay = musicPlayerRule.getActivity().getTimeOfDay();
        assertEquals(0, timeOfDay);

   }

   @Test
   public void testGetDay() {
        musicPlayerRule.getActivity().setDateHelper(new MockDateHelper());
        int day = musicPlayerRule.getActivity().getDay();
        assertEquals(Calendar.TUESDAY, day);

        musicPlayerRule.getActivity().setDateHelper(new MockDateHelperMorning());
        day = musicPlayerRule.getActivity().getDay();
        assertEquals(Calendar.SATURDAY, day);

        musicPlayerRule.getActivity().setDateHelper(new MockDateHelperAfternoon());
        day = musicPlayerRule.getActivity().getDay();
        assertEquals(Calendar.SUNDAY, day);
   }

}