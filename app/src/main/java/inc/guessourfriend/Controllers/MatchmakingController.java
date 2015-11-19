package inc.guessourfriend.Controllers;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestRunner;
import inc.guessourfriend.R;

public class MatchmakingController extends SlideNavigationController {

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

    public void onBeginMatchmaking(View view) {
        //Disable the button while we handle the request
        Button button = (Button) findViewById(R.id.find_a_game_button);
        button.setEnabled(false);

        String authToken = model.fbProfileModel.authToken;

        //Request a member from the queue asynchronously
        //TODO: Point this at our server when it's running
        new NetworkRequestRunner("GET", "http://jsonplaceholder.typicode.com/posts/1", authToken) {
            @Override
            protected void onPostExecute(JSONObject result) {
                //TODO: Change to parse whatever our model returns
                //Get the facebook id
                Long facebookId = null;
                try {
                    facebookId = result.getLong("userId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Check if there was a friend in the queue
                if (facebookId != null) {
                    //Create a game with this friend

                    //TODO: Send to StartOfGameController once it exists and remove this test code
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Button button = (Button) findViewById(R.id.find_a_game_button);
                            button.setText("Starting Match...");
                        }
                    });
                } else {
                    //TODO: Add this user to the queue via a POST

                    //Enable the button and change the text appropriately
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Button button = (Button) findViewById(R.id.find_a_game_button);
                            button.setText("Cancel Matchmaking");
                            button.setEnabled(true);
                        }
                    });
                }
            }
        }.execute();
    }
}
