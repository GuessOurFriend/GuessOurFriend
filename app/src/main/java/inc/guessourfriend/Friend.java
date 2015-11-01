package inc.guessourfriend;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend {
    private long facebookID;
    private String fullName;
    private String profilePicture;
    private String groups;

    public Friend(long facebookID, String profilePicture, String fullName, String groups){
        this.facebookID = facebookID;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.groups = groups;
    }

    public long getFacebookID(){
        return this.facebookID;
    }
    public String getFullName(){
        return this.fullName;
    }
    public String getProfilePicture(){
        return this.profilePicture;
    }
    public String getGroups(){
        return this.groups;
    }
    public void addToGroup(String groupName){
        // TODO: Complete this function (use regular expressions)
        // think about taking care of database queries in this function
    }
    public void removeFromGroup(String groupName){
        // TODO: Complete this function (use regular expressions)
        // think about taking care of database queries in this function
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Friend))return false;
        Friend friend = (Friend)other;
        if(this.facebookID == friend.facebookID){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) (facebookID ^ (facebookID >>> 32));
    }
}

