package inc.guessourfriend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sellmaurer on 10/20/15.
 */
public class FBProfileModel {
    private long facebookID;
    private String fullName;
    private String profilePicture;
    private List<Friend> friendList;

    public FBProfileModel(long facebookID, String fullName, String profilePicture, List<Friend> friendList){
        super();
        updateFBProfileInfo(facebookID, fullName, profilePicture, friendList);
    }

    public long getFacebookID(){
        return this.facebookID;
    }
    public String getProfilePicture(){
        return this.profilePicture;
    }
    public String getFullName(){
        return this.fullName;
    }
    public List<Friend> getFriendList(){
        return this.friendList;
    }

    public void updateFBProfileInfo(long facebookID, String fullName, String profilePicture, List<Friend> friendList){
        this.facebookID = facebookID;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.friendList = friendList;
    }

    public Friend getFriendById(long friendID) {
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).getFacebookID() == friendID) {
                return friendList.get(i);
            }
        }
        return null;
    }
}
