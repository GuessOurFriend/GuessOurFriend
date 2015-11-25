package inc.guessourfriend.Application;

import android.app.Application;
import android.content.Context;

import inc.guessourfriend.Models.CurrentGameListModel;
import inc.guessourfriend.Models.FBProfileModel;
import inc.guessourfriend.Models.IncomingChallengeListModel;
import inc.guessourfriend.Models.LeaderboardListModel;
import inc.guessourfriend.Models.OutgoingChallengeListModel;
import inc.guessourfriend.SQLiteDB.DatabaseHelper;

/**
 * Created by sellmaurer on 11/16/15.
 */
public class Model extends Application {
    private static Context context;

    public FBProfileModel fbProfileModel;
    public CurrentGameListModel currentGameListModel;
    public IncomingChallengeListModel incomingChallengeListModel;
    public LeaderboardListModel leaderboardListModel;
    public OutgoingChallengeListModel outgoingChallengeListModel;

    @Override
    public void onCreate (){
        super.onCreate();
        Model.context = getApplicationContext();
        fbProfileModel = new FBProfileModel();
        currentGameListModel = new CurrentGameListModel();
        incomingChallengeListModel = new IncomingChallengeListModel();
        leaderboardListModel = new LeaderboardListModel();
        outgoingChallengeListModel = new OutgoingChallengeListModel();
    }

    public static Context getAppContext() {
        return Model.context;
    }
}

