package inc.guessourfriend.Controllers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.R;
import inc.guessourfriend.SupportingClasses.Friend;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.SupportingClasses.ImageAdapter;
import inc.guessourfriend.SupportingClasses.MutualFriend;
import inc.guessourfriend.SupportingClasses.MutualFriendList;

public class MiddleOfGameController extends SlideNavigationController implements OnTaskCompleted {
    private Model model;
    private List<MutualFriend> famousPeople;
    private String intentReceivedKey = "messageReceived";
    private GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    private Game game = new Game();
    private int lastQuestionId = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_middle_of_game_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        //Set up GCM messaging
        gcm = GoogleCloudMessaging.getInstance(this);
        setUpIntentListeners();

        //Set up the sending of messages
        setUpEnterAndSendTheMessage();

        //Set up the game board
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (!extrasBundle.isEmpty()) {
            game.ID = extrasBundle.getLong("gameId");
            game.opponentID = extrasBundle.getLong("opponentID");
            game.opponentFirstName = extrasBundle.getString("opponentFirstName");
            game.opponentLastName = extrasBundle.getString("opponentLastName");
            game.setStateOfGame(Game.MIDDLE_OF_GAME);
            game.opponentPool = new MutualFriendList();
            game.opponentPool.mutualFriendList = new ArrayList<MutualFriend>();
            NetworkRequestHelper.getGameBoard(MiddleOfGameController.this, game.ID);

            ProfilePictureView opponent = (ProfilePictureView) findViewById(R.id.opponent_mystery_friend);
            opponent.setProfileId(Long.toString(game.opponentID));

        }
        yourTurn();
    }
    public void yourTurn()
    {
        TextView textView = (TextView) findViewById(R.id.your_turn_text);
        TextView textView1= (TextView) findViewById(R.id.their_turn_text);
        Game game = new Game();
        boolean turn = game.getIsMyTurn();
        if (turn == false)
        {
            textView.setVisibility(View.GONE);
            textView1.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.GONE);
        }

        }

    private void answerQuestion(int answer) {
        NetworkRequestHelper.answerQuestion(MiddleOfGameController.this, game.ID, lastQuestionId, answer);
        yourTurn();
    }

    public void yesButtonClick(View view) {
        answerQuestion(1);
    }

    public void noButtonClick(View view) {
        answerQuestion(0);
    }

    public void idkButtonClick(View view) {
        answerQuestion(2);
    }

    //TODO: Remove debug button
    public void getGameBoardButtonClicked(View view) {
        NetworkRequestHelper.getGameBoard(MiddleOfGameController.this, game.ID);
    }

    //TODO: Remove debug button
    public void passMyGuessButtonClicked(View view) {
        NetworkRequestHelper.guessMysteryFriend(MiddleOfGameController.this, game.ID, -1);
        yourTurn();
    }


    //Make clicking send on the keyboard send the message
    private void setUpEnterAndSendTheMessage(){
        final EditText theMessage = (EditText) findViewById(R.id.theMessage);
        theMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    NetworkRequestHelper.sendQuestion(MiddleOfGameController.this, game.ID, theMessage.getText().toString());
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        yourTurn();
    }

    //Set up the recieving of GCM messages to add to the conversation
    private void setUpIntentListeners(){
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EditText conversation = (EditText) findViewById(R.id.conversation);
                conversation.append(intent.getStringExtra("body") + "\n");
            }
        }, new IntentFilter(intentReceivedKey));
    }

    private void createprofilePictureUrls() {
        //Also account for famous people URLs
        getFamousPeopleList();

        // Get the image URL's from names in the opponentpool field in the game object
        for (int i = 0; i < 20; i++) {
            MutualFriend currPoolFriend = game.opponentPool.mutualFriendList.get(i);
            if (currPoolFriend.facebookID > 0) {
                //It's a real id, find it in our facebook friends list
                for (int j = 0; j < model.fbProfileModel.friendList.size(); j++) {
                    Friend currFriend = model.fbProfileModel.friendList.get(j);
                    if (currFriend.facebookID == currPoolFriend.facebookID) {
                        currPoolFriend.profilePicture = currFriend.profilePicture;
                        currPoolFriend.firstName = currFriend.firstName;
                        currPoolFriend.lastName = currFriend.lastName;
                        break;
                    }
                }
            } else {
                //It's a famous person id, find it in the famous people list
                for (int j = 0; j < famousPeople.size(); j++) {
                    if (famousPeople.get(j).facebookID == currPoolFriend.facebookID) {
                        currPoolFriend.profilePicture = famousPeople.get(j).profilePicture;
                        currPoolFriend.firstName = famousPeople.get(j).firstName;
                        currPoolFriend.lastName = famousPeople.get(j).lastName;
                        break;
                    }
                }
            }
        }
    }


    private void setUpMutualFriendsList() {
        //Set up the view
        final GridView gridView = (GridView) findViewById(R.id.middle_of_game_gridview);

        //Set up an adapter to hold all the profile pictures
        ImageAdapter imageAdapter = new ImageAdapter(MiddleOfGameController.this, game.opponentPool.mutualFriendList);
        gridView.setAdapter(imageAdapter);

        //Set up the click handler for each of the images
        //TODO: Consider click for gray out and long click for guess
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get the ImageView
                final ImageView selectedImage = (ImageView) v;
                selectedImage.setCropToPadding(true);

                //TODO: Figure out if this is opponentPool or myPool that we want
                final MutualFriend selectedFriend = game.opponentPool.mutualFriendList.get(position);

                //Create a dialog for the user to decide what to do
                AlertDialog.Builder adb = new AlertDialog.Builder(MiddleOfGameController.this);

                //Show different dialogs based on whether or not it is the user's turn
                if (game.isMyTurn) {
                    //Let the user guess the mystery friend, gray out, or exit
                    adb.setTitle("Gray Out Or Select A Friend");
                    adb.setMessage("Would you like to gray out or guess " +
                            selectedFriend.getFullName() + "?");
                    //Keep Gray Out as the "positive" so it is always in the same position
                    adb.setPositiveButton("Gray Out", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Highlight/Unhighlight the friend that was clicked
                            if (selectedFriend.isGrayedOut) {
                                selectedImage.setColorFilter(Color.parseColor("#00000000"));
                                selectedFriend.isGrayedOut = false;
                                NetworkRequestHelper.ungreyFriend(game.ID, selectedFriend.facebookID);
                            } else {
                                selectedImage.setColorFilter(Color.parseColor("#88000000"));
                                selectedFriend.isGrayedOut = true;
                                NetworkRequestHelper.greyFriend(game.ID, selectedFriend.facebookID);
                            }
                        }
                    });
                    adb.setNegativeButton("Guess", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NetworkRequestHelper.guessMysteryFriend(MiddleOfGameController.this, game.ID, selectedFriend.facebookID);
                        }
                    });
                    adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Do Nothing
                        }
                    });

                } else {
                    //Let the user gray out or exit (they can't guess a friend if it's not their turn)
                    adb.setTitle("Gray Out A Friend");
                    adb.setMessage("Would you like to gray out " +
                            selectedFriend.getFullName() + "?");
                    adb.setPositiveButton("Gray Out", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Highlight/Unhighlight the friend that was clicked
                            if (selectedFriend.isGrayedOut) {
                                selectedImage.setColorFilter(Color.parseColor("#00000000"));
                                selectedFriend.isGrayedOut = false;
                                NetworkRequestHelper.ungreyFriend(game.ID, selectedFriend.facebookID);
                            } else {
                                selectedImage.setColorFilter(Color.parseColor("#88000000"));
                                selectedFriend.isGrayedOut = true;
                                NetworkRequestHelper.greyFriend(game.ID, selectedFriend.facebookID);
                            }
                        }
                    });
                    adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Do Nothing
                        }
                    });
                }

                //Display the dialog
                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });

        //Long click listener - guess friend
        //Set up the long click handler for each of the images
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView selectedImage = (ImageView) v;
                selectedImage.setCropToPadding(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(MiddleOfGameController.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.customlayout_mutual_friend_pop_up, null);
                ImageView popUpImage = (ImageView) dialogLayout.findViewById(R.id.mutual_friend_image);
                popUpImage.setImageDrawable(selectedImage.getDrawable());
                TextView popUpName = (TextView) dialogLayout.findViewById(R.id.mutual_friend_name);
                MutualFriend popUpFriend = game.opponentPool.mutualFriendList.get(position);
                popUpName.setText(popUpFriend.getFullName());
                builder.setView(dialogLayout);

                builder.setTitle("Guess " + popUpName.getText() + "?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Guess This Friend", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //do stuff
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    private void getFamousPeopleList() {
        if (famousPeople == null) {
            famousPeople = new ArrayList<MutualFriend>();
            famousPeople.add(new MutualFriend(-2, "Kim", "Kardashian", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Kim_Kardashian_West%2C_Parramatta_Westfield_Sydney_Australia.jpg/200px-Kim_Kardashian_West%2C_Parramatta_Westfield_Sydney_Australia.jpg", false));
            famousPeople.add(new MutualFriend(-3, "Barack", "Obama", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/220px-President_Barack_Obama.jpg", false));
            famousPeople.add(new MutualFriend(-4, "Albert", "Einstein", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Albert_Einstein_Head.jpg/220px-Albert_Einstein_Head.jpg", false));
            famousPeople.add(new MutualFriend(-5, "Beyonce", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Beyonce_Knowles_with_necklaces.jpg/220px-Beyonce_Knowles_with_necklaces.jpg", false));
            famousPeople.add(new MutualFriend(-6, "Harry", "Potter", "https://upload.wikimedia.org/wikipedia/en/thumb/4/44/HarryPotter5poster.jpg/225px-HarryPotter5poster.jpg", false));
            famousPeople.add(new MutualFriend(-7, "Hillary", "Clinton", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Hillary_Clinton_official_Secretary_of_State_portrait_crop.jpg/220px-Hillary_Clinton_official_Secretary_of_State_portrait_crop.jpg", false));
            famousPeople.add(new MutualFriend(-8, "Nicki", "Minaj", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Nicki_Minaj_de_gala_en_las_BET_Awards_2013.png/220px-Nicki_Minaj_de_gala_en_las_BET_Awards_2013.png", false));
            famousPeople.add(new MutualFriend(-9, "SpongeBob", "SquarePants", "https://upload.wikimedia.org/wikipedia/en/thumb/5/5c/Spongebob-squarepants.png/175px-Spongebob-squarepants.png", false));
            famousPeople.add(new MutualFriend(-10, "Patrick", "Star", "https://upload.wikimedia.org/wikipedia/en/thumb/7/7e/Patrick_Star.png/220px-Patrick_Star.png", false));
            famousPeople.add(new MutualFriend(-11, "Bernie", "Sanders", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Bernie_Sanders.jpg/220px-Bernie_Sanders.jpg", false));
            famousPeople.add(new MutualFriend(-12, "Rihanna", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Rihanna_2012_%28Cropped%29.jpg/170px-Rihanna_2012_%28Cropped%29.jpg", false));
            famousPeople.add(new MutualFriend(-13, "Abraham", "Lincoln", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Abraham_Lincoln_November_1863.jpg/220px-Abraham_Lincoln_November_1863.jpg", false));
            famousPeople.add(new MutualFriend(-14, "John F.", "Kennedy", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/John_F._Kennedy%2C_White_House_color_photo_portrait.jpg/225px-John_F._Kennedy%2C_White_House_color_photo_portrait.jpg", false));
            famousPeople.add(new MutualFriend(-15, "Bill", "Clinton", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/44_Bill_Clinton_3x4.jpg/220px-44_Bill_Clinton_3x4.jpg", false));
            famousPeople.add(new MutualFriend(-16, "Oprah", "Winfrey", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Oprah_Winfrey_at_2011_TCA.jpg/220px-Oprah_Winfrey_at_2011_TCA.jpg", false));
            famousPeople.add(new MutualFriend(-17, "Donald", "Trump", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Donald_August_19_%28cropped%29.jpg/220px-Donald_August_19_%28cropped%29.jpg", false));
            famousPeople.add(new MutualFriend(-18, "Justin", "Timberlake", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Justin_Timberlake_Cannes_2013.jpg/220px-Justin_Timberlake_Cannes_2013.jpg", false));
            famousPeople.add(new MutualFriend(-19, "Tom", "Hanks", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Tom_Hanks_2014.jpg/220px-Tom_Hanks_2014.jpg", false));
            famousPeople.add(new MutualFriend(-20, "Will", "Smith", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Will_Smith_2012.jpg/220px-Will_Smith_2012.jpg", false));
            famousPeople.add(new MutualFriend(-21, "Britney", "Spears", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/da/Britney_Spears_2013_%28Straighten_Crop%29.jpg/220px-Britney_Spears_2013_%28Straighten_Crop%29.jpg", false));
        }
    }

    public void onTaskCompleted(String taskName, Object resultModel){
        if(taskName.equals("getGameBoard")) {
            Game fullGame = (Game) resultModel;

            //TODO: Make server return this info too?
            fullGame.ID = game.ID;
            fullGame.opponentID = game.opponentID;
            fullGame.opponentFirstName = game.opponentFirstName;
            fullGame.opponentLastName = game.opponentLastName;
            game = fullGame;
            createprofilePictureUrls();
            setUpMutualFriendsList();

        } else if (taskName.equals("getFriendPool")) {

            //TODO: Get the list of friends

        } else if (taskName.equals("getQuestions")) {

            //TODO: Load previous questions

        } else if(taskName.equalsIgnoreCase("questionSent")){
            EditText theMessage = (EditText) findViewById(R.id.theMessage);
            EditText conversation = (EditText) findViewById(R.id.conversation);
            conversation.append(theMessage.getText() + "\n");
            theMessage.setText("");
        } else if(taskName.equalsIgnoreCase("questionAnswered")) {

        } else if(taskName.equalsIgnoreCase("passedUpMyGuess")) {
            Log.v("Successfully: ", "passed up my guess");
        } else if(taskName.equalsIgnoreCase("myGuessWasWrong")) {

        } else if(taskName.equalsIgnoreCase("iWon")) {

        } else{

        }
    }
}
