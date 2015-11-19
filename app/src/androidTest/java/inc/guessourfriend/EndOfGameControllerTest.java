package inc.guessourfriend;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by sellmaurer on 11/15/15.
 */
public class EndOfGameControllerTest extends AndroidTestCase {

    FBProfileModel fbProfileModel;

    public void setUp() throws Exception {
        fbProfileModel = new FBProfileModel();
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        Friend friend1 = new Friend(1, "manav", "garg", "propic1");
        Friend friend2 = new Friend(2, "laura", "xu", "propic2");
        Friend friend3 = new Friend(3, "eric", "koth", "propic3");
        Friend friend4 = new Friend(4, "ash", "balasubramanian", "propic4");
        friendList.add(friend1);
        friendList.add(friend2);
        friendList.add(friend3);
        friendList.add(friend4);
        fbProfileModel.friendList = friendList;
    }

    @Test
    public void displayForWinner() throws Exception{
        boolean testPassed = true;

        Friend friend1 = fbProfileModel.friendList.get(0);
        if((friend1.facebookID != 1) && (!friend1.firstName.equalsIgnoreCase("manav"))){
            testPassed = false;
        }

        Friend friend2 = fbProfileModel.friendList.get(1);
        if((friend2.facebookID != 2) && (!friend2.firstName.equalsIgnoreCase("laura"))){
            testPassed = false;
        }

        Friend friend3 = fbProfileModel.friendList.get(2);
        if((friend3.facebookID != 3) && (!friend3.firstName.equalsIgnoreCase("eric"))){
            testPassed = false;
        }

        Friend friend4 = fbProfileModel.friendList.get(3);
        if((friend4.facebookID != 4) && (!friend4.firstName.equalsIgnoreCase("ash"))){
            testPassed = false;
        }
        Assert.assertEquals(true, testPassed);
    }
}