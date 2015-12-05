package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.SQLiteDB.DatabaseHelper;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.Application.Model;
import inc.guessourfriend.R;

public class CurrentGamesController extends SlideNavigationController implements OnTaskCompleted {

    private Model model;
    ListView listView;

    private List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_current_games_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.current_games_list);

        //Get the games from the server
        NetworkRequestHelper.getAllGames(CurrentGamesController.this);
    }

    @Override
    public void onTaskCompleted(String taskName, Object model) {
        if (taskName.equals("gamesLoaded")) {
            gameList = (List<Game>) model;

            String[] friendNames = new String[gameList.size()];
            for (int i=0; i<gameList.size(); i++) {
                friendNames[i] = gameList.get(i).opponentFirstName + " " + gameList.get(i).opponentLastName;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, friendNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Game selectedGame = gameList.get(position);
                    if (selectedGame.stateOfGame == Game.START_OF_GAME) {
                        Intent myIntent = new Intent(CurrentGamesController.this, StartOfGameController.class);
                        myIntent.putExtra("gameId", selectedGame.myID);
                        myIntent.putExtra("opponentID", selectedGame.opponentID);
                        myIntent.putExtra("opponentFirstName", selectedGame.opponentFirstName);
                        myIntent.putExtra("opponentLastName", selectedGame.opponentLastName);
                        startActivity(myIntent);
                    } else if (selectedGame.stateOfGame == Game.MIDDLE_OF_GAME) {
                        Intent myIntent = new Intent(CurrentGamesController.this, MiddleOfGameController.class);
                        myIntent.putExtra("gameId", selectedGame.myID);
                        myIntent.putExtra("opponentID", selectedGame.opponentID);
                        myIntent.putExtra("opponentFirstName", selectedGame.opponentFirstName);
                        myIntent.putExtra("opponentLastName", selectedGame.opponentLastName);
                        startActivity(myIntent);
                    }
//                    Intent myIntent = new Intent(CurrentGamesController.this, MiddleOfGameController.class);
//                    myIntent.putExtra("gameId", selectedGame.myID);
//                    myIntent.putExtra("opponentID", selectedGame.opponentID);
//                    myIntent.putExtra("opponentFirstName", selectedGame.opponentFirstName);
//                    myIntent.putExtra("opponentLastName", selectedGame.opponentLastName);
//                    startActivity(myIntent);
                }
            });
        }
    }
}
