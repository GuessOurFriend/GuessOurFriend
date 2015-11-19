package inc.guessourfriend.Models;

import java.util.ArrayList;
import java.util.List;

import inc.guessourfriend.SupportingClasses.IncomingChallenge;


public class IncomingChallengeListModel {

    public List<IncomingChallenge> IncomingChallengeList = new ArrayList<>();

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
