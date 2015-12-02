package inc.guessourfriend.Controllers;

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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
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
    private HashMap<Long, Friend> friendListMap;

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
                    public void onSuccess(final LoginResult loginResult) {
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
                                            takeCareOfInitialDatabaseSetupUponFBLogin(response);
                                            //reGetGcmId();
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

    private void takeCareOfInitialDatabaseSetupUponFBLogin(GraphResponse response){
        try {
            JSONObject json = response.getJSONObject();
            String jsonresult = String.valueOf(json);
            System.out.println("JSON Result" + jsonresult);

            long facebookID = Long.parseLong(json.getString("id"));
            String firstName = json.getString("first_name");
            String lastName = json.getString("last_name");
            String profilePicture = json.getJSONObject("picture").getJSONObject("data").getString("url");
            DatabaseHelper.updateCurrentUser(getApplicationContext(), facebookID);
            model.fbProfileModel = DatabaseHelper.getFBProfileWithID(getApplicationContext(), facebookID);
            //Send the user to our server
            if (model.fbProfileModel == null)
            {
                DatabaseHelper.insertOrUpdateFBProfile(getApplicationContext(), facebookID,
                        null, firstName, lastName, profilePicture);
                NetworkRequestHelper.createUserOnServer(facebookID, firstName, lastName, profilePicture);
            }else{
                DatabaseHelper.insertOrUpdateFBProfile(getApplicationContext(), facebookID,
                        model.fbProfileModel.authToken, firstName, lastName, profilePicture);
                // TODO: delete this line after all testing of application is finished +
                // TODO:        tell server guys to make duplicate user creation not return the auth token
                NetworkRequestHelper.createUserOnServer(facebookID, firstName, lastName, profilePicture);
            }

            model.fbProfileModel = DatabaseHelper.getFBProfileWithID(getApplicationContext(), facebookID);
            model.fbProfileModel.friendList = DatabaseHelper.getFriendList(getApplicationContext());

            friendListMap = new HashMap<Long, Friend>();
            //Get the friends that were returned from Facebook
            JSONArray friends = json.getJSONObject("friends").getJSONArray("data");
            for (int i = 0; i < friends.length(); i++) {
                JSONObject friend = friends.getJSONObject(i);
                facebookID = Long.parseLong(friend.getString("id"));
                firstName = friend.getString("first_name");
                lastName = friend.getString("last_name");
                profilePicture = friend.getJSONObject("picture").getJSONObject("data").getString("url");
                Friend myFriend = new Friend(facebookID, firstName, lastName, profilePicture, false);
                friendListMap.put(facebookID, myFriend);
            }
            doPaging(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cleanFriendList(){
        // Clean up the Friends Table by deleting rows which contain deleted friends
        ArrayList<Friend> deletedFriends = DatabaseHelper.findDeletedFriends(getApplicationContext(), friendListMap);
        for(int i = 0; i < deletedFriends.size(); i++) {
            DatabaseHelper.deleteFriendWithID(getApplicationContext(), deletedFriends.get(i).facebookID);
        }

        // Update the user's friend list in the database with the new friend list
        Set<Long> friendListKeys = friendListMap.keySet();
        Iterator<Long> itr = friendListKeys.iterator();
        while(itr.hasNext()){
            Friend friend = friendListMap.get(itr.next());
            DatabaseHelper.insertOrUpdateFriend(getApplicationContext(),
                    friend.facebookID, friend.firstName, friend.lastName, friend.profilePicture, friend.isBlacklisted);
        }
        model.fbProfileModel.friendList = DatabaseHelper.getFriendList(getApplicationContext());
    }

    private void doPaging(GraphResponse response){
        // Taking paging into account
        GraphRequest nextPageRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);

        if(nextPageRequest != null) {
            nextPageRequest.setCallback(new GraphRequest.Callback() {
                public void onCompleted(GraphResponse newResponse) {
                    if (newResponse.getError() != null) {
                        Log.v("FBGraphRequest Error: ", newResponse.getError().getErrorMessage());
                    } else {
                        System.out.println("Success");
                        try {
                            JSONObject json = newResponse.getJSONObject();
                            //Get the friends that were returned from Facebook
                            JSONArray friends = json.getJSONObject("friends").getJSONArray("data");
                            for (int i = 0; i < friends.length(); i++) {
                                JSONObject friend = friends.getJSONObject(i);
                                long facebookID = Long.parseLong(friend.getString("id"));
                                String firstName = friend.getString("first_name");
                                String lastName = friend.getString("last_name");
                                String profilePicture = friend.getJSONObject("picture").getJSONObject("data").getString("url");
                                Friend myFriend = new Friend(facebookID, firstName, lastName, profilePicture, false);
                                friendListMap.put(facebookID, myFriend);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    GraphRequest newNextPageRequest = newResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                    if (newNextPageRequest != null) {
                        doPaging(newResponse);
                    } else {
                        cleanFriendList();
                    }
                }
            });
            nextPageRequest.executeAsync();
        }else{
            cleanFriendList();
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

    private void reGetGcmId() {
        new AsyncTask<String, String, String>() {

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
                NetworkRequestHelper.updateGcmId(token);
            }
        }.execute();
    }

}