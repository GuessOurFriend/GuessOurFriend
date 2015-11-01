package inc.guessourfriend;

import java.util.List;

/**
 * Created by Ashwini1505 on 01-11-2015.
 */
public class OutgoingChallengeListModel {
    private List<Friend> outgoingChallengeList;

    public List<Friend> getOutgoingChallengeList() {
        return this.outgoingChallengeList;
    }
    public void addOutgoingChallenge()
    {

    }

    public void deleteOutgoingChallenge()
    {

    }
    public void populateOutgoingChallengeList(List<Friend> outgoingChallengeList)
    {
        this.outgoingChallengeList = outgoingChallengeList;
    }
}
