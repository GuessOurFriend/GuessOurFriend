package inc.guessourfriend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChallengesController extends SlideNavigationController {


    private Model model;
    private List<IncomingChallenge> incomingChallengeList;
    ListView listView;

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

        incomingChallengeList = model.incomingChallengeListModel.getIncomingChallengeList();

        listView = (ListView) findViewById(R.id.incominglist);
        IncomingChallenge test = new IncomingChallenge(77778);
        incomingChallengeList.add(test);
        Long [] incomingchallengerID = new Long[incomingChallengeList.size()];

        for (int i = 0; i < incomingChallengeList.size(); i++) {
            incomingchallengerID[i] = incomingChallengeList.get(i).getChallengerID();
        }

        ArrayAdapter<Long> incomingchallengelistAdapter =
                new ArrayAdapter<Long>(this, android.R.layout.simple_list_item_1, incomingchallengerID);

        listView.setAdapter(incomingchallengelistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemPosition = position;
                final Long itemValue = (Long) listView.getItemAtPosition(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(ChallengesController.this);
                adb.setTitle("Accept Challenge Request");
                adb.setMessage("Accept " + itemValue + " a challenge request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            // TODO: Go to Start of Game Controller on clicking the accept button, and start the new activity
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
