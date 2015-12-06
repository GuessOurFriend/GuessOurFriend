package inc.guessourfriend.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.SupportingClasses.ImageAdapter;
import inc.guessourfriend.Application.Model;
import inc.guessourfriend.SupportingClasses.MutualFriend;
import inc.guessourfriend.SupportingClasses.MutualFriendList;
import inc.guessourfriend.R;

/**
 * Created by Laura on 11/14/2015.
 */
public class StartOfGameController extends SlideNavigationController {

    private Model model;
    private List<MutualFriend> famousPeople;
    private Game game = new Game();
    private int highlightedFriendPos = -1;
    private MutualFriend highlightedFriend = null;
    private MutualFriend currentlySelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_start_of_game_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        //Get the opponentID from the view we just came from (Challenges)
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (!extrasBundle.isEmpty()) {
            boolean hasID = extrasBundle.containsKey("opponentID");
            if (hasID) {
                game.opponentID = extrasBundle.getLong("opponentID");

                //Set up the select mystery friend button
                Button button = (Button) findViewById(R.id.choose_mystery_friend_button);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (highlightedFriend != null) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(StartOfGameController.this);
                            adb.setTitle("Select Mystery Friend");
                            adb.setMessage("Choose " + highlightedFriend.fullName + " as mystery friend?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            currentlySelected = highlightedFriend;
                                            Toast.makeText(getApplicationContext(),
                                                    currentlySelected.fullName + " selected", Toast.LENGTH_SHORT).show();
                                            currentlySelected.isMysteryFriend = true;
                                            //TODO change value in db
                                            game.setStateOfGame(Game.MIDDLE_OF_GAME);
                                            Intent intent = new Intent(StartOfGameController.this, MiddleOfGameController.class);
                                            //startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = adb.create();
                            alertDialog.show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No friend selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Go and set up the new game
                createNewGame();
            }
        }
    }

    private long[] getImagefbids() {
        long[] imagefbids = new long[game.myPool.getMutualFriendList().size()];
        for (int i = 0; i < game.myPool.getMutualFriendList().size(); i++) {
            imagefbids[i] = game.myPool.getMutualFriendList().get(i).facebookID;
        }
        return imagefbids;
    }


    private String[] getImageURLs() {
        String[] imageURLs = new String[game.myPool.getMutualFriendList().size()];
        for (int i = 0; i < game.myPool.getMutualFriendList().size(); i++) {
            imageURLs[i] = game.myPool.getMutualFriendList().get(i).profilePicture;
        }
        return imageURLs;
    }

    private void createNewGame() {
        //Make the call to Facebook to get the mutual friends with this person
        Bundle myBundle = new Bundle();
        myBundle.putString("fields", "context.fields(mutual_friends{id,name,picture})");
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + game.opponentID,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        //Set up a temp collection for all the friends
                        List<MutualFriend> allMutualFriends = new ArrayList<MutualFriend>();
                        game.myPool = new MutualFriendList();
                        game.myPool.mutualFriendList = new ArrayList<MutualFriend>();

                        if (response.getError() != null) {
                            System.out.println("MutualFriend Error" + response.getError().toString());
                        } else {
                            System.out.println("MutualFriend Success");
                            JSONObject json = response.getJSONObject();
                            try {
                                //TODO: Remove debugging info
                                String jsonresult = String.valueOf(json);
                                System.out.println("JSON Result" + jsonresult);

                                //Get the list of mutual friends from the JSON
                                //TODO: Handle paging?
                                JSONArray mutualFriends = json.getJSONObject("context").getJSONObject("mutual_friends").getJSONArray("data");

                                //Loop through the list of mutual friends
                                for (int i = 0; i < mutualFriends.length(); i++) {
                                    JSONObject friend = mutualFriends.getJSONObject(i);
                                    long facebookID = Long.parseLong(friend.getString("id"));
                                    String fullName = friend.getString("name");
                                    String profilePicture = friend.getJSONObject("picture").getJSONObject("data").getString("url");

                                    //Insert this friend into the list
                                    MutualFriend newFriend = new MutualFriend(facebookID, fullName, profilePicture, false);
                                    allMutualFriends.add(newFriend);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Add up to 20 mutual friends and fill the rest with famous people
                            if (allMutualFriends.size() < 20) {
                                int numMutualFriends = allMutualFriends.size();
                                game.myPool.mutualFriendList.addAll(allMutualFriends);
                                game.myPool.mutualFriendList.addAll(getFamousPeople(20 - numMutualFriends));
                            } else {
                                Collections.shuffle(allMutualFriends);
                                for (int i = 0; i < 20; i++) {
                                    game.myPool.mutualFriendList.add(allMutualFriends.get(i));
                                }
                            }

                            //Set up the view
                            final GridView gridView = (GridView) findViewById(R.id.gridview);

                            //Set up an adapter to hold all the profile pictures

                            NetworkRequestHelper.postfriendpools(getImagefbids(),currentlySelected.facebookID);
                            ImageAdapter imageAdapter = new ImageAdapter(StartOfGameController.this, getImageURLs());
                            gridView.setAdapter(imageAdapter);

                            //Set up the click handler for each of the images
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                    //Get the ImageView
                                    ImageView selectedImage = (ImageView) v;
                                    selectedImage.setCropToPadding(true);

                                    //Highlight/Unhighlight the friend that was clicked
                                    if (highlightedFriendPos == position) {
                                        selectedImage.setColorFilter(Color.parseColor("#00000000"));
                                        //selectedImage.setBackgroundColor(Color.parseColor("#80ffffff"));
                                        highlightedFriendPos = -1;
                                        highlightedFriend = null;
                                    } else {
                                        if (highlightedFriend != null) {
                                            //Unhighlight the old highlighted friend
                                            ((ImageView) gridView.getChildAt(highlightedFriendPos)).setColorFilter(Color.parseColor("#00000000"));
                                            //gridView.getChildAt(position).setBackgroundColor(Color.parseColor("#80ffffff"));
                                        }
                                        highlightedFriendPos = position;
                                        highlightedFriend = game.myPool.mutualFriendList.get(position);
                                        selectedImage.setColorFilter(Color.parseColor("#55ffff00"));
                                        //selectedImage.setBackgroundColor(Color.parseColor("#ffff00"));
                                    }
                                }
                            });
                        }
                    }
                }
        );
        request.setParameters(myBundle);
        request.executeAsync();
    }

    private List<MutualFriend> getFamousPeople(int numNeeded) {
        List<MutualFriend> neededFriends = new ArrayList<MutualFriend>();
        if (famousPeople == null) {
            getFamousPeopleList();
        }
        Collections.shuffle(famousPeople);
        for (int i = 0; i < numNeeded; i++) {
            neededFriends.add(famousPeople.get(i));
        }
        return neededFriends;
    }

    private void getFamousPeopleList() {
        if (famousPeople == null) {
            famousPeople = new ArrayList<MutualFriend>();
            famousPeople.add(new MutualFriend(-1, "Kim Kardashian", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Kim_Kardashian_West%2C_Parramatta_Westfield_Sydney_Australia.jpg/200px-Kim_Kardashian_West%2C_Parramatta_Westfield_Sydney_Australia.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Barack Obama", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/220px-President_Barack_Obama.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Albert Einstein", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Albert_Einstein_Head.jpg/220px-Albert_Einstein_Head.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Beyonce", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Beyonce_Knowles_with_necklaces.jpg/220px-Beyonce_Knowles_with_necklaces.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Harry Potter", "https://upload.wikimedia.org/wikipedia/en/thumb/4/44/HarryPotter5poster.jpg/225px-HarryPotter5poster.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Hillary Clinton", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Hillary_Clinton_official_Secretary_of_State_portrait_crop.jpg/220px-Hillary_Clinton_official_Secretary_of_State_portrait_crop.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Nicki Minaj", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Nicki_Minaj_de_gala_en_las_BET_Awards_2013.png/220px-Nicki_Minaj_de_gala_en_las_BET_Awards_2013.png", false));
            famousPeople.add(new MutualFriend(-1, "SpongeBob SquarePants", "https://upload.wikimedia.org/wikipedia/en/thumb/5/5c/Spongebob-squarepants.png/175px-Spongebob-squarepants.png", false));
            famousPeople.add(new MutualFriend(-1, "Patrick Star", "https://upload.wikimedia.org/wikipedia/en/thumb/7/7e/Patrick_Star.png/220px-Patrick_Star.png", false));
            famousPeople.add(new MutualFriend(-1, "Bernie Sanders", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Bernie_Sanders.jpg/220px-Bernie_Sanders.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Rihanna", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Rihanna_2012_%28Cropped%29.jpg/170px-Rihanna_2012_%28Cropped%29.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Abraham Lincoln", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Abraham_Lincoln_November_1863.jpg/220px-Abraham_Lincoln_November_1863.jpg", false));
            famousPeople.add(new MutualFriend(-1, "John F. Kennedy", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/John_F._Kennedy%2C_White_House_color_photo_portrait.jpg/225px-John_F._Kennedy%2C_White_House_color_photo_portrait.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Bill Clinton", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/44_Bill_Clinton_3x4.jpg/220px-44_Bill_Clinton_3x4.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Oprah Winfrey", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Oprah_Winfrey_at_2011_TCA.jpg/220px-Oprah_Winfrey_at_2011_TCA.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Donald Trump", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Donald_August_19_%28cropped%29.jpg/220px-Donald_August_19_%28cropped%29.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Justin Timberlake", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Justin_Timberlake_Cannes_2013.jpg/220px-Justin_Timberlake_Cannes_2013.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Tom Hanks", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Tom_Hanks_2014.jpg/220px-Tom_Hanks_2014.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Will Smith", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Will_Smith_2012.jpg/220px-Will_Smith_2012.jpg", false));
            famousPeople.add(new MutualFriend(-1, "Britney Spears", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/da/Britney_Spears_2013_%28Straighten_Crop%29.jpg/220px-Britney_Spears_2013_%28Straighten_Crop%29.jpg", false));

        }
    }
}
