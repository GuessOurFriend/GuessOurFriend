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

public class ChallengesController extends Activity {

    IncomingChallengeListModel incomingChallengeListModel = new IncomingChallengeListModel();
    private List<IncomingChallenge> incomingChallengeList = incomingChallengeListModel.getIncomingChallengeList();

    //For View
    ListView listView;


    //For View
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenges);

        listView = (ListView) findViewById(R.id.incominglist);
        Long [] incomingchallengerID = new Long[incomingChallengeList.size()];

        for (int i = 0; i < incomingChallengeList.size(); i++) {
            incomingchallengerID[i] = incomingChallengeList.get(i).getChallengerID();
        }

        ArrayAdapter<Long> incomigchallengelistAdapter =
                new ArrayAdapter<Long>(this, android.R.layout.simple_list_item_1, incomingchallengerID);

        listView.setAdapter(incomigchallengelistAdapter);

    }
}
