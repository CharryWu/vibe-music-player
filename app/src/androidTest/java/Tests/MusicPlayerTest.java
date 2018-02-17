package Tests;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.chadlohrli.myapplication.MusicPlayer;
import com.example.chadlohrli.myapplication.MusicService;
import com.example.chadlohrli.myapplication.R;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;
import com.example.chadlohrli.myapplication.SongParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Kyle on 2/16/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MusicPlayerTest {

    private ArrayList<SongData> songs;

    @Rule
    public ActivityTestRule<MusicPlayer> musicPlayer = new ActivityTestRule<MusicPlayer>(MusicPlayer.class);

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Before
    public void setup() {
        songs = songListActivity.getActivity().createSongs();

    };

    @Test
    public void testService() throws TimeoutException {
        Intent serviceIntent = new Intent(musicPlayer.getActivity().getApplicationContext(), MusicService.class);
        IBinder binder = mServiceRule.bindService(serviceIntent);

        MusicService service = ((MusicService.MusicBinder) binder).getService();



    }

    @Test
    public void testSongParser() {
        //this tests the SongParser class which is used in SongListActivity

    }

    @Test
    public void testCreateSongs() {


    }

    @Test public void testSongPicked() {



    }

}
