package inc.guessourfriend;

/**
 * Created by Laura on 11/1/2015.
 */
public class Game {
    private long myID;
    private long opponentID;
    private MutualFriendList myPool;
    private MutualFriendList opponentPool;
    private boolean isMyTurn;
    private int stateOfGame;
    private int numberOfQuestions;

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
        return myID;
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
