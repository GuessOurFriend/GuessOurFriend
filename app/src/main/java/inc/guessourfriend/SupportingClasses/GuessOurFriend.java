package inc.guessourfriend.SupportingClasses;

import android.app.Application;
import android.content.Context;

/**
 * Created by Laura on 11/1/2015.
 */
public class GuessOurFriend extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        GuessOurFriend.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GuessOurFriend.context;
    }
}
