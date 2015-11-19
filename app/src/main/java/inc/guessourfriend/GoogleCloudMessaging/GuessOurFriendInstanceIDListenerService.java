package inc.guessourfriend.GoogleCloudMessaging;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Eric on 11/11/15.
 *
 * Documentation: https://developers.google.com/android/reference/com/google/android/gms/iid/InstanceIDListenerService
 * API Documentation: https://developers.google.com/cloud-messaging/android/client
 */
public class GuessOurFriendInstanceIDListenerService extends InstanceIDListenerService {

    private String token = "";

    public GuessOurFriendInstanceIDListenerService() {
        super();
    }

    @Override
    public void onCreate() {
        // Fetch the new Instance ID token and notify our app's server of it
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    @Override
    public void onDestroy() {
        //TODO: Implement?
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO: Implement?
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
