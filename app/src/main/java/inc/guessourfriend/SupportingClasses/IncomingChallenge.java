package inc.guessourfriend.SupportingClasses;

/**
 * Created by Ashwini1505 on 01-11-2015.
 */
public class IncomingChallenge {
    public long challengeId;
    public long challengerId;
    public String firstName;
    public String lastName;
    public long fbId;

    public IncomingChallenge(long challengeId, long challengerId, String firstName, String lastName, long fbId){
        this.challengeId = challengeId;
        this.challengerId = challengerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fbId = fbId;
    }

    public long getChallengeID() {
        return challengeId;
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }

}
