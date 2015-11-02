package inc.guessourfriend;

import java.util.ArrayList;
import java.util.List;


public class IncomingChallengeListModel {

    private List<IncomingChallenge> IncomingChallengeList = new ArrayList<>();

    public IncomingChallengeListModel() {
        super();
        populateIncomingChallengeList();
    }

    private void populateIncomingChallengeList() {

        // Hardcoding the data for testing
        IncomingChallenge test = new IncomingChallenge(77777);
        this.IncomingChallengeList.add(test);

        //TODO: get challenges from database
    }

    public List<IncomingChallenge> getIncomingChallengeList() {

        return this.IncomingChallengeList;
    }


    public void deleteIncomingChallengeList(IncomingChallenge toDelete) {

        //TODO: remove challenge from database
        IncomingChallengeList.remove(toDelete);
    }
}
