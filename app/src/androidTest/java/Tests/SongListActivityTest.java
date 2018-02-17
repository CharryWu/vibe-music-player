package Tests;

import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chadlohrli.myapplication.AlbumActivity;
import com.example.chadlohrli.myapplication.BuildConfig;
import com.example.chadlohrli.myapplication.R;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;
import com.example.chadlohrli.myapplication.SongParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by chadlohrli on 2/15/18.
 */

public class SongListActivityTest {

    private ArrayList<SongData> songs;
    private SongData sd;

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Before
    public void setup() {
        sd = SongParser.parseSong("android.resource://" +
                        songListActivity.getActivity().getPackageName() + "/raw/", R.raw.class.getFields()[0].getName(),
                songListActivity.getActivity().getApplicationContext());
    }


    @Test
    public void testSongParser() {
        assertEquals("backeast", sd.getID());
        assertEquals("I Will Not Be Afraid (A Sampler)", sd.getAlbum());
        assertEquals("Back East", sd.getTitle());
        assertEquals("189518", sd.getLength());
    }

    @Test
    public void testCreateSongs() {
        Field[] fields = R.raw.class.getFields();
        ArrayList<SongData> list = songListActivity.getActivity().createSongs();
        assertEquals(fields.length, list.size());
        // Can't compare song file name and song's true name stored in its metadata because they
        // do not exactly match
    }
}
