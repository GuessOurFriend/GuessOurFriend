package inc.guessourfriend;

import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by Laura on 11/15/2015.
 */
public class FriendListTest extends AndroidTestCase {

    @Test
    public void testFriendList() {
        FBProfileModel fbProfileModel = new FBProfileModel();
        ArrayList<Friend> friendList = new ArrayList<>();
        friendList.add(new Friend(234, "Alice", "Johnson", "pp"));
        friendList.add(new Friend(345, "Bob", "Johnson", "pp"));
        friendList.add(new Friend(456, "Charlie", "Johnson", "pp"));
        friendList.add(new Friend(567, "David", "Johnson", "pp"));
        fbProfileModel.updateFBProfileInfo(123, "Laura", "Xu", "profpic", friendList);
        assertEquals(fbProfileModel.getFriendById(234).firstName, "Alice");
    }

}
