package inc.guessourfriend;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import inc.guessourfriend.Models.LeaderboardListModel;

/**
 * Created by mgarg on 11/15/15.
 */
public class LeaderboardListModelTest extends AndroidTestCase {

    LeaderboardListModel leaderboardListModel;
    public void setUp() throws Exception {
       leaderboardListModel = new LeaderboardListModel();
    }

    @Test

    public void getleaderboardlist(){

        List <String> teststring = new ArrayList<>();
        String testEntry1 = "FBID: 77777   Points: 98" ;
        teststring.add(testEntry1);
        String testEntry2 = "FBID: 77778   Points: 71" ;
        teststring.add(testEntry2);
        String testEntry3 = "FBID: 77774   Points: 64" ;
        teststring.add(testEntry3);
        String testEntry4 = "FBID: 77776   Points: 58" ;
        teststring.add(testEntry4);
        String testEntry5 = "FBID: 77775   Points: 46" ;
        teststring.add(testEntry5);
        String testEntry6 = "FBID: 77779   Points: 36" ;
        teststring.add(testEntry6);

        List <String> teststringreturned = new ArrayList<>();
        teststringreturned = leaderboardListModel.getLeaderboardList();

        Assert.assertEquals(teststring,teststringreturned);


    }

}