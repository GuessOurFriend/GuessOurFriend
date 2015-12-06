package inc.guessourfriend.SupportingClasses;

/**
 * Created by Laura on 11/1/2015.
 */
public class Game {
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
}
