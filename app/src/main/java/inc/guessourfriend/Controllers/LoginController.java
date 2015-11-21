package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// Now, when people install or engage with your app, you'll see
//      this data reflected in your app's Insights dashboard:
//      https://www.facebook.com/analytics/468962606617084/
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import inc.guessourfriend.SQLiteDB.DatabaseHelper;
import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.R;
import inc.guessourfriend.SupportingClasses.Friend;

public class LoginController extends FragmentActivity {

    private Model model;
    private static final String TAG_CANCEL = "Cancel";
    private static final String TAG_ERROR = "Error";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
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
                                            takeCareOfInitialDatabaseSetupUponFBLogin(json);
                                            Intent myIntent = new Intent(LoginController.this, ChallengeAFriendController.class);
                                            startActivity(myIntent);
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

    private void takeCareOfInitialDatabaseSetupUponFBLogin(JSONObject json){
        try {
            String jsonresult = String.valueOf(json);
            System.out.println("JSON Result" + jsonresult);

            long facebookID = Long.parseLong(json.getString("id"));
            String firstName = json.getString("first_name");
            String lastName = json.getString("last_name");
            String profilePicture = json.getJSONObject("picture").getJSONObject("data").getString("url");
            model.fbProfileModel = DatabaseHelper.getFBProfile(getApplicationContext());
            if(model.fbProfileModel != null){
                if(model.fbProfileModel.facebookID != facebookID){
                    DatabaseHelper.deleteFBProfile(getApplicationContext());
                }
            }
            //Send the user to our server
            if (DatabaseHelper.getFBProfile(getApplicationContext()) == null)
            {
                DatabaseHelper.insertOrUpdateFBProfile(getApplicationContext(), facebookID,
                        null, firstName, lastName, profilePicture);
                NetworkRequestHelper.createUserOnServer(facebookID, firstName, lastName, profilePicture);
            }else{
                DatabaseHelper.insertOrUpdateFBProfile(getApplicationContext(), facebookID,
                        model.fbProfileModel.authToken, firstName, lastName, profilePicture);
            }

            model.fbProfileModel = DatabaseHelper.getFBProfile(getApplicationContext());

            //TODO: Take paging into account
            //Get the friends that were returned from facebooks
            JSONArray friends = json.getJSONObject("friends").getJSONArray("data");
            HashMap<Long, Friend> friendListMap = new HashMap<Long, Friend>();
            for (int i = 0; i < friends.length(); i++) {
                JSONObject friend = friends.getJSONObject(i);
                facebookID = Long.parseLong(friend.getString("id"));
                firstName = friend.getString("first_name");
                lastName = friend.getString("last_name");
                profilePicture = friend.getJSONObject("picture").getJSONObject("data").getString("url");
                Friend myFriend = new Friend(facebookID, firstName, lastName, profilePicture);
                friendListMap.put(facebookID, myFriend);
            }

            // Clean up the Group Table by deleting rows which contain deleted friends
            ArrayList<Friend> deletedFriends = DatabaseHelper.findDeletedFriends(getApplicationContext(), friendListMap);
            for(int i = 0; i < deletedFriends.size(); i++){
                DatabaseHelper.deleteFriendWithIDFromFriendTableAndFacebookIDBlacklistPairTable(getApplicationContext(),
                        deletedFriends.get(i).facebookID);
            }

            // Update the user's friend list in the database with the new friend list
            Set<Long> friendListKeys = friendListMap.keySet();
            Iterator<Long> itr = friendListKeys.iterator();
            while(itr.hasNext()){
                Friend friend = friendListMap.get(itr.next());
                DatabaseHelper.insertOrUpdateFriend(getApplicationContext(), friend.facebookID,
                        friend.firstName, friend.lastName, friend.profilePicture);
            }

            model.fbProfileModel.friendList = DatabaseHelper.getFriendList(getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            String authToken = DatabaseHelper.getFBProfile(getApplicationContext()).authToken;

            // Add custom implementation, as needed.
            //Request a member from the queue asynchronously
            new NetworkRequestRunner("PUT", "https://guess-our-friend.herokuapp.com/user/gcm_id", authToken).execute(data);
        }
    }.execute();*/

}