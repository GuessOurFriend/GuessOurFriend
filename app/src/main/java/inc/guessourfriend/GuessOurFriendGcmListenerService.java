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
    public GuessOurFriendGcmListenerService() {
        super();
    }

    @Override
    public void onDeletedMessages() {
        //TODO: Implement?
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Intent intent = new Intent(Intent.ACTION_SEND, null, this, MiddleOfGameController.class);
        intent.setType("text/plain");
        intent.putExtra("theReceivedMessage", data.getString("msg"));
        startActivity(intent);
    }

    @Override
    public void onMessageSent(String msgId) {
        //TODO: Implement

    }

    @Override
    public void onSendError(String msgId, String error) {
        //TODO: Implement? Try sending again?
    }
}
