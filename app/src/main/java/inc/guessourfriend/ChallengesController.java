package inc.guessourfriend;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 10/31/2015.
 */
public class ChallengesController extends Activity {
    IncomingChallengeListModel IncomingChallengeList = new IncomingChallengeListModel();

    //For View
    ListView listView;

    //For Controller
    //private list<IncomingChallengeList> incomingChallengeList;

    //For View
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenges);

        listView = (ListView) findViewById(R.id.incominglist);

       // String[] incomingChallengeNames = new String[IncomingChallengeList.size()];

       // for (int i = 0; i < IncomingChallengeList.size(); i++) {
       //     incomingChallengeNames[i] = IncomingChallengeList.getIncomingChallengeList();
       // }

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//               // android.R.layout.simple_list_item_1, android.R.id.text1, friendNames);
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Position:" + itemPosition + " ListItem: " + itemValue, Toast.LENGTH_LONG).show();

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i) {
                            case DialogInterface.BUTTON_POSITIVE:
                              //  outgoingChallengeListModel.addOutgoingChallenge(
                                  //      new OutgoingChallenge(friendList.get(itemPosition).getFacebookID()));
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

            }
        });
    }
}
