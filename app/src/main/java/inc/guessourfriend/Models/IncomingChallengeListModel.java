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

        //TODO: get challenges from database
    }

    public List<IncomingChallenge> getIncomingChallengeList() {

        return this.IncomingChallengeList;
    }


    public void deleteIncomingChallengeList(IncomingChallenge toDelete) {

        //TODO: remove challenge from database
        IncomingChallengeList.remove(toDelete);
    }

    public void refreshData(){
        // update this model's data with the data in the database
    }
}
