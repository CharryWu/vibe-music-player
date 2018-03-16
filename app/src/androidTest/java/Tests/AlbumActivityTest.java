package Tests;

import android.os.Environment;
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

import java.io.File;
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
        File nf = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        File[] files = nf.listFiles();
        assertEquals(files.length, songs.size());
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
