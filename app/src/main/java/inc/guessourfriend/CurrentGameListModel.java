package inc.guessourfriend;

import java.util.List;

/**
 * Created by Laura on 11/1/2015.
 */
public class CurrentGameListModel {
    public List<Game> currentGameList;
//    private FBProfileModel myProfile; // can be accessed through DatabaseHelper

    public List<Game> getCurrentGameList() {
        return currentGameList;
    }
    //private void populateCurrentGameList() {
    //    //TODO: get current games from database
    //}
}
