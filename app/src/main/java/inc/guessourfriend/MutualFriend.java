package inc.guessourfriend;

import android.net.Uri;

/**
 * Created by Laura on 11/1/2015.
 */
public class MutualFriend {
    private int poolID;
    private String facebookID;
    private boolean isMysteryFriend;
    private boolean hasBeenGuessed;
    public boolean isGrayedOut;

    public int getPoolID() {
        return poolID;
    }

    public String getFacebookID() {
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
        return "";
    }

    public String getProfilePic() {
        return null;
    }

    private void populateFacebookInfo() {

    }
}
