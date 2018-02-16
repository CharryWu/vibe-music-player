package Tests;

import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chadlohrli.myapplication.R;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by chadlohrli on 2/15/18.
 */

public class SongListActivityTest {

    private ArrayList<SongData> songs;

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Before
    public void setup() {


    };


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
