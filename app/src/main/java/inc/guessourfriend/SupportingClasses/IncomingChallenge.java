package inc.guessourfriend.SupportingClasses;

/**
 * Created by Ashwini1505 on 01-11-2015.
 */
public class IncomingChallenge {
    public long ChallengerID;

    public IncomingChallenge(){
        this.ChallengerID = -1;
    }
    public IncomingChallenge(long ChallengerID){
        this.ChallengerID = ChallengerID;
    }
    public long getChallengerID() {
        return ChallengerID;
    }
}
