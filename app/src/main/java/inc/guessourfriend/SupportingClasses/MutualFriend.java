package inc.guessourfriend.SupportingClasses;

import inc.guessourfriend.SQLiteDB.DatabaseHelper;
import inc.guessourfriend.Application.Model;

/**
 * Created by Laura on 11/1/2015.
 */
public class MutualFriend {
    public int poolID;
    public long facebookID;
    public boolean isMysteryFriend;
    public boolean hasBeenGuessed;
    public String firstName;
    public String lastName;

    public String profilePicture;
    public boolean isGrayedOut;

    public MutualFriend() {
        this.facebookID = -1;
        this.profilePicture = "";
        this.isMysteryFriend = false;
    }

    public MutualFriend(long facebookID, String firstName, String lastName, String profilePicture, boolean isMysteryFriend) {
        this.facebookID = facebookID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.isMysteryFriend = isMysteryFriend;
    }
    public int getPoolID() {
        return poolID;
    }

    public long getFacebookID() {
        return facebookID;
    }

    public boolean getIsMysteryFriend() {
        return isMysteryFriend;
    }

    public boolean getHasBeenGuessed() {
        return hasBeenGuessed;
    }

    public void setHasBeenGuessed(boolean hasBeenGuessed) {
        this.hasBeenGuessed = hasBeenGuessed;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getProfilePic() {
        Friend friend = DatabaseHelper.getCurrentUsersFBProfile(Model.getAppContext())
                .getFriendById(facebookID);
        return friend.profilePicture;
    }
}
