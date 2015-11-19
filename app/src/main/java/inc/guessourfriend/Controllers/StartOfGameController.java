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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private int highlighted = -1;
    private ImageView previouslySelected = null;

    private String[] getImageURLs() {
        String[] imageURLs = new String[game.myPool.getMutualFriendList().size()];
        for (int i = 0; i < game.myPool.getMutualFriendList().size(); i++) {
            imageURLs[i] = game.myPool.getMutualFriendList().get(i).profilePicture;
        }
        return imageURLs;
    }

    private MutualFriendList generatePool(long opponentID) {
        MutualFriendList poolMFL = new MutualFriendList();
        List<MutualFriend> newPool = new ArrayList<MutualFriend>();
        MutualFriendList mutualFriends = new MutualFriendList();
        mutualFriends.populateMutualFriendList(this); // give opponentID (?)
        if (mutualFriends.mutualFriendList.size() < 20) {
            newPool = mutualFriends.mutualFriendList;
            newPool.addAll(getFamousPeople(20 - mutualFriends.mutualFriendList.size()));
        } else {
            Collections.shuffle(mutualFriends.mutualFriendList);
            for (int i = 0; i < 20; i++) {
                newPool.add(mutualFriends.mutualFriendList.get(i));
            }
        }
        poolMFL.mutualFriendList = newPool;
        return poolMFL;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_start_of_game_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (!extrasBundle.isEmpty()) {
            boolean hasLong = extrasBundle.containsKey("opponentID");
            if (hasLong) {
                game.opponentID = extrasBundle.getLong("opponentID");
            }
        }

        //TODO currently pool is only famous people (populateMutualFriendList in MutualFriendListModel)
        final MutualFriendList pool1 = new MutualFriendList();
        pool1.mutualFriendList = getFamousPeople(20);
//        MutualFriendList pool1 = generatePool(game.opponentID);

        game.myPool = pool1;

        GridView gridView = (GridView) findViewById(R.id.gridview);

        final ImageAdapter imageAdapter = new ImageAdapter(this, getImageURLs());
        gridView.setAdapter(imageAdapter);

//        gridView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(StartOfGameController.this, "" + position + " hold", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final int pos = position;
                ImageView selectedImage = (ImageView) v;
                selectedImage.setCropToPadding(true);
                if (highlighted == position) {
                    selectedImage.setBackgroundColor(Color.parseColor("#80ffffff"));
                    highlighted = -1;
                    previouslySelected = null;
                } else {
                    if (previouslySelected != null) {
                        previouslySelected.setBackgroundColor(Color.parseColor("#80ffffff"));
                    }
                    previouslySelected = selectedImage;
                    highlighted = position;
                    selectedImage.setBackgroundColor(Color.parseColor("#ffff00"));
                }

                Button button = (Button) findViewById(R.id.choose_mystery_friend_button);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (highlighted != -1) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(StartOfGameController.this);
                            adb.setTitle("Select Mystery Friend");
                            adb.setMessage("Choose " + pool1.mutualFriendList.get(pos).fullName + " as mystery friend?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Toast.makeText(getApplicationContext(),
                                                    pool1.mutualFriendList.get(pos).fullName + " selected", Toast.LENGTH_SHORT).show();
                                            pool1.mutualFriendList.get(pos).isMysteryFriend = true;
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
            }
        });
        Button button = (Button) findViewById(R.id.choose_mystery_friend_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (highlighted == -1) {
                    Toast.makeText(getApplicationContext(),
                            "No friend selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
