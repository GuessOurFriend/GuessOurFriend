package inc.guessourfriend;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 10/31/2015.
 */
public class ChallengeAFriendController extends SlideNavigationController {

    //For View
    ListView listView;

    //For View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_challenge_a_friend_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.list);

        String[] friendNames = new String[fbProfileModel.friendList.size()];

        for (int i = 0; i < fbProfileModel.friendList.size(); i++) {
            friendNames[i] = fbProfileModel.friendList.get(i).getFirstName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, friendNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemPosition = position;
                final String itemValue = (String) listView.getItemAtPosition(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(ChallengeAFriendController.this);
                adb.setTitle("Send Challenge Request");
                adb.setMessage("Send " + itemValue + " a challenge request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                outgoingChallengeListModel.addOutgoingChallenge(
                                        new OutgoingChallenge(fbProfileModel.friendList.get(itemPosition).getFacebookID()));
//                                Log.v("first challengee id", "" + outgoingChallengeListModel
//                                        .getOutgoingChallengeList().get(itemPosition).getChallengeeID());
//                                Log.v("item position", "" + itemPosition);
                                Toast.makeText(getApplicationContext(),
                                        "Position:" + itemPosition + " ListItem: " + itemValue, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });
    }
}
