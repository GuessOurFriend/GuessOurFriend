package inc.guessourfriend;

import com.facebook.login.widget.ProfilePictureView;

import java.io.Serializable;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend implements Serializable{
    public long facebookID;
    public String firstName;
    public String lastName;
    public String profilePicture;
    public boolean isBlacklisted = false ; //Manav: I added this field along with some methods to keep track if the friend is backlisted.
    public long matchesWon;
    public long matchesLost;
    public long points;
    public long ratings;

    public Friend(){
        this.facebookID = -1;
        this.firstName = "";
        this.lastName = "";
        this.profilePicture = "";
        this.matchesLost = 0;
        this.matchesWon =0;
        this.points =0;
        this.ratings =0;

    }

    public Friend(long facebookID, String firstName, String lastName, String profilePicture){
        this.facebookID = facebookID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }

    public long getFacebookID(){
        return this.facebookID;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
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

