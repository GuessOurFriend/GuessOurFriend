package inc.guessourfriend.Models;

import java.util.ArrayList;

import inc.guessourfriend.SupportingClasses.OutgoingChallenge;

/**
 * Created by Laura on 10/31/2015.
 */
public class OutgoingChallengeListModel {

    public ArrayList<OutgoingChallenge> outgoingChallengeList;

    public OutgoingChallengeListModel() {
        populateOutgoingChallengeList();
    }

    public void populateOutgoingChallengeList() {

        this.outgoingChallengeList = new ArrayList<>();
        //TODO: get challenges from database
    }

    public ArrayList<OutgoingChallenge> getOutgoingChallengeList() {

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

    public void refreshData(){
        // update this model's data with the data in the database
    }
}
