package inc.guessourfriend;

import android.test.AndroidTestCase;

import org.junit.Test;

import java.util.ArrayList;

import inc.guessourfriend.Models.FBProfileModel;
import inc.guessourfriend.SupportingClasses.Friend;

/**
 * Created by Laura on 11/15/2015.
 */
public class FriendListTest extends AndroidTestCase {

    @Test
    public void testFriendList() {
        FBProfileModel fbProfileModel = new FBProfileModel();
        ArrayList<Friend> friendList = new ArrayList<>();
        friendList.add(new Friend(234, "Alice", "Johnson", "pp", false));
        friendList.add(new Friend(345, "Bob", "Johnson", "pp", false));
        friendList.add(new Friend(456, "Charlie", "Johnson", "pp", false));
        friendList.add(new Friend(567, "David", "Johnson", "pp", false));
        fbProfileModel.updateFBProfileInfo(123, "Laura", "Xu", "profpic", friendList);
        assertEquals(fbProfileModel.getFriendById(234).firstName, "Alice");
    }

    @Test
    public void testUpdateFBProfileInfo() {

        int fbIDBefore = 123;
        String firstNameBefore = "LauraBefore";
        String lastNameBefore = "XuBefore";
        String profilePictureBefore = "ppBefore";
        ArrayList<Friend> friendListBefore = new ArrayList<>();
        friendListBefore.add(new Friend(234, "Alice", "Before1", "pp1", false));
        friendListBefore.add(new Friend(345, "Bob", "Before2", "pp2", false));
        friendListBefore.add(new Friend(456, "Charlie", "Before3", "pp3", false));
        friendListBefore.add(new Friend(567, "David", "Before4", "pp4", false));

        int fbIDAfter = 890;
        String firstNameAfter = "LauraAfter";
        String lastNameAfter = "XuAfter";
        String profilePictureAfter = "ppAfter";
        ArrayList<Friend> friendListAfter = new ArrayList<>();
        friendListAfter.add(new Friend(432, "Alfred", "After1", "pp5", false));
        friendListAfter.add(new Friend(543, "Barb", "After2", "pp6", false));
        friendListAfter.add(new Friend(654, "Carol", "After3", "pp7", false));
        friendListAfter.add(new Friend(765, "Dennis", "After4", "pp8", false));
        friendListAfter.add(new Friend(876, "Eric", "After5", "pp9", false));

        FBProfileModel fbProfileModel = new FBProfileModel();
        fbProfileModel.facebookID = fbIDBefore;
        fbProfileModel.firstName = firstNameBefore;
        fbProfileModel.lastName = lastNameBefore;
        fbProfileModel.profilePicture = profilePictureBefore;
        fbProfileModel.friendList = friendListBefore;

        fbProfileModel.updateFBProfileInfo(fbIDAfter, firstNameAfter, lastNameAfter, profilePictureAfter, friendListAfter);

        assertEquals(fbProfileModel.facebookID, fbIDAfter);
        assertEquals(fbProfileModel.firstName, firstNameAfter);
        assertEquals(fbProfileModel.lastName, lastNameAfter);
        assertEquals(fbProfileModel.profilePicture, profilePictureAfter);

        assertEquals(friendListAfter.size(), 5);
        assertEquals(friendListAfter.get(3).firstName, "Dennis");
    }

}
