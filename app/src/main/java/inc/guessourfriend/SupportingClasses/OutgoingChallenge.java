package inc.guessourfriend.SupportingClasses;

/**
 * Created by Laura on 10/31/2015.
 */
public class OutgoingChallenge {

    public long fbID;
    public String sentMessage;
    public boolean wasDeclined;

    public OutgoingChallenge() {

        this.fbID = -1;
    }

    public OutgoingChallenge(long fbID) {

        this.fbID = fbID;
    }

    public long getChallengeeID() {
        return fbID;
    }

    public String getSentMessage() {
        return sentMessage;
    }

    public boolean getWasDeclined() {
        return wasDeclined;
    }
}
