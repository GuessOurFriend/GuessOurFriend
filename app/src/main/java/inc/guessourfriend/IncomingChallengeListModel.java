package inc.guessourfriend;

import java.util.List;


public class IncomingChallengeListModel {

    private List<IncomingChallenge> IncomingChallengeList;

    public IncomingChallengeListModel() {
        populateIncomingChallengeList();
    }

    private void populateIncomingChallengeList() {

        //TODO: get challenges from database
    }

    public List<IncomingChallenge> getIncomingChallengeList() {

        return IncomingChallengeList;
    }


    public void deleteIncomingChallengeList(IncomingChallenge toDelete) {

        //TODO: remove challenge from database
        IncomingChallengeList.remove(toDelete);
    }
}
