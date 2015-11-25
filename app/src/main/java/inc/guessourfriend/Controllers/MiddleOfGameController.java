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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.R;

public class MiddleOfGameController extends SlideNavigationController {
    private Model model;
    private String intentReceivedKey = "messageReceived";
    private String intentSentMessageSuccessKey = "sentMessageSuccess";
    private GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    private long gameId = 1l;

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

    private void setUpEnterAndSendTheMessage(){
        final EditText theMessage = (EditText) findViewById(R.id.theMessage);
        theMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    NetworkRequestHelper.sendQuestion(gameId, theMessage.getText().toString());
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
        }, new IntentFilter(intentReceivedKey + gameId));

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
