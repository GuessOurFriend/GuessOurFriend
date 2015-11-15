package inc.guessourfriend;

/**
 * Created by Laura on 10/31/2015.
 */
public class OutgoingChallenge {

    public long challengeeID;
    public String sentMessage;
    public boolean wasDeclined;

    public OutgoingChallenge() {

        this.challengeeID = -1;
    }

    public OutgoingChallenge(long challengeeID) {

        this.challengeeID = challengeeID;
    }

    public long getChallengeeID() {
        return challengeeID;
    }

    public String getSentMessage() {
        return sentMessage;
    }

    public boolean getWasDeclined() {
        return wasDeclined;
    }
}
