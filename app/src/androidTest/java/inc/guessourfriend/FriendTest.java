package inc.guessourfriend;

import android.test.AndroidTestCase;

/**
 * Created by Ashwini1505 on 15-11-2015.
 */
public class FriendTest extends AndroidTestCase {


    public void testgetFacebookID() {
        Friend friend = new Friend(101, "Ashwini", "Balasubramanian", "pp");
        assertEquals(friend.getFacebookID(), 101);

    }
    public void testgetFirstName()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.getFirstName(),"Ashwini");

    }
    public void testgetLastName()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.getLastName(),"Balasubramanian");
    }
    public void testgetProfilePicture()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.getProfilePicture(),"pp");
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
        friend.points=100;
   assertEquals(friend.getPoints(),100);
    }
    public void testgetRating()
    {
        Friend friend=new Friend();
        friend.ratings=5;
        assertEquals(friend.getRatings(),5);
    }
}
