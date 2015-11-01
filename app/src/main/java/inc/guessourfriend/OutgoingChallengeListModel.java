package inc.guessourfriend;

import java.util.List;

/**
 * Created by Laura on 10/31/2015.
 */
public class OutgoingChallengeListModel {

    private List<OutgoingChallenge> outgoingChallengeList;

    private void populateOutgoingChallengeList() {
//        DatabaseHelper.getOutgoingChallengeTableRows();
    }

    public List<OutgoingChallenge> getOutgoingChallengeList() {

        return outgoingChallengeList;
    }

    public void addOutgoingChallenge(OutgoingChallenge toAdd) {

        outgoingChallengeList.add(toAdd);
    }

    public void deleteOutgoingChallenge(OutgoingChallenge toDelete) {

        outgoingChallengeList.remove(toDelete);
    }
}
