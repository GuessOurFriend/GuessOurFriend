package inc.guessourfriend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
                DatabaseHelper.insertOrUpdateFBProfile(LoginController.this, facebookID, authToken, firstName, profilePicture);
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
                        myBundle.putString("fields", "id,name,picture,friends{id,name,picture}");
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
                                                String fullName = json.getString("name");
                                                String profilePicture = json.getJSONObject("picture").getJSONObject("data").getString("url");

                                                DatabaseHelper.insertOrUpdateFBProfile(LoginController.this, facebookID, fullName, fullName, profilePicture);

                                                //Send the user to our server
                                                //TODO: Parse name or change how server stores it
                                                if (DatabaseHelper.getFBProfile(LoginController.this) == null)
                                                {
                                                    //createUserOnServer(facebookID, fullName, fullName, profilePicture);
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

                                                ArrayList<Friend> friendList = new ArrayList<Friend>();
                                                //Loop through the list of friends
                                                for (int i = 0; i < friends.length(); i++) {
                                                    JSONObject friendJSONObject = friends.getJSONObject(i);
                                                    facebookID = Long.parseLong(friendJSONObject.getString("id"));
                                                    fullName = friendJSONObject.getString("name");
                                                    profilePicture = friendJSONObject.getJSONObject("picture").getJSONObject("data").getString("url");

                                                    // TODO: Retrieve firstName and lastName from facebook instead of fullName
                                                    Friend friend = new Friend(facebookID, fullName, fullName, profilePicture);
                                                    friendList.add(friend);
                                                }

                                                // programmatically switch to another activity (the first activity we want to show)
                                                Intent myIntent = new Intent(LoginController.this, ChallengeAFriendController.class);
                                                myIntent.putExtra("friendList", friendList);
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

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (isResumed) {
                    if (currentAccessToken == null) {
                        //Delete the FBProfile and Friend rows from the local database
                        DatabaseHelper.deleteFBProfile(LoginController.this);
                    }
                }
            }
        };

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