package inc.guessourfriend.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.SupportingClasses.OutgoingChallenge;
import inc.guessourfriend.R;

/**
 * Created by Laura on 10/31/2015.
 */
public class ChallengeAFriendController extends SlideNavigationController {

    private Model model;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // Set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_challenge_a_friend_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.list);

        String[] friendNames = new String[model.fbProfileModel.friendList.size()];
        for (int i = 0; i < model.fbProfileModel.friendList.size(); i++) {
            friendNames[i] = model.fbProfileModel.friendList.get(i).firstName;
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
                                model.outgoingChallengeListModel.addOutgoingChallenge(
                                        new OutgoingChallenge(model.fbProfileModel.friendList.get(itemPosition).facebookID));
//                                Log.v("first challengee id", "" + model.outgoingChallengeListModel
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
