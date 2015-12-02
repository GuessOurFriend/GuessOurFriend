package inc.guessourfriend.SupportingClasses;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend implements Comparable<Friend>{
    public long facebookID;
    public String firstName;
    public String lastName;
    public String profilePicture;
    public boolean isBlacklisted;
    public boolean isChallenged;
    private long matchesWon;
    private long matchesLost;
    private long points;
    private long rating;

    @Override
    public int compareTo(Friend otherFriend) throws UnsupportedOperationException {
        if(otherFriend != null){
            if(otherFriend instanceof Friend){
                return (this.firstName + this.lastName).compareTo(otherFriend.firstName + otherFriend.lastName);
            }
        }
        throw new UnsupportedOperationException("passed in compareTo for Friend class is null or not of the right type of object");
    }

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

    public Friend(long facebookID, String firstName, String lastName, String profilePicture, boolean isBlacklisted){
        this.facebookID = facebookID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.isBlacklisted = isBlacklisted;
        this.matchesLost = 0;
        this.matchesWon =0;
        this.points =0;
        this.rating =0;
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


    public boolean isBlacklisted(){return isBlacklisted;}
    public boolean isChallenged(){return isChallenged;}
    public void setBlacklisted(boolean blacklisted){isBlacklisted = blacklisted;}
    public void setChallenged(boolean challenged){isChallenged = challenged;}
    public void toggleBlacklisted() {
        isBlacklisted = !isBlacklisted ;
    }
    public void toggleChallenged() {
        isChallenged = !isChallenged ;
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

