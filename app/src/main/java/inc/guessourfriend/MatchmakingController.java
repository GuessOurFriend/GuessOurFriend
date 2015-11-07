package inc.guessourfriend;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MatchmakingController extends SlideNavigationController {

    //For Model
    FBProfileModel fbProfileModel = DatabaseHelper.getFBProfile(GuessOurFriend.getAppContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    //Create an AsyncTaskRunner class to run the network stuff on a separate thread
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Create a URL object pointing to the location of our method
            URL url = null;
            try {
                //TODO: Point this at our server when it's running
                url = new URL("http://jsonplaceholder.typicode.com/posts/1");
            } catch (MalformedURLException ex) {
                //This can't really happen unless we mess up, but AndroidStudio will complain without it
                ex.printStackTrace();
            }

            String result = null;

            //Make the request
            HttpURLConnection urlConnection = null;
            try {
                //Open the connection and convert the result to a string
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();
                result = builder.toString();
            } catch (IOException ex) {
                ex.printStackTrace();
                //TODO: Handle this error more appropriately
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Long facebookId = null;

            //Parse the result as JSON
            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(result.toString());

                //Get the facebook id
                //TODO: Change to parse whatever our model returns
                facebookId = null;//jsonResult.getLong("userId");

                //Check if there was a friend in the queue
                if (facebookId != null) {
                    //Create a game with this friend

                    //TODO: Send to StartOfGameController once it exists
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
            } catch (JSONException ex) {
                //This shouldn't happen unless we mess up
                ex.printStackTrace();
            }
        }
    }

    public void onBeginMatchmaking(View view) {
        //Disable the button while we handle the request
        Button button = (Button) findViewById(R.id.find_a_game_button);
        button.setEnabled(false);

        //Request the queue asynchronously
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }
}
