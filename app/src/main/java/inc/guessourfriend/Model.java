package inc.guessourfriend;

import android.app.Application;

/**
 * Created by sellmaurer on 11/16/15.
 */
public class Model extends Application {

    public FBProfileModel fbProfileModel;
    public CurrentGameListModel currentGameListModel;
    public IncomingChallengeListModel incomingChallengeListModel;
    public LeaderboardListModel leaderboardListModel;
    public OutgoingChallengeListModel outgoingChallengeListModel;

    @Override
    public void onCreate (){
        super.onCreate();

        fbProfileModel = DatabaseHelper.getFBProfile(this);
        if(fbProfileModel == null){
            fbProfileModel = new FBProfileModel();
        }
        fbProfileModel.friendList = DatabaseHelper.getFriendList(this);

        currentGameListModel = new CurrentGameListModel();
        incomingChallengeListModel = new IncomingChallengeListModel();
        leaderboardListModel = new LeaderboardListModel();
        outgoingChallengeListModel = new OutgoingChallengeListModel();
    }
}

