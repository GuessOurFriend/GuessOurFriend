package inc.guessourfriend.Models;

import java.util.ArrayList;
import java.util.List;

import inc.guessourfriend.SupportingClasses.Game;

/**
 * Created by Laura on 11/1/2015.
 */
public class CurrentGameListModel {
    private List<Game> currentGameList;
//    private FBProfileModel myProfile; // can be accessed through DatabaseHelper

    public CurrentGameListModel(){
        currentGameList = new ArrayList<Game>(); // switch this out with server query which retrieves current games
    }

    public List<Game> getCurrentGameList() {
        return currentGameList;
    }
    //private void populateCurrentGameList() {
    //    //TODO: get current games from database
    //}
}
