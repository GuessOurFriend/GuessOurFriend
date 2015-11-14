package inc.guessourfriend;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend {
    private long facebookID;
    private String fullName;
    private String profilePicture;
    private boolean isBlacklisted = false ; //Manav: I added this field along with some methods to keep track if the friend is backlisted.
    private long matchesWon;
    private long matchesLost;
    private long points;
    private long ratings;


    public Friend(long facebookID, String fullName, String profilePicture){
        this.facebookID = facebookID;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
    }

    public long getFacebookID(){
        return this.facebookID;
    }
    public String getFullName(){
        return this.fullName;
    }
    public String getProfilePicture(){
        return this.profilePicture;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Friend))return false;
        Friend friend = (Friend)other;
        if(this.facebookID == friend.facebookID){
            return true;
        }else{
            return false;
        }
    }
    public boolean isBlacklisted() {
        return isBlacklisted;
    }
    public void setBlacklisted(boolean isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
    }
    public void toggleBlacklisted() {
        isBlacklisted = !isBlacklisted ;
    }
    public long getPoints() {
        return points;
    }
    public long getRatings() {
        return ratings;
    }
    public long getMatchesWon() {
        return matchesWon;
    }
    public long getMatchesLost() {
        return matchesLost;
    }
    public void incrementMatchesWon(){
        matchesWon++;
    }
    public void incrementMatchesLost(){
        matchesLost++;
    }
    public void updatePoints (long points) {
        points = points + 1;  // TODO: discuss the final logic
    }
    public void updateRatings (long ratings) {
        // TODO: discuss the final logic
    }



    @Override
    public int hashCode() {
        return (int) (facebookID ^ (facebookID >>> 32));
    }
}

