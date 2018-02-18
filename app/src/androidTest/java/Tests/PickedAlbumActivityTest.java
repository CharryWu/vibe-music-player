package Tests;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.chadlohrli.myapplication.Album;
import com.example.chadlohrli.myapplication.AlbumActivity;
import com.example.chadlohrli.myapplication.PickedAlbumActivity;
import com.example.chadlohrli.myapplication.R;
import com.example.chadlohrli.myapplication.SongData;
import com.example.chadlohrli.myapplication.SongListActivity;
import com.example.chadlohrli.myapplication.SongParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marissahing on 2/17/18.
 */

public class PickedAlbumActivityTest {
    private ArrayList<SongData> songs;
    private ArrayList<Album> albums;
    private Album albumToCompare;
    private Context context;

    @Rule
    public ActivityTestRule<SongListActivity> songListActivity = new ActivityTestRule<SongListActivity>(SongListActivity.class);

    @Rule
    public ActivityTestRule<AlbumActivity> albumActivity = new ActivityTestRule<AlbumActivity>(AlbumActivity.class);

    @Rule
    public ActivityTestRule<PickedAlbumActivity> pickedAlbumActivity = new ActivityTestRule<PickedAlbumActivity>(PickedAlbumActivity.class, false, false);

    @Before
    public void setup() {
        songs = songListActivity.getActivity().createSongs();
        albums = albumActivity.getActivity().buildAlbums(songs);
        context = InstrumentationRegistry.getContext();
        albumToCompare = albums.get(0);
        Intent intent = new Intent();
        intent.putExtra("ALBUMS", albums);
        intent.putExtra("CUR", albumToCompare);
        pickedAlbumActivity.launchActivity(intent);
    }


    @Test
    public void testPickedAlbum() {
        String albumNameFromIntent;
        String artistNameFromIntent;
        Bitmap albumCoverFromIntent;
        Bitmap albumCover;
        Album cur_album;
        ArrayList<SongData> songsFromIntent;
        ArrayList<SongData> songsFromAlbum;
        ArrayList<Album> albumsFromIntent;

        albumsFromIntent = (ArrayList<Album>) pickedAlbumActivity.getActivity().getIntent().getSerializableExtra("ALBUMS");
        cur_album = albumsFromIntent.get(pickedAlbumActivity.getActivity().getIntent().getIntExtra("CUR", 0));

        songsFromIntent = cur_album.getSongs();
        songsFromAlbum = albumToCompare.getSongs();

        //check if song arrays match
        boolean sameSongs = true;
        if (songsFromIntent.size() == songsFromAlbum.size()) {
            for (int i = 0; i < songsFromAlbum.size(); i++) {
                if (!songsFromAlbum.get(i).getTitle().equals(songsFromIntent.get(i).getTitle())) {
                    sameSongs = false;
                }
            }
        }
        else {
            sameSongs = false;
        }

        albumNameFromIntent = cur_album.getAlbumTitle();
        artistNameFromIntent = cur_album.getArtistName();
        albumCoverFromIntent = SongParser.albumCover(songsFromIntent.get(0), context);
        albumCover = SongParser.albumCover(songsFromAlbum.get(0), context);


        boolean sameAlbum = albumToCompare.getAlbumTitle().equals(albumNameFromIntent);
        boolean sameArtist = albumToCompare.getArtistName().equals(artistNameFromIntent);
        boolean sameCover = albumCover.sameAs(albumCoverFromIntent);

        assertTrue(sameSongs);
        assertTrue(sameAlbum);
        assertTrue(sameArtist);
        assertTrue(sameCover);
    }
}
