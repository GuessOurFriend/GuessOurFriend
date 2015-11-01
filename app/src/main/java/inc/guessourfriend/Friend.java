package inc.guessourfriend;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend {
    private long facebookID;
    private String fullName;
    private String profilePicture;

    public Friend(long facebookID, String profilePicture, String fullName, String groups){
        this.facebookID = facebookID;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
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

