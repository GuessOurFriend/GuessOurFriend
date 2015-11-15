package inc.guessourfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 11/1/2015.
 */
public class MutualFriendList {
    public List<MutualFriend> mutualFriendList = null;

    public MutualFriendList(){
        mutualFriendList = null;
    }
    public List<MutualFriend> getMutualFriendList() {
        return mutualFriendList;
    }

    public void populateMutualFriendList(Activity activity) {
        Bundle myBundle = new Bundle();
        myBundle.putString("fields", "context.fields(mutual_friends{id,name,picture})");
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/10154358099544816",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() != null) {
                            System.out.println("MutualFriend Error");
                        } else {
                            System.out.println("MutualFriend Success");
                            JSONObject json = response.getJSONObject();
                            try {
                                //TODO: Remove debugging info
                                String jsonresult = String.valueOf(json);
                                System.out.println("JSON Result" + jsonresult);

                                //New up a list for our view
                                mutualFriendList = new ArrayList<>();

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
                                    mutualFriendList.add(newFriend);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        request.setParameters(myBundle);
        request.executeAsync();
    }
}
