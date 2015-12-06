package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.Models.IncomingChallengeListModel;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.NetworkRequestRunner;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.R;
import inc.guessourfriend.SQLiteDB.DatabaseHelper;
import inc.guessourfriend.SupportingClasses.Friend;
import inc.guessourfriend.SupportingClasses.Game;

public class MatchmakingController extends SlideNavigationController implements OnTaskCompleted {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_matchmaking_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
    }

    // TODO : Delete this method
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matchmaking_controller, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButtonBeforeServerQuery();
        NetworkRequestHelper.checkIfAlreadyInMatchmaking(this);
    }

    public void onMatchmakingButtonClick(View view) {
        Button button = (Button) findViewById(R.id.find_a_game_button);
        if(button.getText().toString().equalsIgnoreCase("Enter Matchmaking Queue")){
            updateButtonForEnteringMatchmaking();
            NetworkRequestHelper.enterMatchmaking(this);
        }else{
            updateButtonForLeavingMatchmaking();
            NetworkRequestHelper.leaveMatchmaking(this);
        }
    }

    public void onTaskCompleted(String taskName, Object gameJSONObject){
        Game game = (Game) gameJSONObject;
        if(taskName.equalsIgnoreCase("found a game")){
            // Update the model with the current game
            if(!model.currentGameListModel.getCurrentGameList().contains(game)){
                model.currentGameListModel.getCurrentGameList().add(game);
            }
            updateButtonForClickToEnter();
            Intent intent = new Intent(this, StartOfGameController.class);
            intent.putExtra("gameId", game.ID);
            intent.putExtra("opponentID", game.getOpponentId());
            startActivity(intent);
        }else if(taskName.equalsIgnoreCase("entered matchmaking queue") ||
                taskName.equalsIgnoreCase("already in matchmaking")){
            updateButtonForClickToLeave();
        }else if(taskName.equalsIgnoreCase("not in matchmaking")){
            updateButtonForClickToEnter();
        }else if(taskName.equalsIgnoreCase("left the matchmaking queue")){
            updateButtonForClickToEnter();
        }else{
            updateButtonForError();
            Log.v("Matchmaking error: ", "matchmaking onTaskCompleted");
        }
    }

    private void updateButtonForError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.find_a_game_button);
                button.setText("Error :(");
                button.setEnabled(false);
            }
        });
    }

    private void updateButtonBeforeServerQuery(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.find_a_game_button);
                button.setText("...");
                button.setEnabled(false);
            }
        });
    }

    private void updateButtonForLeavingMatchmaking(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.find_a_game_button);
                button.setText("Leaving the matchmaking queue...");
                button.setEnabled(false);
            }
        });
    }

    private void updateButtonForEnteringMatchmaking(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.find_a_game_button);
                button.setText("Entering the matchmaking queue");
                // Disable the button until we get a response from the server
                button.setEnabled(false);
            }
        });
    }

    private void updateButtonForClickToEnter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.find_a_game_button);
                button.setText("Enter Matchmaking Queue");
                button.setEnabled(true);
            }
        });
    }

    private void updateButtonForClickToLeave(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.find_a_game_button);
                button.setText("Click to leave Matchmaking Queue");
                button.setEnabled(true);
            }
        });
    }
}
