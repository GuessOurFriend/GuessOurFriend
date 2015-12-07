package inc.guessourfriend.GoogleCloudMessaging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import inc.guessourfriend.Controllers.MiddleOfGameController;

/**
 * Created by Eric on 11/11/15.
 *
 * Documentation: https://developers.google.com/android/reference/com/google/android/gms/gcm/GcmListenerService
 * API Documentation: https://developers.google.com/cloud-messaging/android/client
 */
public class GuessOurFriendGcmListenerService extends GcmListenerService {

    private String intentReceivedKey = "messageReceived";
    private String intentSentMessageSuccessKey = "sentMessageSuccess";

    public GuessOurFriendGcmListenerService() {
        super();
    }

    @Override
    public void onDeletedMessages() {
        //TODO: Implement?
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Bundle theData = data;
        String body = data.getString("body");
        Log.v("The bundle", theData.toString());
        // TODO: add the game ID to intentReceivedKey in order to differentiate each game's messages
        // TODO: the Bundle of data that return is a JSON Object which we don't know the key for
        // TODO:        figure out what format this is in by asking Brian
        // TODO: Current game state: question asked, question answered, now we need to guess a friend or pass up our opportunity to guess
        Intent intent = new Intent(intentReceivedKey);
        intent.putExtra(intentReceivedKey, body);
        sendBroadcast(intent);
    }

    @Override
    public void onMessageSent(String msgId) {

    }

    @Override
    public void onSendError(String msgId, String error) {
        //TODO: Implement? Try sending again?
    }
}
