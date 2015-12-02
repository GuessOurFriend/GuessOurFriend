package inc.guessourfriend.Controllers;

import android.os.Bundle;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matchmaking_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onMatchmakingButtonClick(View view) {
        Button button = (Button) findViewById(R.id.find_a_game_button);
        if(button.getText().toString().equalsIgnoreCase("Enter Matchmaking Queue")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) findViewById(R.id.find_a_game_button);
                    button.setText("Entering the matchmaking queue");
                    // Disable the button until we get a response from the server
                    button.setEnabled(false);
                }
            });
            NetworkRequestHelper.enterMatchmaking(this);
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) findViewById(R.id.find_a_game_button);
                    button.setText("Leaving the matchmaking queue...");
                    button.setEnabled(false);
                }
            });
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) findViewById(R.id.find_a_game_button);
                    button.setText("Enter Matchmaking Queue");
                    button.setEnabled(true);
                }
            });
            //TODO: Send this user to StartOfGameController

        }else if(taskName.equalsIgnoreCase("entered matchmaking queue")){
            // user has entered matchmaking queue
            //Enable the button and change the text appropriately
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) findViewById(R.id.find_a_game_button);
                    button.setText("Click to leave Matchmaking Queue");
                    button.setEnabled(true);
                }
            });
            // FIXME: How do we get notified of a game in this case?

        }else{
            // user left the matchmaking queue
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) findViewById(R.id.find_a_game_button);
                    button.setText("Enter Matchmaking Queue");
                    button.setEnabled(true);
                }
            });
        }
    }
}
