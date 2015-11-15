package inc.guessourfriend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 11/14/2015.
 */
public class StartOfGameController extends SlideNavigationController {
//    FBProfileModel fbProfileModel = DatabaseHelper.getFBProfile(GuessOurFriend.getAppContext());
//    ArrayList<Friend> friendList = DatabaseHelper.getFriendList(GuessOurFriend.getAppContext());

    Game game;

    private void displayStartOfGameScreen() {
//        GridView gridView = (GridView) findViewById(R.id.gridview);
//        gridView.setAdapter(new ImageAdapter(this));
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Toast.makeText(StartOfGameController.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//        gridView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                return false;
//            }
//        });
    }

    private void onHold() {

    }

    private void onSelect() {

    }

    private void onReady() {

    }

    private String[] getImageURLs() {
//        String[] imageURLs = new String[game.myPool.getMutualFriendList().size()];
//        for (int i = 0; i < game.myPool.getMutualFriendList().size(); i++) {
//            imageURLs[i] = game.myPool.getMutualFriendList().get(i).getProfilePic();
//        }
        String[] imageURLs = new String[fbProfileModel.friendList.size()];
        for (int i = 0; i < fbProfileModel.friendList.size(); i++) {
            imageURLs[i] = fbProfileModel.friendList.get(i).getProfilePic();
        }
        return imageURLs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_of_game_controller);
//        GridView gridView = (GridView) findViewById(R.id.gridview);
//        getLayoutInflater().inflate(R.layout.activity_current_games_controller, frameLayout); // change view
//        mDrawerList.setItemChecked(position, true);
//        setTitle(listArray[position]);
        fbProfileModel.friendList = DatabaseHelper.getFriendList(this);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this, getImageURLs()));
        gridView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(StartOfGameController.this, "" + position + " hold", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(StartOfGameController.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
