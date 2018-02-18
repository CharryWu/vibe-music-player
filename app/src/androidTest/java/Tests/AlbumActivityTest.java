package Tests;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by marissahing on 2/17/18.
 */

public class AlbumActivityTest {
    private ArrayList<SongData> songs;
    private ArrayList<Album> albums;

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Rule
    public ActivityTestRule<AlbumActivity> albumActivity = new ActivityTestRule<AlbumActivity>(AlbumActivity.class);

    @Before
    public void setup() {
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
    }

    @Test
    public void testBuildAlbums() {
        albums = albumActivity.getActivity().buildAlbums(songs);
        //check out log statements in debug mode

        ArrayList<String> albumNames = new ArrayList<String>();
        for (int i = 0; i < songs.size(); i++) {
            albumNames.add(songs.get(i).getAlbum());
        }

        Set<String> set = new HashSet<String>(albumNames);
        assertEquals(set.size(), albums.size());
    }
}
