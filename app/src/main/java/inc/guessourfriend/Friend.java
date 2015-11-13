package inc.guessourfriend;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by sellmaurer on 10/31/15.
 */

public class Friend {
    private long facebookID;
    private String fullName;
    private String profilePicture;
    private boolean checked = false ; //Manav: I added this field along with some methods to keep track if the friend is backlisted.

    public Friend(long facebookID, String fullName, String profilePicture){
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
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public void toggleChecked() {
        checked = !checked ;
    }



    @Override
    public int hashCode() {
        return (int) (facebookID ^ (facebookID >>> 32));
    }
}

