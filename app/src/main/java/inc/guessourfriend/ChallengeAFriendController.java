package inc.guessourfriend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Laura on 10/31/2015.
 */
public class ChallengeAFriendController extends SlideNavigationController {

    //For Model
    FBProfileModel fbProfileModel = DatabaseHelper.getFBProfileTableRow(GuessOurFriend.getAppContext());
    OutgoingChallengeListModel outgoingChallengeListModel = new OutgoingChallengeListModel();

    //For View
    ListView listView;

    //For Controller
    private List<Friend> friendList = fbProfileModel.getFriendList();

    //For View
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_challenge_a_friend_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.list);

        String[] friendNames = new String[friendList.size()];

        for (int i = 0; i < friendList.size(); i++) {
            friendNames[i] = friendList.get(i).getFullName();
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
                                        new OutgoingChallenge(friendList.get(itemPosition).getFacebookID()));
                                Log.v("first challengee id", "" + outgoingChallengeListModel
                                        .getOutgoingChallengeList().get(0).getChallengeeID());
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
