package Tests;

import android.content.res.Resources;
import android.os.Environment;
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

import java.io.File;
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
        File nf = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        File[] files = nf.listFiles();
        assertEquals(files.length, songs.size());
    }
}
