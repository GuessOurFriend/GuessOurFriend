package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.SupportingClasses.MutualFriendList;
import inc.guessourfriend.NetworkCommunication.NetworkRequestRunner;
import inc.guessourfriend.R;

public class TestController extends SlideNavigationController {

    private Model model;
    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // get the models
        model = (Model) getApplicationContext();
        // setting up slide menu
        getLayoutInflater().inflate(R.layout.activity_test_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (isResumed) {
                    if (currentAccessToken == null) {
                        // programmatically switch to the LoginController to let the user log back in
                        Intent myIntent = new Intent(TestController.this, LoginController.class);
                        startActivity(myIntent);
                    }
                }
            }
        };


    }

    public void pressed(View view){
        MutualFriendList testing = new MutualFriendList();
        testing.populateMutualFriendList(this);
    }

    public void dbload(View view) {
        NetworkRequestHelper.getUser();
    }

    public void sendTestMessage(View view) {
        //Set up the data to send a message to the currently logged in user (yourself for testing)
        JSONObject data = new JSONObject();
        try {
            data.put("fb_id", model.fbProfileModel.facebookID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get the auth token
        String authToken = model.fbProfileModel.authToken;

        //Make the test message request
        new NetworkRequestRunner("POST", "https://guess-our-friend.herokuapp.com/test_gcm", authToken).execute(data);
    }

    public void pressedEndOfGameButton(View view){
        Intent myIntent = new Intent(this, EndOfGameController.class);
        startActivity(myIntent);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
