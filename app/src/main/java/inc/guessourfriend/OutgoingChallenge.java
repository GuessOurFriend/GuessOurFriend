package inc.guessourfriend;

/**
 * Created by Laura on 10/31/2015.
 */
public class OutgoingChallenge {

    private long challengeeID;
    private String sentMessage;
    private boolean wasDeclined;

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
