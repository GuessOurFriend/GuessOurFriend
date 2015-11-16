package inc.guessourfriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class CurrentGamesController extends SlideNavigationController {

    FBProfileModel fbProfileModel = DatabaseHelper.getFBProfile(GuessOurFriend.getAppContext());

    ListView listView;

    private List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_current_games_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.current_games_list);

        //TODO get current game list from server
        //// test game
        fbProfileModel.friendList = DatabaseHelper.getFriendList(this);
        Game newGame = new Game();
        newGame.setStateOfGame(Game.START_OF_GAME);
        newGame.myID = fbProfileModel.facebookID;
        newGame.opponentID = fbProfileModel.friendList.get(0).facebookID;
        currentGameListModel.getCurrentGameList().add(newGame);
        ////

        gameList = currentGameListModel.getCurrentGameList();
        String[] gameListFriendNames = new String[gameList.size()];

        for (int i = 0; i < gameList.size(); i++) {
            gameListFriendNames[i] = fbProfileModel.getFriendById(gameList.get(i).getOpponentId()).getFirstName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, gameListFriendNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemPosition = position;
                final String itemValue = (String) listView.getItemAtPosition(position);
                Intent myIntent;
                if (gameList.get(itemPosition).getStateOfGame() == 0) { // Start of Game
                    myIntent = new Intent(CurrentGamesController.this, StartOfGameController.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("opponentID", gameList.get(itemPosition).opponentID);
                    myIntent.putExtras(bundle);
                } else if (gameList.get(itemPosition).getStateOfGame() == 1) { // Middle of Game
                    myIntent = new Intent(CurrentGamesController.this, MiddleOfGameController.class);
                } else { //?? End of Game?
                    myIntent = new Intent(CurrentGamesController.this, EndOfGameController.class);
                }

                startActivity(myIntent);

            }
        });

    }
}
