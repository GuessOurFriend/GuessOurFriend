package inc.guessourfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    public void onBeginMatchmaking(View view) {
        //TODO: Make a call to the server to see if any friends are in the queue
        Long facebookId = null;

        //Check if there was a friend in the queue
        if (facebookId != null) {
            //Create a game with this friend

        } else {
            //Add this user to the queue

        }
    }
}
