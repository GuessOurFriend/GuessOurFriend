package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

import inc.guessourfriend.R;

public class OptionsController extends SlideNavigationController {

    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        getLayoutInflater().inflate(R.layout.activity_options_controller, frameLayout);
        //mDrawerList.setItemChecked(position, true);
        //setTitle(listArray[position]);
        Button blacklistButton = (Button) findViewById(R.id.blacklistbutton);
        Button inviteButton = (Button) findViewById(R.id.inviteafriendbutton);
        Button reportABugButton = (Button) findViewById(R.id.reportabugbutton);

        blacklistButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OptionsController.this, BlacklistController.class);
                startActivity(intent);
                finish();
            }
        });

        reportABugButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OptionsController.this, ReportABugController.class);
                intent.putExtra("intentFrom", "OptionsController");
                startActivity(intent);
            }
        });

        //Set up handling of the Facebook logout button
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (isResumed) {
                    if (currentAccessToken == null) {
                        // programmatically switch to the LoginController to let the user log back in
                        Intent myIntent = new Intent(OptionsController.this, LoginController.class);
                        startActivity(myIntent);
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
