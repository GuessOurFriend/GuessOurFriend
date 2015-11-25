package inc.guessourfriend.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private List<IncomingChallenge> currentChallenges;

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

        currentChallenges = model.incomingChallengeListModel.getIncomingChallengeList();

        //Go get the incoming challenges
        NetworkRequestHelper.getIncomingChallenges(ChallengesController.this);

        listView = (ListView) findViewById(R.id.incominglist);

        mAdapter = new ArrayAdapter<IncomingChallenge>(this, android.R.layout.simple_list_item_1,
                currentChallenges);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final IncomingChallenge itemValue = (IncomingChallenge) listView.getItemAtPosition(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(ChallengesController.this);
                adb.setTitle("Accept Challenge Request");
                adb.setMessage("Accept " + itemValue + " a challenge request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            // TODO: Go to Start of Game Controller on clicking the accept button, and start the new activity
                                Toast.makeText(getApplicationContext(),
                                        "Position:" + position + " ListItem: " + itemValue.firstName, Toast.LENGTH_SHORT).show();
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

    public void onTaskCompleted(String taskName, Object result){
        if(taskName.equalsIgnoreCase("getIncomingChallenges")){
            model.incomingChallengeListModel = (IncomingChallengeListModel) result;
            currentChallenges = model.incomingChallengeListModel.getIncomingChallengeList();

            mAdapter.notifyDataSetChanged();
        }
    }
}
