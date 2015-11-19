package inc.guessourfriend;

import com.facebook.login.widget.ProfilePictureView;

import java.io.Serializable;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend{
    public long facebookID;
    public String firstName;
    public String lastName;
    public String profilePicture;
    public boolean isBlacklisted = false;
    private long matchesWon;
    private long matchesLost;
    private long points;
    private long rating;

    public Friend(){
        this.facebookID = -1;
        this.firstName = "";
        this.lastName = "";
        this.profilePicture = "";
        this.matchesLost = 0;
        this.matchesWon =0;
        this.points =0;
        this.rating =0;

    }

    public Friend(long facebookID, String firstName, String lastName, String profilePicture){
        this.facebookID = facebookID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
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


    public boolean isBlacklisted(){
        return isBlacklisted;
    }
    public void setBlacklisted(boolean blacklisted){
        isBlacklisted = blacklisted;
    }
    public void toggleBlacklisted() {
        isBlacklisted = !isBlacklisted ;
    }
    public long getPoints() {
        return points;
    }
    public long getRatings() {
        return rating;
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
    public void updatePoints () {
        points = points + 1;  // TODO: discuss the final logic
    }
    public void updateRatings () {
        // TODO: discuss the final logic
        rating = 5;
    }

    @Override
    public int hashCode() {
        return (int) (facebookID ^ (facebookID >>> 32));
    }
}

