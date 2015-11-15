package inc.guessourfriend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// Now, when people install or engage with your app, you'll see
//      this data reflected in your app's Insights dashboard:
//      https://www.facebook.com/analytics/468962606617084/
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginController extends FragmentActivity {

    private static final String TAG_CANCEL = "Cancel";
    private static final String TAG_ERROR = "Error";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed;

    public void createUserOnServer(final long facebookID, final String firstName, final String lastName, final String profilePicture) {

        JSONObject dataToPost = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            user.put("fb_id", facebookID);
            user.put("gcm_id", "");
            user.put("first_name", firstName);
            user.put("last_name", lastName);
            dataToPost.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Request a member from the queue asynchronously
        new NetworkRequestRunner("POST", "https://guess-our-friend.herokuapp.com/users") {
            @Override
            protected void onPostExecute(JSONObject result) {
                //Parse the auth token in the response so we can update this user in the future
                String authToken = null;
                try {
                    authToken = result.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Insert the FBProfile into the local database
                DatabaseHelper.insertOrUpdateFBProfile(LoginController.this, facebookID, authToken,
                        firstName, lastName, profilePicture);
            }
        }.execute(dataToPost);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login_controller);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Bundle myBundle = new Bundle();
                        myBundle.putString("fields", "id,first_name,last_name,picture,friends{id,first_name,last_name,picture}");
                        new GraphRequest(
                                loginResult.getAccessToken(),
                                "/me",
                                myBundle,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        if (response.getError() != null) {
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            JSONObject json = response.getJSONObject();
                                            try {
                                                //TODO: Remove debugging info
                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                //Get the basic info for this user
                                                long facebookID = Long.parseLong(json.getString("id"));
                                                String firstName = json.getString("first_name");
                                                String lastName = json.getString("last_name");
                                                String profilePicture = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                                // TODO: put Ruby on Rails auth token in fb profile model
                                                DatabaseHelper.insertOrUpdateFBProfile(LoginController.this, facebookID, "token goes here", firstName, lastName, profilePicture);

                                                //Send the user to our server
                                                if (DatabaseHelper.getFBProfile(LoginController.this) == null)
                                                {
                                                    //createUserOnServer(facebookID, firstName, lastName, profilePicture);
                                                }

                                                //TODO: Delete this. It's here incase someone else needs the gcm_id manually added
                                                /*new AsyncTask<String, String, String>() {

                                                    @Override
                                                    protected String doInBackground(String... params) {
                                                        try {
                                                            return InstanceID.getInstance(LoginController.this).getToken(getString(R.string.gcm_defaultSenderId),
                                                                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        return "";
                                                    }
                                                    @Override
                                                    protected void onPostExecute(String token) {
                                                        System.out.println("Hello: " + token);
                                                        JSONObject data = new JSONObject();
                                                        try {
                                                            data.put("gcm_id", token);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        //Get the auth token
                                                        String authToken = DatabaseHelper.getFBProfile(getApplicationContext()).getAuthToken();

                                                        // Add custom implementation, as needed.
                                                        //Request a member from the queue asynchronously
                                                        new NetworkRequestRunner("PUT", "https://guess-our-friend.herokuapp.com/user/gcm_id", authToken).execute(data);
                                                    }
                                                }.execute();*/

                                                //Get the friends that were returned
                                                //TODO: Take paging into account
                                                JSONArray friends = json.getJSONObject("friends").getJSONArray("data");

                                                //Loop through the list of friends
                                                for (int i = 0; i < friends.length(); i++) {
                                                    JSONObject friendJSONObject = friends.getJSONObject(i);
                                                    facebookID = Long.parseLong(friendJSONObject.getString("id"));
                                                    firstName = friendJSONObject.getString("first_name");
                                                    lastName = friendJSONObject.getString("last_name");
                                                    profilePicture = friendJSONObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                                    DatabaseHelper.insertOrUpdateFriend(LoginController.this, facebookID, firstName, lastName, profilePicture);
                                                }

                                                // programmatically switch to another activity (the first activity we want to show)
                                                Intent myIntent = new Intent(LoginController.this, SlideNavigationController.class);
                                                startActivity(myIntent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                        ).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_controller, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

}