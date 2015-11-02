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
    public boolean isGrayedOut;

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

        Friend friend = DatabaseHelper.getFBProfileTableRow(GuessOurFriend.getAppContext())
                .getFriendById(facebookID);
        return friend.getFullName();
    }

    public String getProfilePic() {
        Friend friend = DatabaseHelper.getFBProfileTableRow(GuessOurFriend.getAppContext())
                .getFriendById(facebookID);
        return friend.getProfilePicture();
    }
}
