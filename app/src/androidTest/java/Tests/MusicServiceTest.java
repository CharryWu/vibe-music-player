package Tests;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;

import com.example.chadlohrli.myapplication.FlashBackActivity;
import com.example.chadlohrli.myapplication.MusicService;
import com.example.chadlohrli.myapplication.SongData;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by charry on 1/18/18.
 */

public class MusicServiceTest {
    @Rule
    public final ServiceTestRule testRule = new ServiceTestRule();

    /*@Test
    public void testWithBoundService() {
        try{
            IBinder binder =
                    testRule.bindService(new Intent(InstrumentationRegistry.getTargetContext(),
                            MusicService.class));
            MusicService service = ((MusicService.MusicBinder) binder).getService();
            assertTrue("True wasn't returned", true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

}
