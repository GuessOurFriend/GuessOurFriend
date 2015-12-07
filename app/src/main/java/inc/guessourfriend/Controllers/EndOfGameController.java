package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.SupportingClasses.Friend;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.Application.Model;
import inc.guessourfriend.R;
import inc.guessourfriend.SupportingClasses.ImageAdapter;
import inc.guessourfriend.SupportingClasses.MutualFriend;

public class EndOfGameController extends SlideNavigationController implements OnTaskCompleted {

    private Model model;
    private Game game;
    private Friend opponentUser;
    // These four variables below will be generated by the variables above
    //      when we get to integration testing
    private long winner;
    private long loser;
    private long opponentMysteryId = -1L;
    // The two mystery friends below will be of type "MutualFriend"
    //      when we get to integration testing
    private Friend myMysteryFriend;
    private Friend theirMysteryFriend;
    String howGameEnded;

    public void onDone(View view){
        // start the current games activity by using "openActivity(int position) - do it this way
        //      because it doesn't work as expected if you just make an intent and start an
        //      activity (this is the consequence of using the slide navigation controller)
        openActivity(3);
    }

    public void onRematch(View view){

    }

    private void displayForWinner(){

        //guessed
        if (game.mysteryFriendId < 0 && game.mysteryFriendId != -1) {
            MutualFriend guessedFriend= null;
            List<MutualFriend> famousPeople = MiddleOfGameController.getFamousPeopleList();
            for (int i = 0; i < famousPeople.size(); i++) {
                if (famousPeople.get(i).facebookID == game.mysteryFriendId) {
                    guessedFriend = famousPeople.get(i);
                }
            }
            ImageView guessedView = (ImageView) findViewById(R.id.guessed_profile_picture_iv);
            String imageUrl = guessedFriend.profilePicture;
            new ImageAdapter.DownloadImageTask(guessedView).execute(imageUrl);
            guessedView.setVisibility(View.VISIBLE);
        } else if (game.mysteryFriendId != -1) {
            Friend guessedFriend = model.fbProfileModel.getFriendById(game.mysteryFriendId);
            ProfilePictureView guessedView = (ProfilePictureView) findViewById(R.id.guessed_profile_picture);
            guessedView.setProfileId(Long.toString(guessedFriend.facebookID));
            TextView guessedNameTextView = (TextView) findViewById(R.id.guessed_name);
            guessedNameTextView.setText(guessedFriend.firstName);
            guessedView.setVisibility(View.VISIBLE);
        }

//        Friend guessedFriend = model.fbProfileModel.friendList.get(1); ////
//        ProfilePictureView guessedView = (ProfilePictureView) findViewById(R.id.guessed_profile_picture);
//        guessedView.setProfileId(Long.toString(guessedFriend.facebookID));
//        TextView guessedNameTextView = (TextView) findViewById(R.id.guessed_name);
//        guessedNameTextView.setText(guessedFriend.firstName);


        //unguessed
        if (opponentMysteryId < 0  && opponentMysteryId != -1) {
            MutualFriend unguessedFriend= null;
            List<MutualFriend> famousPeople = MiddleOfGameController.getFamousPeopleList();
            for (int i = 0; i < famousPeople.size(); i++) {
                if (famousPeople.get(i).facebookID == opponentMysteryId) {
                    unguessedFriend = famousPeople.get(i);
                }
            }
            ImageView unguessedView = (ImageView) findViewById(R.id.unguessed_profile_picture_iv);
            String imageUrl = unguessedFriend.profilePicture;
            new ImageAdapter.DownloadImageTask(unguessedView).execute(imageUrl);
            unguessedView.setVisibility(View.VISIBLE);
        } else if (opponentMysteryId != -1){
            Friend unguessedFriend = model.fbProfileModel.getFriendById(opponentMysteryId);
            ProfilePictureView unguessedView = (ProfilePictureView) findViewById(R.id.unguessed_profile_picture);
            unguessedView.setProfileId(Long.toString(unguessedFriend.facebookID));
            TextView unguessedNameTextView = (TextView) findViewById(R.id.unguessed_name);
            unguessedNameTextView.setText(unguessedFriend.firstName);
            unguessedView.setVisibility(View.VISIBLE);
        }
//        Friend unguessedFriend = model.fbProfileModel.getFriendById(game.mysteryFriendId);
//        ProfilePictureView unguessedView = (ProfilePictureView) findViewById(R.id.unguessed_profile_picture);
//        unguessedView.setProfileId(Long.toString(unguessedFriend.facebookID));
//        TextView unguessedNameTextView = (TextView) findViewById(R.id.unguessed_name);
//        unguessedNameTextView.setText(unguessedFriend.firstName);

        TextView opponentNameTextView = (TextView) findViewById(R.id.opponent_name);
        opponentNameTextView.setText(game.opponentFirstName);
    }

    private void displayForLoser(){
        TextView win_or_lose = (TextView) findViewById(R.id.win_lose_text);
        win_or_lose.setText(R.string.loss_mystery_text0);
        TextView text1 = (TextView) findViewById(R.id.mystery_text1);
        text1.setText(R.string.loss_mystery_text1);
        Log.v("Game", "" + game.mysteryFriendId);
        //guessed
        if (opponentMysteryId < 0 && opponentMysteryId != -1) {
            MutualFriend guessedFriend = null;
            List<MutualFriend> famousPeople = MiddleOfGameController.getFamousPeopleList();
            for (int i = 0; i < famousPeople.size(); i++) {
                if (famousPeople.get(i).facebookID == opponentMysteryId) {
                    guessedFriend = famousPeople.get(i);
                }
            }
            ImageView guessedView = (ImageView) findViewById(R.id.guessed_profile_picture_iv);
            String imageUrl = guessedFriend.profilePicture;
            new ImageAdapter.DownloadImageTask(guessedView).execute(imageUrl);
            guessedView.setVisibility(View.VISIBLE);
        } else if (opponentMysteryId != -1) {
            Friend guessedFriend = model.fbProfileModel.getFriendById(opponentMysteryId);
            ProfilePictureView guessedView = (ProfilePictureView) findViewById(R.id.guessed_profile_picture);
            guessedView.setProfileId(Long.toString(guessedFriend.facebookID));
            TextView guessedNameTextView = (TextView) findViewById(R.id.guessed_name);
            guessedNameTextView.setText(guessedFriend.firstName);
            guessedView.setVisibility(View.VISIBLE);
        }

        //unguessed
        if (game.mysteryFriendId < 0 && game.mysteryFriendId != -1) {
            MutualFriend unguessedFriend= null;
            List<MutualFriend> famousPeople = MiddleOfGameController.getFamousPeopleList();
            for (int i = 0; i < famousPeople.size(); i++) {
                if (famousPeople.get(i).facebookID == game.mysteryFriendId) {
                    unguessedFriend = famousPeople.get(i);
                }
            }
            ImageView unguessedView = (ImageView) findViewById(R.id.unguessed_profile_picture_iv);
            String imageUrl = unguessedFriend.profilePicture;
            new ImageAdapter.DownloadImageTask(unguessedView).execute(imageUrl);
            unguessedView.setVisibility(View.VISIBLE);
        } else if (game.mysteryFriendId != -1) {
            Friend unguessedFriend = model.fbProfileModel.getFriendById(game.mysteryFriendId);
            ProfilePictureView unguessedView = (ProfilePictureView) findViewById(R.id.unguessed_profile_picture);
            unguessedView.setProfileId(Long.toString(unguessedFriend.facebookID));
            TextView unguessedNameTextView = (TextView) findViewById(R.id.unguessed_name);
            unguessedNameTextView.setText(unguessedFriend.firstName);
            unguessedView.setVisibility(View.VISIBLE);
        }
//        Friend unguessedFriend = model.fbProfileModel.friendList.get(1); /////
//        ProfilePictureView unguessedView = (ProfilePictureView) findViewById(R.id.unguessed_profile_picture);
//        unguessedView.setProfileId(Long.toString(unguessedFriend.facebookID));
//        TextView unguessedNameTextView = (TextView) findViewById(R.id.unguessed_name);
//        unguessedNameTextView.setText(unguessedFriend.firstName);

        TextView opponentNameTextView = (TextView) findViewById(R.id.opponent_name);
        opponentNameTextView.setText(game.opponentFirstName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // Set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_end_of_game_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        Intent intent = getIntent();
        if (savedInstanceState == null) {
            Bundle extras = intent.getExtras();
            if(extras == null) {
                howGameEnded = null;
            } else {
                howGameEnded= extras.getString("howGameEnded");
            }
        } else {
            howGameEnded= (String) savedInstanceState.getSerializable("howGameEnded");
        }

        game = (Game) intent.getParcelableExtra("game");
        NetworkRequestHelper.getOpponentMysteryId(EndOfGameController.this, game.ID);

        //////////////////// Done button functionality /////////////

        Button button = (Button) findViewById(R.id.done_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NetworkRequestHelper.sendDone(game.ID);
            }
        });

        Button button2 = (Button) findViewById(R.id.rematch_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NetworkRequestHelper.sendRematch(game.ID);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_of_game_controller, menu);
        return true;
    }

    public void onTaskCompleted(String taskName, Object resultModel) {
        if (taskName.equals("opponentMysteryId")) {
            Long id = (Long) resultModel;
            opponentMysteryId = id;

            if (howGameEnded.equalsIgnoreCase("left") || howGameEnded.equalsIgnoreCase("lost")) {
                loser = model.fbProfileModel.facebookID;
                winner = game.opponentID;
                displayForLoser();
            } else if (howGameEnded.equalsIgnoreCase("won")) {
                loser = game.opponentID;
                winner = model.fbProfileModel.facebookID;
                displayForWinner();
            }
        }
    }
}
