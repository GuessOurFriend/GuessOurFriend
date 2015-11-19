package inc.guessourfriend.Controllers;

import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import inc.guessourfriend.Model;
import inc.guessourfriend.R;

public class LeaderboardController extends SlideNavigationController {

    private Model model;
    private List<String> leaderboardList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_leaderboard_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        leaderboardList = model.leaderboardListModel.getLeaderboardList();

        listView = (ListView) findViewById(R.id.leaderboardlist);

        ArrayAdapter <String> leaderboardlistAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leaderboardList);

        listView.setAdapter(leaderboardlistAdapter);


    }

}
