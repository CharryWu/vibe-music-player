package Tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.chadlohrli.myapplication.Album;
import com.example.chadlohrli.myapplication.AlbumActivity;
import com.example.chadlohrli.myapplication.R;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;
import com.example.chadlohrli.myapplication.SongParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

/**
 * Created by marissahing on 2/17/18.
 */

public class SongParserTest {
    private ArrayList<SongData> songs;
    private ArrayList<Album> albums;
    private SongData sd;
    private Context context;

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Rule
    public ActivityTestRule<AlbumActivity> albumActivity = new ActivityTestRule<AlbumActivity>(AlbumActivity.class);

    @Before
    public void setup() {
        songs = songListActivity.getActivity().createSongs();
        albums = albumActivity.getActivity().buildAlbums(songs);
        context = InstrumentationRegistry.getContext();
        sd = SongParser.parseSong("android.resource://" +
                        songListActivity.getActivity().getPackageName() + "/raw/", R.raw.class.getFields()[0].getName(),
                songListActivity.getActivity().getApplicationContext());
    }

    @Test
    public void testBitmaps() {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getSongs().size() > 1) {
                Bitmap bp = SongParser.albumCover(albums.get(i).getSongs().get(0), context);
                Bitmap bp2 = SongParser.albumCover(albums.get(i).getSongs().get(1), context);
                if (bp != null && bp2 != null) {
                    boolean same = bp.sameAs(bp2);
                    assertTrue(same);
                }
            }
        }
    }

    @Test
    public void testParseSong() {
        assertEquals("backeast", sd.getID());
        assertEquals("I Will Not Be Afraid (A Sampler)", sd.getAlbum());
        assertEquals("Back East", sd.getTitle());
        assertEquals("189518", sd.getLength());
    }
}
