package inc.guessourfriend;

import android.net.Uri;

/**
 * Created by Laura on 11/1/2015.
 */
public class MutualFriend {
    private int poolID;
    private long facebookID;
    private boolean isMysteryFriend;
    private boolean hasBeenGuessed;
    private String fullName;
    private String profilePicture;
    public boolean isGrayedOut;

    public MutualFriend(long facebookID, String fullName, String profilePicture, boolean isMysteryFriend) {

        this.facebookID = facebookID;
        this.fullName = fullName;
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

    public String getName() {

        Friend friend = DatabaseHelper.getFBProfile(GuessOurFriend.getAppContext())
                .getFriendById(facebookID);
        return friend.getFullName();
    }

    public String getProfilePic() {
        Friend friend = DatabaseHelper.getFBProfile(GuessOurFriend.getAppContext())
                .getFriendById(facebookID);
        return friend.getProfilePicture();
    }
}
