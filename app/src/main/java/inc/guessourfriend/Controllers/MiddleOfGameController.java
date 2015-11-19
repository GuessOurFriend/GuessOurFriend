package inc.guessourfriend.Controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.R;

public class MiddleOfGameController extends AppCompatActivity {

    private Model model;
    private String intentReceivedKey = "messageReceived";
    private String intentSentMessageSuccessKey = "sentMessageSuccess";
    private GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        setContentView(R.layout.activity_middle_of_game_controller);
        gcm = GoogleCloudMessaging.getInstance(this);
        setUpIntentListeners();
    }

    private void setUpEnterAndSendTheMessage(){
        EditText theMessage = (EditText) findViewById(R.id.theMessage);
        theMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    //gcm.send
                    // TODO: gcm.send needs the other person's ID in order to send messages
                    //      We're assuming that our ID is the instanceID from the
                    //      GuessOurFriendInstanceIDListenerService class. We need to send each of
                    //      the players IDs to our database in order to get each others IDs and
                    //      send messages to each other.
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
                conversation.append(intent.getStringExtra(intentReceivedKey) + "\n");
            }
        }, new IntentFilter(intentReceivedKey));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EditText theMessage = (EditText) findViewById(R.id.theMessage);
                EditText conversation = (EditText) findViewById(R.id.conversation);
                conversation.append(theMessage.getText() + "\n");
                theMessage.setText("");
            }
        }, new IntentFilter(intentSentMessageSuccessKey));
    }
}
