package inc.guessourfriend;

import java.util.List;

/**
 * Created by Laura on 10/31/2015.
 */
public class OutgoingChallengeListModel {

    private List<OutgoingChallenge> outgoingChallengeList;

    public OutgoingChallengeListModel() {
        populateOutgoingChallengeList();
    }

    private void populateOutgoingChallengeList() {

        //TODO: get challenges from database
    }

    public List<OutgoingChallenge> getOutgoingChallengeList() {

        return outgoingChallengeList;
    }

    public void addOutgoingChallenge(OutgoingChallenge toAdd) {

        //TODO: add challenge to database
        outgoingChallengeList.add(toAdd);
    }

    public void deleteOutgoingChallenge(OutgoingChallenge toDelete) {

        //TODO: remove challenge from database
        outgoingChallengeList.remove(toDelete);
    }
}
