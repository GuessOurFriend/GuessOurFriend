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
    CurrentGameListModel currentGameListModel; //get from database

    ListView listView;

    private List<Game> gameList = currentGameListModel.getCurrentGameList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_current_games_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.current_games_list);

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
                } else if (gameList.get(itemPosition).getStateOfGame() == 1) { // Middle of Game
                    myIntent = new Intent(CurrentGamesController.this, MiddleOfGameController.class);
                } else { //?? End of Game?
                    myIntent = new Intent(CurrentGamesController.this, MiddleOfGameController.class);
                }

                startActivity(myIntent);

            }
        });
    }
}
