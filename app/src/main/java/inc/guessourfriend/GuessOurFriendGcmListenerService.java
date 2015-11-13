package inc.guessourfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmListenerService;

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
        // TODO: add the game ID to intentReceivedKey in order to differentiate each game's messages
        Intent intent = new Intent(intentReceivedKey, null, this, MiddleOfGameController.class);
        intent.setType("text/plain");
        intent.putExtra(intentReceivedKey, data.getString("msg"));
        sendBroadcast(intent);
    }

    @Override
    public void onMessageSent(String msgId) {
        // TODO: add the game ID to "messageReceived" in order to differentiate each game's messages
        Intent intent = new Intent(intentSentMessageSuccessKey, null, this, MiddleOfGameController.class);
        sendBroadcast(intent);
    }

    @Override
    public void onSendError(String msgId, String error) {
        //TODO: Implement? Try sending again?
    }
}
