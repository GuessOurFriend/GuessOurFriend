package inc.guessourfriend;

import android.test.AndroidTestCase;

/**
 * Created by Ashwini1505 on 15-11-2015.
 */
public class FriendTest extends AndroidTestCase {


    public void testgetFacebookID() {
        Friend friend = new Friend(101, "Ashwini", "Balasubramanian", "pp");
        assertEquals(friend.facebookID, 101);

    }
    public void testgetFirstName()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.firstName,"Ashwini");

    }
    public void testgetLastName()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.lastName,"Balasubramanian");
    }
    public void testgetProfilePicture()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.profilePicture,"pp");
    }
    public void testsetBlacklisted()
    {
        Friend friend=new Friend();
        friend.setBlacklisted(true);
        assertEquals(friend.isBlacklisted(),true);

    }
    public void testmatchesLost()
    {
        Friend friend=new Friend();
        friend.incrementMatchesLost();
        assertEquals(friend.getMatchesLost(),1);
    }
    public void testmatchesWon()
    {
        Friend friend=new Friend();
        friend.incrementMatchesWon();
        assertEquals(friend.getMatchesWon(),1);
    }
    public void testtoogleBlackListed()
    {
        Friend friend=new Friend();
        friend.setBlacklisted(true);
        friend.toggleBlacklisted();
        assertEquals(friend.isBlacklisted(),false);
    }
    public void testgetPoints()
    {
        Friend friend=new Friend();
        friend.updatePoints();
        assertEquals(friend.getPoints(),1);
    }
    public void testgetRating()
    {
        Friend friend=new Friend();
        friend.updateRatings();
        assertEquals(friend.getRatings(),5);
    }
}
