package inc.guessourfriend.Controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.R;

public class MiddleOfGameController extends SlideNavigationController implements OnTaskCompleted {
    private Model model;
    private String intentReceivedKey = "messageReceived";
    private String intentSentMessageSuccessKey = "sentMessageSuccess";
    private GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    private long gameId = 4l;
    private int lastQuestionId = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_middle_of_game_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        gcm = GoogleCloudMessaging.getInstance(this);
        setUpIntentListeners();
        setUpEnterAndSendTheMessage();
    }

    private void answerQuestion(int answer) {
        NetworkRequestHelper.answerQuestion(MiddleOfGameController.this, gameId, lastQuestionId, answer);
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

    public void getGameBoardButtonClicked(View view) {
        NetworkRequestHelper.getGameBoard(MiddleOfGameController.this, gameId);
    }

    public void passMyGuessButtonClicked(View view) {
        NetworkRequestHelper.guessMysteryFriend(MiddleOfGameController.this, gameId, -1);
    }

    private void setUpEnterAndSendTheMessage(){
        final EditText theMessage = (EditText) findViewById(R.id.theMessage);
        theMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    NetworkRequestHelper.sendQuestion(MiddleOfGameController.this, gameId, theMessage.getText().toString());
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setUpIntentListeners(){
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EditText conversation = (EditText) findViewById(R.id.conversation);
                conversation.append(intent.getStringExtra("body") + "\n");
            }
        }, new IntentFilter(intentReceivedKey));
    }

    public void onTaskCompleted(String taskName, Object resultModel){
        if(taskName.equalsIgnoreCase("questionSent")){
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
