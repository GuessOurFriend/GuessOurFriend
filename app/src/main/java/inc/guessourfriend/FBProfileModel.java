package inc.guessourfriend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sellmaurer on 10/20/15.
 */
public class FBProfileModel {
    public long facebookID;
    public String authToken;
    public String firstName;
    public String lastName;
    public String profilePicture;
    public ArrayList<Friend> friendList;

    public FBProfileModel(){
        super();
        updateFBProfileInfo(-1, "", "", "", null);
    }

    public FBProfileModel(long facebookID, String authToken, String fullName, String profilePicture, ArrayList<Friend> friendList){
        super();
        updateFBProfileInfo(facebookID, firstName, lastName, profilePicture, friendList);
        this.authToken = authToken;
    }

    public void updateFBProfileInfo(long facebookID, String firstName, String lastName, String profilePicture, ArrayList<Friend> friendList){
        this.facebookID = facebookID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.friendList = friendList;
    }

    public Friend getFriendById(long friendID) {
        if(friendList != null) {
            for (int i = 0; i < friendList.size(); i++) {
                if (friendList.get(i).getFacebookID() == friendID) {
                    return friendList.get(i);
                }
            }
        }
        return null;
    }
}