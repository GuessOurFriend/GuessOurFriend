package inc.guessourfriend;

/**
 * Created by mgarg on 11/1/15.
 */

// Class not listed in the Design Document. Need to create it for the LeaderboardListModel


public class User {

    private String facebookProfilePicture;
    private long facebookId;
    private String facebookName;
    private long matchesWon;
    private long matchesLost;
    private long points; // TODO: added new field
    private long ratings; // TODO: added new field

    public User(String facebookProfilePicture,long facebookId, String facebookName){
        this.facebookProfilePicture= facebookProfilePicture;
        this.facebookId= facebookId;
        this.facebookName=facebookName;
        this.matchesLost=0;
        this.matchesWon=0;
        this.points=0;
        this.ratings=5; // TODO: Finalize the initial ratings
    }
    public void updatePoints (long points) {
        points = points + 1;  // TODO: discuss the final logic
    }
    public void updateRatings (long ratings) {
        // TODO: discuss the final logic
    }
    public String getName () {
        return facebookName;
    }
    public long getFacebookId() {
        return facebookId;
    }
    public String getFacebookProfilePicture() {
        return facebookProfilePicture;
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
}
