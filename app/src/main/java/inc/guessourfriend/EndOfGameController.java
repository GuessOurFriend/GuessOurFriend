package inc.guessourfriend;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

public class EndOfGameController extends SlideNavigationController {

    private Game game;
    private Friend opponentUser;
    // These four variables below will be generated by the variables above
    //      when we get to integration testing
    private long winner;
    private long loser;
    // The two mystery friends below will be of type "MutualFriend"
    //      when we get to integration testing
    private Friend myMysteryFriend;
    private Friend theirMysteryFriend;

    public void onDone(View view){
        // start the current games activity by using "openActivity(int position) - do it this way
        //      because it doesn't work as expected if you just make an intent and start an
        //      activity (this is the consequence of using the slide navigation controller)
        openActivity(3);
    }

    public void onRematch(View view){

    }

    private void displayForWinner(){
        Friend guessedFriend = fbProfileModel.friendList.get(0);
        ProfilePictureView guessedView = (ProfilePictureView) findViewById(R.id.guessed_profile_picture);
        guessedView.setProfileId(Long.toString(guessedFriend.getFacebookID()));
        TextView guessedNameTextView = (TextView) findViewById(R.id.guessed_name);
        guessedNameTextView.setText(guessedFriend.getFirstName());

        Friend unguessedFriend = fbProfileModel.friendList.get(1);
        ProfilePictureView unguessedView = (ProfilePictureView) findViewById(R.id.unguessed_profile_picture);
        unguessedView.setProfileId(Long.toString(unguessedFriend.getFacebookID()));
        TextView unguessedNameTextView = (TextView) findViewById(R.id.unguessed_name);
        unguessedNameTextView.setText(unguessedFriend.getFirstName());

        Friend opponent = fbProfileModel.friendList.get(2);
        TextView opponentNameTextView = (TextView) findViewById(R.id.opponent_name);
        opponentNameTextView.setText(opponent.getFirstName());
    }

    private void displayForLoser(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_end_of_game_controller, frameLayout);
        //mDrawerList.setItemChecked(position, true);
        //setTitle(listArray[position]);

        // Simulating a loss
        //winner = fbProfile.getFriendList().get(1).getFacebookID();
        //loser = fbProfile.getFacebookID();
        // Simulating a win
        winner = fbProfileModel.facebookID;
        loser = fbProfileModel.friendList.get(1).getFacebookID();
        if(winner == fbProfileModel.facebookID){
            // this user is the winner
            displayForWinner();
        }else{
            displayForLoser();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_of_game_controller, menu);
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
}
