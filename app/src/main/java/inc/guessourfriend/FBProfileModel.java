package inc.guessourfriend;

/**
 * Created by sellmaurer on 10/20/15.
 */
public class FBProfileModel {
    private long facebookID;
    private String fullName;
    private String profilePicture;
    private int[] friendList;

    public FBProfileModel(long facebookID, String fullName, String profilePicture, int[] friendList){
        this.facebookID = facebookID;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.friendList = friendList;
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
    public int[] getFriendList(){
        return this.friendList;
    }

    public void updateFBProfileInfo(long facebookID, String fullName, String profilePicture, int[] friendList){
        this.facebookID = facebookID;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.friendList = friendList;
    }
}
