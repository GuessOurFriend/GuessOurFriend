package inc.guessourfriend.SupportingClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Laura on 11/1/2015.
 */
public class Game implements Parcelable {
    public long ID;
    public long opponentID;
    public String opponentFirstName;
    public String opponentLastName;
    public long mysteryFriendId;
    public MutualFriendList myPool;
    public MutualFriendList opponentPool;
    public boolean isMyTurn;
    public int stateOfGame;
    public int numberOfQuestions;
    public ArrayList<String> conversation;
    public int lastQuestionId;
    public static final int START_OF_GAME = 0;
    public static final int MIDDLE_OF_GAME = 1;
    public static final int END_OF_GAME = 2;

    public Game(){
        super();
        this.ID = -1;
        this.opponentID = -1;
        this.mysteryFriendId = -1;
        this.myPool = null;
        this.opponentPool = null;
        this.isMyTurn = false;
        this.stateOfGame = START_OF_GAME;
        this.numberOfQuestions = -1;
    }

    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setStateOfGame(int stateOfGame) {
        this.stateOfGame = stateOfGame;
    }

    public long getMyId() {
        return ID;
    }

    public long getOpponentId() {
        return opponentID;
    }

    public MutualFriendList getMyPool() {
        return myPool;
    }

    public MutualFriendList getOpponentPool() {
        return opponentPool;
    }

    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getStateOfGame() {
        return stateOfGame;
    }

    private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(ID);
        out.writeLong(opponentID);
        out.writeString(opponentFirstName);
        out.writeString(opponentLastName);
        out.writeLong(mysteryFriendId);
        out.writeInt(stateOfGame);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Game(Parcel in) {

        ID = in.readLong();
        opponentID = in.readLong();
        opponentFirstName = in.readString();
        opponentLastName = in.readString();
        mysteryFriendId = in.readLong();
        stateOfGame = in.readInt();
    }
}
