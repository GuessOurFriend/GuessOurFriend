package inc.guessourfriend.Controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.R;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.SupportingClasses.ImageAdapter;
import inc.guessourfriend.SupportingClasses.MutualFriend;
import inc.guessourfriend.SupportingClasses.MutualFriendList;

public class MiddleOfGameController extends SlideNavigationController implements OnTaskCompleted {
    private Model model;
    private String intentReceivedKey = "messageReceived";
    private GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    private Game game = new Game();
    private int lastQuestionId = 7;
    String[] profilePictureUrls = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_middle_of_game_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        //Set up GCM messaging
        gcm = GoogleCloudMessaging.getInstance(this);
        setUpIntentListeners();

        //Set up the sending of messages
        setUpEnterAndSendTheMessage();

        //Set up the game board
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (!extrasBundle.isEmpty()) {
            game.myID = extrasBundle.getLong("gameId");
            game.opponentID = extrasBundle.getLong("opponentID");
            game.opponentFirstName = extrasBundle.getString("opponentFirstName");
            game.opponentLastName = extrasBundle.getString("opponentLastName");
            game.setStateOfGame(Game.MIDDLE_OF_GAME);
            game.opponentPool = new MutualFriendList();
            game.opponentPool.mutualFriendList = new ArrayList<MutualFriend>();
            NetworkRequestHelper.getGameBoard(MiddleOfGameController.this, game.myID);

            ProfilePictureView opponent = (ProfilePictureView) findViewById(R.id.opponent_mystery_friend);
            opponent.setProfileId(Long.toString(game.opponentID));

        }
    }

    private void answerQuestion(int answer) {
        NetworkRequestHelper.answerQuestion(MiddleOfGameController.this, game.myID, lastQuestionId, answer);
    }

    public void yesButtonClick(View view) {
        answerQuestion(1);
    }

    public void noButtonClick(View view) {
        answerQuestion(0);
    }

    public void idkButtonClick(View view) {
        answerQuestion(2);
    }

    //TODO: Remove debug button
    public void getGameBoardButtonClicked(View view) {
        NetworkRequestHelper.getGameBoard(MiddleOfGameController.this, game.myID);
    }

    //TODO: Remove debug button
    public void passMyGuessButtonClicked(View view) {
        NetworkRequestHelper.guessMysteryFriend(MiddleOfGameController.this, game.myID, -1);
    }

    //Make clicking send on the keyboard send the message
    private void setUpEnterAndSendTheMessage(){
        final EditText theMessage = (EditText) findViewById(R.id.theMessage);
        theMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    NetworkRequestHelper.sendQuestion(MiddleOfGameController.this, game.myID, theMessage.getText().toString());
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //Set up the recieving of GCM messages to add to the conversation
    private void setUpIntentListeners(){
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EditText conversation = (EditText) findViewById(R.id.conversation);
                conversation.append(intent.getStringExtra("body") + "\n");
            }
        }, new IntentFilter(intentReceivedKey));
    }

    private void createprofilePictureUrls() {
       // Get the image URL's from names in the opponentpool field in the game object
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < model.fbProfileModel.friendList.size(); j++) {
                //if (game.opponentPool.mutualFriendList != null) {
                    if (model.fbProfileModel.friendList.get(j).firstName.equalsIgnoreCase(game.opponentPool.mutualFriendList.get(i).firstName)) {
                        profilePictureUrls[i] = model.fbProfileModel.friendList.get(j).profilePicture;
                        break;
                    }

            }
        }
    }


    private void setUpMutualFriendsList(String[] profilePictureUrls) {
        //Set up the view
        final GridView gridView = (GridView) findViewById(R.id.middle_of_game_gridview);

        //Set up an adapter to hold all the profile pictures
        ImageAdapter imageAdapter = new ImageAdapter(MiddleOfGameController.this, profilePictureUrls);
        gridView.setAdapter(imageAdapter);
    }

    public void onTaskCompleted(String taskName, Object resultModel){
        if(taskName.equals("getGameBoard")) {
            Game fullGame = (Game) resultModel;

            //TODO: Make server return this info too?
            fullGame.myID = game.myID;
            fullGame.opponentID = game.opponentID;
            fullGame.opponentFirstName = game.opponentFirstName;
            fullGame.opponentLastName = game.opponentLastName;
            game = fullGame;
            createprofilePictureUrls();
            setUpMutualFriendsList(profilePictureUrls);

        } else if (taskName.equals("getFriendPool")) {

            //TODO: Get the list of friends

        } else if (taskName.equals("getQuestions")) {

            //TODO: Load previous questions

        } else if(taskName.equalsIgnoreCase("questionSent")){
            EditText theMessage = (EditText) findViewById(R.id.theMessage);
            EditText conversation = (EditText) findViewById(R.id.conversation);
            conversation.append(theMessage.getText() + "\n");
            theMessage.setText("");
        } else if(taskName.equalsIgnoreCase("questionAnswered")) {

        } else if(taskName.equalsIgnoreCase("passedUpMyGuess")) {
            Log.v("Successfully: ", "passed up my guess");
        } else if(taskName.equalsIgnoreCase("myGuessWasWrong")) {

        } else if(taskName.equalsIgnoreCase("iWon")) {

        } else{

        }
    }
}
