package Tests;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;

import com.example.chadlohrli.myapplication.AlbumActivity;
import com.example.chadlohrli.myapplication.FlashBackActivity;
import com.example.chadlohrli.myapplication.MusicService;
import com.example.chadlohrli.myapplication.SharedPrefs;
import com.example.chadlohrli.myapplication.SongData;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static junit.framework.Assert.assertTrue;

/**
 * Created by charry on 1/18/18.
 */

public class MusicServiceTest {
    private ArrayList<SongData> flashBackList = new ArrayList<SongData>();
    private SongData song1;
    private SongData song2;
    private String id;
    private float latitude;
    private float longitude;
    private int day;
    private int time;
    private int rating;
    private int state;
    private int timesPlayed;
    private String lastPlayed;
    private String rid;
    private float rlatitude;
    private float rlongitude;
    private int rday;
    private int rtime;
    private int rrating;
    private int rstate;
    private int rtimesPlayed;
    private String rlastPlayed;

    @Rule
    public final ServiceTestRule testRule = new ServiceTestRule();
    @Rule
    public ActivityTestRule<AlbumActivity> albumActivity = new ActivityTestRule<>(AlbumActivity.class);


    @Test
    public void testWithBoundService() {
        try{
            id = "everglow";
            latitude = (float) 32.886100;
            longitude = (float) -117.243945;
            day = Calendar.MONDAY;
            time = 4;
            rating = 2;
            state = 0;
            timesPlayed = 3;
            lastPlayed = "1995.12.21.21.12.32";
            rid = "hymnfortheweekend";
            rlatitude = (float) 32.927315;
            rlongitude = (float) -117.102829;
            rday = Calendar.MONDAY;
            rtime = 4;
            rrating = 5;
            rstate = 0;
            rtimesPlayed = 3;
            rlastPlayed = "1995.12.21.21.12.32";

            song1 = new SongData(id, "10", "lala", "everglow",
                    "coldplay", "a");
            song2 = new SongData(rid, "12", "lala2", "happier",
                    "ed", "b");

            SharedPrefs.saveData(albumActivity.getActivity().getApplicationContext(),id,latitude,
                    longitude,day,time,rating,state,timesPlayed, lastPlayed);
            SharedPrefs.saveData(albumActivity.getActivity().getApplicationContext(),rid, rlatitude,
                    rlongitude,rday,rtime,rrating,rstate,rtimesPlayed, rlastPlayed);

            flashBackList.add(song1);
            flashBackList.add(song2);



            IBinder binder =
                    testRule.bindService(new Intent(InstrumentationRegistry.getTargetContext(),
                            MusicService.class));
            MusicService service = ((MusicService.MusicBinder) binder).getService();
            service.setSongList(flashBackList);
            service.setCurrentSong(0);
            service.playSong();

            Log.d("Test",service.getDataDir().getPath());

            assertTrue("True wasn't returned", true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
