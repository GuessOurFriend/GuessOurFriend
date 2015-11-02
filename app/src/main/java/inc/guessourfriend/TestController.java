package inc.guessourfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

public class TestController extends SlideNavigationController {

    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // setting up slide menu
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
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
                        //DatabaseHelper.deleteFBProfile(LoginController.this);
                        //DatabaseHelper.getFBProfileTableRows(LoginController.this);
                        //Log.v("Database Operation: ", "Deleted all rows in FBProfile table.");
                    }
                }
            }
        };


    }

    public void pressed(View view){
        MutualFriendList testing = new MutualFriendList();
        testing.populateMutualFriendList(this);
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
