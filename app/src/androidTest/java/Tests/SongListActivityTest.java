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
    public void setUp() {
        songs = songListActivity.getActivity().createSongs();
    }

    @Test
    public void testCreateSongs() {
        String songMade;
        String songRaw;
        boolean songMatch = true;

        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            songMade = songs.get(i).getTitle().toLowerCase().replaceAll("\\s", "");
            songMade = songMade.replace("'", "");
            songRaw = fields[i].getName().toLowerCase().replaceAll("_", "");
            if (!songRaw.equals(songMade)) {
                songMatch = false;
            }
        }
        assertTrue(songMatch);
        assertEquals(fields.length, songs.size());
    }
    @Test
    public void testButtonState() {

    }
}
