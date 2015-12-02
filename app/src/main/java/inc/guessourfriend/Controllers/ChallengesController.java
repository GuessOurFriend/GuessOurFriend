package inc.guessourfriend.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import inc.guessourfriend.Models.IncomingChallengeListModel;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.SupportingClasses.IncomingChallenge;
import inc.guessourfriend.Application.Model;
import inc.guessourfriend.R;

public class ChallengesController extends SlideNavigationController implements OnTaskCompleted {

    private Model model;
    ListView listView;
    private ArrayAdapter<IncomingChallenge> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //debug statements: to be removed later
        //String Hello = "Hello";
        //Log.v("Test: challenges:manav","" + Hello);
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_challenges_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        //Go get the incoming challenges
        NetworkRequestHelper.getIncomingChallenges(ChallengesController.this);

        listView = (ListView) findViewById(R.id.incominglist);

        mAdapter = new ArrayAdapter<IncomingChallenge>(this, android.R.layout.simple_list_item_1,
                model.incomingChallengeListModel.getIncomingChallengeList());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final IncomingChallenge itemValue = (IncomingChallenge) listView.getItemAtPosition(position);

                // TODO: Add a cancel button to the alert dialog
                AlertDialog.Builder adb = new AlertDialog.Builder(ChallengesController.this);
                adb.setTitle("Accept Challenge Request");
                adb.setMessage("Accept " + itemValue + "'s challenge request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Accept the challenge
                                NetworkRequestHelper.deleteChallengeFromChallengee(itemValue.challengeId, itemValue.challengerId, true);

                                Toast.makeText(getApplicationContext(),
                                        "Position:" + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT).show();

                                //TODO: Switch directly to StartOfGameController instead?
                                mAdapter.remove(itemValue);
                                mAdapter.notifyDataSetChanged();

                                Intent intent = new Intent(ChallengesController.this, StartOfGameController.class);
                                intent.putExtra("opponentID", itemValue.fbId);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Decline the challenge (and remove it from the model's list and view's list)
                                NetworkRequestHelper.deleteChallengeFromChallengee(itemValue.challengeId, itemValue.challengerId, false);
                                model.incomingChallengeListModel.IncomingChallengeList.remove(itemValue);
                                mAdapter.remove(itemValue);
                                mAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });


    }

    public void onTaskCompleted(String taskName, Object result){
        if(taskName.equalsIgnoreCase("getIncomingChallenges")){
            //Update the model's list and view's list with the result
            model.incomingChallengeListModel = (IncomingChallengeListModel) result;
            mAdapter.clear();
            mAdapter.addAll(model.incomingChallengeListModel.getIncomingChallengeList());
            mAdapter.notifyDataSetChanged();
        }
    }
}
