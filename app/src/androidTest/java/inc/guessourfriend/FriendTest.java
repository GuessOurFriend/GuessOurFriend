package inc.guessourfriend;

import android.test.AndroidTestCase;

/**
 * Created by Ashwini1505 on 15-11-2015.
 */
public class FriendTest extends AndroidTestCase {


    public void testgetFacebookID()
    {
        Friend friend= new Friend(101,"Ashwini","Balasubramanian","pp");
        assertEquals(friend.getFacebookID(),101);

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
}
