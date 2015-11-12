package inc.guessourfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class LeaderboardController extends SlideNavigationController {

    LeaderboardListModel leaderboardListModel = new LeaderboardListModel();
    private List<String> LeaderboardList = leaderboardListModel.getLeaderboardList();

    //For View
    ListView listView;


    //For View
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_leaderboard_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        listView = (ListView) findViewById(R.id.leaderboardlist);

        ArrayAdapter <String> leaderboardlistAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LeaderboardList);

        listView.setAdapter(leaderboardlistAdapter);


    }

}
