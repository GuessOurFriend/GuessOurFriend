package inc.guessourfriend.NetworkCommunication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.Models.IncomingChallengeListModel;
import inc.guessourfriend.Models.OutgoingChallengeListModel;
import inc.guessourfriend.SQLiteDB.DatabaseHelper;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.SupportingClasses.IncomingChallenge;
import inc.guessourfriend.SupportingClasses.MutualFriend;
import inc.guessourfriend.SupportingClasses.MutualFriendList;
import inc.guessourfriend.SupportingClasses.OutgoingChallenge;

public class NetworkRequestHelper {

    private static final String ROOT_URL = "https://guess-our-friend.herokuapp.com";

    private static String getAuthToken() {
        return DatabaseHelper.getCurrentUsersFBProfile(Model.getAppContext()).authToken;
    }

    //////////////////////////////////////////////////
    //Users
    //////////////////////////////////////////////////

    //POST /users
    public static void createUserOnServer(final long facebookID, final String firstName, final String lastName, final String profilePicture) {

        JSONObject data = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            user.put("fb_id", facebookID);
            user.put("gcm_id", "");
            user.put("first_name", firstName);
            user.put("last_name", lastName);
            data.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/users") {
            @Override
            protected void onPostExecute(JSONObject result) {
                //Parse the auth token in the response so we can update this user in the future
                String authToken = null;
                try {
                    if (result != null) {
                        authToken = result.getString("token");
                        String token = authToken;
                    } else {
                        Log.v("Can't get auth token: ", "User already exists");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Insert the FBProfile into the local database
                DatabaseHelper.insertOrUpdateFBProfile(Model.getAppContext(), facebookID, authToken,
                        firstName, lastName, profilePicture);
            }
        }.execute(data);
    }

    //PUT /user/gcm_id
    public static void updateGcmId(String token) {
        JSONObject data = new JSONObject();
        try {
            data.put("gcm_id", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("PUT", ROOT_URL + "/user/gcm_id", getAuthToken()).execute(data);
    }

    //PUT /user/update_rating
    public static void updateRating() {
        JSONObject data = new JSONObject();
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("PUT", ROOT_URL + "/user/update_rating", getAuthToken()).execute(data);
    }

    //PUT /user/update_postmatch
    public static void updateMatches() {
        JSONObject data = new JSONObject();
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("PUT", ROOT_URL + "/user/update_postmatch", getAuthToken()).execute(data);
    }

    //GET /user
    public static void getUser() {
        new NetworkRequestRunner("GET", ROOT_URL + "/user", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                //TODO: Implement
                JSONObject theAuthToken = result;
                System.out.println("The auth token: " + theAuthToken.toString());
            }
        }.execute();
    }

    //DELETE /user
    public static void deleteUser() {
        new NetworkRequestRunner("DELETE", ROOT_URL + "/user", getAuthToken()).execute();
    }

    //////////////////////////////////////////////////
    //Bug Reports
    //////////////////////////////////////////////////

    //POST /bug_reports
    public static void sendBugReport(String title, String message) {
        JSONObject data = new JSONObject();
        try {
            data.put("title", title);
            data.put("content", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/bug_reports", getAuthToken()).execute(data);
    }

    //////////////////////////////////////////////////
    //Game Info
    //////////////////////////////////////////////////

    //GET /user/all_games
    public static void getAllGames(final OnTaskCompleted theListener) {
        new NetworkRequestRunner("GET", ROOT_URL + "/user/all_games", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject jsonResult) {
                ArrayList<Game> result = new ArrayList<Game>();
                System.out.println(jsonResult);
                try {
                    JSONObject jsonResults = jsonResult.getJSONObject("results");
                    JSONArray incomingGames = jsonResults.getJSONArray("incoming_games");
                    JSONArray outgoingGames = jsonResults.getJSONArray("outgoing_games");

                    //Loop through all the games
                    for (int i = 0; i < incomingGames.length(); i++) {
                        JSONObject curr = incomingGames.getJSONObject(i);
                        Game temp = new Game();
                        temp.ID = Long.parseLong(curr.getString("game_id"));
                        temp.opponentID = Long.parseLong(curr.getString("fb_id"));
                        temp.opponentFirstName = curr.getString("first_name");
                        temp.opponentLastName = curr.getString("last_name");
                        temp.stateOfGame = curr.getInt("state");
                        result.add(temp);
                    }

                    for (int i = 0; i < outgoingGames.length(); i++) {
                        JSONObject curr = outgoingGames.getJSONObject(i);
                        Game temp = new Game();
                        temp.ID = Long.parseLong(curr.getString("game_id"));
                        temp.opponentID = Long.parseLong(curr.getString("fb_id"));
                        temp.opponentFirstName = curr.getString("first_name");
                        temp.opponentLastName = curr.getString("last_name");
                        temp.stateOfGame = curr.getInt("state");
                        result.add(temp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(result);
                theListener.onTaskCompleted("gamesLoaded", result);
            }
        }.execute();
    }

    public static String intAnswerToString(int answer) {
        switch (answer) {
            case 0:
                return "No";
            case 1:
                return "Yes";
            case 2:
                return "Not Sure";
            default:
                return "";
        }
    }

    //TODO: Check with Brian, if we can get the imageURLS along with friendlist in getGameBooard
    //TODO: Also the query currently returns a null object
    //GET /game_board
    public static void getGameBoard(final OnTaskCompleted theListener, final long gameId) {
        new NetworkRequestRunner("GET", ROOT_URL + "/game_board?game_id=" + gameId, getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject jsonResult) {
                Game gameResult = new Game();
                gameResult.ID = gameId;
                gameResult.opponentPool = new MutualFriendList();
                gameResult.opponentPool.mutualFriendList = new ArrayList<MutualFriend>();
                gameResult.myPool = new MutualFriendList();
                gameResult.myPool.mutualFriendList = new ArrayList<MutualFriend>();

                JSONObject json = jsonResult;
                Log.v("Stupid json: ", jsonResult.toString());

                try {
                    JSONObject resultObject = json.getJSONObject("results");
                    JSONObject questions = resultObject.getJSONObject("questions");
                    JSONArray outgoingQuestions = questions.getJSONArray("outgoing_questions");
                    JSONArray incomingQuestions = questions.getJSONArray("incoming_questions");
                    JSONObject friendsList = resultObject.getJSONObject("friend_list");
                    JSONArray outgoingFriendsList = friendsList.getJSONArray("outgoing_list");
                    JSONArray incomingFriendsList = friendsList.getJSONArray("incoming_list");

                    gameResult.typeOfTurn = Game.TypeOfTurn.fromInt(Integer.parseInt(resultObject.getString("your_turn")));

                    if (!resultObject.isNull("mystery_friend")) {
                        JSONObject mysteryFriend = resultObject.getJSONObject("mystery_friend");
                        gameResult.mysteryFriendId = Long.parseLong(mysteryFriend.getString("fb_id"));
                    }

                    //Create the conversations (loop to the lower of the two lengths)
                    gameResult.conversation = new ArrayList<>();
                    int incomingQuestionIndex = 0;
                    int incomingQuestionLength = incomingQuestions.length();
                    int outgoingQuestionIndex = 0;
                    int outgoingQuestionLength = outgoingQuestions.length();

                    //Create a TreeMap to sort by the question ids for us
                    TreeMap<Integer, String> sortedConvo = new TreeMap<>();

                    for (int i = 0; i < incomingQuestionLength; i++) {
                        JSONObject incomingQuestion = incomingQuestions.getJSONObject(incomingQuestionIndex++);
                        int incomingQuestionId = Integer.parseInt(incomingQuestion.getString("id"));
                        String incomingQuestionContent = incomingQuestion.getString("content");
                        int incomingQuestionAnswer = Integer.parseInt(incomingQuestion.getString("answer"));

                        sortedConvo.put(incomingQuestionId,
                                "Them: " + incomingQuestionContent + ((incomingQuestionAnswer != -1) ? "\nYou: " : "") +
                                        intAnswerToString(incomingQuestionAnswer));
                    }

                    for (int i = 0; i < outgoingQuestionLength; i++) {
                        JSONObject outgoingQuestion = outgoingQuestions.getJSONObject(outgoingQuestionIndex++);
                        int outgoingQuestionId = Integer.parseInt(outgoingQuestion.getString("id"));
                        String outgoingQuestionContent = outgoingQuestion.getString("content");
                        int outgoingQuestionAnswer = Integer.parseInt(outgoingQuestion.getString("answer"));

                        sortedConvo.put(outgoingQuestionId,
                                "You: " + outgoingQuestionContent + ((outgoingQuestionAnswer != -1) ? "\nThem: " : "") +
                                        intAnswerToString(outgoingQuestionAnswer));
                    }

                    for (Map.Entry<Integer, String> convoEntry : sortedConvo.entrySet()) {
                        gameResult.conversation.add(convoEntry.getValue());
                        gameResult.lastQuestionId = convoEntry.getKey();
                    }

                    for (int i = 0; i < incomingFriendsList.length(); i++) {
                        JSONObject curr = incomingFriendsList.getJSONObject(i);
                        Long fbId = Long.parseLong(curr.getString("fb_id"));
                        boolean isGrayedOut = curr.getBoolean("grey");

                        MutualFriend temp = new MutualFriend();
                        temp.facebookID = fbId;
                        temp.isGrayedOut = isGrayedOut;
                        gameResult.myPool.mutualFriendList.add(temp);
                    }

                    for (int i = 0; i < outgoingFriendsList.length(); i++) {
                        JSONObject curr = outgoingFriendsList.getJSONObject(i);
                        Long fbId = Long.parseLong(curr.getString("fb_id"));
                        boolean isGrayedOut = curr.getBoolean("grey");

                        MutualFriend temp = new MutualFriend();
                        temp.facebookID = fbId;
                        temp.isGrayedOut = isGrayedOut;
                        gameResult.opponentPool.mutualFriendList.add(temp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                theListener.onTaskCompleted("getGameBoard", gameResult);
                //theListener.onTaskCompleted("getQuestions", result);
                //theListener.onTaskCompleted("getFriendPool", result);
            }
        }.execute();
    }

    //GET /game_board
    public static void getGameObject(final OnTaskCompleted theListener, final long gameId) {
        new NetworkRequestRunner("GET", ROOT_URL + "/game_board?game_id=" + gameId, getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject jsonResult) {
                JSONObject json = jsonResult;
                Log.v("Game object: ", jsonResult.toString());
            }
        }.execute();
    }

    //GET /game/reveal
    public static void getOpponentMysteryId(final OnTaskCompleted theListener, final long gameId) {
        new NetworkRequestRunner("GET", ROOT_URL + "/game/reveal?game_id=" + gameId, getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject jsonResult) {
                Long fbId = -1L;
                try {
                    fbId = Long.parseLong(jsonResult.getString("mystery_friend"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("Opponent mystery id: ", jsonResult.toString());
                theListener.onTaskCompleted("opponentMysteryId", fbId);
            }
        }.execute();
    }

    //POST /questions
    public static void sendQuestion(OnTaskCompleted listener, long gameId, String question) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            data.put("content", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/questions", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                String success = "Successfully sent the question";
                String actualResult = "";
                try {
                    actualResult = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (actualResult.equals(success)) {
                    theListener.onTaskCompleted("questionSent", null);
                } else {
                    Log.v("questionSent error: ", actualResult);
                }
            }
        }.execute(data);
    }

    //POST /question/answer
    public static void answerQuestion(OnTaskCompleted listener, long gameId, int questionId, int answer) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        Long id = gameId;
        int questionID = questionId;
        int answerNumber = answer;
        String mes = "";
        Log.v("blah", "" + id + questionID + answerNumber);
        try {
            data.put("game_id", gameId);
            data.put("question_id", questionId);
            data.put("answer", answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/question/answer", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                JSONObject result2 = result;
                Log.v("result", result2.toString());
                String success = "Successfully answered the question";
                String actualResult = "";
                try {
                    System.out.println(result);
                    if (result.has("errors")) {
                        System.err.println(result.getString("errors"));
                        return;
                    }
                    actualResult = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (actualResult.equals(success)) {
                    theListener.onTaskCompleted("questionAnswered", null);
                } else {
                    Log.v("qAnswered error: ", actualResult);
                }
            }
        }.execute(data);
    }

    //POST /game/guess
    public static void guessMysteryFriend(OnTaskCompleted listener, long gameId, long guessedFacebookID) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            data.put("guess_fb_id", guessedFacebookID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/game/guess", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                String passedMessage = "Player has given up the guess opportunity";
                String wonMessage = "You have won the game.";
                String guessWasWrongMessage = "Your guess is wrong. Your opponent will be rewarded with two questions";
                String theMessage = "";
                String theError = "";
                try {
                    System.out.println(result);
                    if (result.has("errors")) {
                        System.err.println(result.getString("errors"));
                        return;
                    }
                    theMessage = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (theMessage.equals(passedMessage)) {
                    theListener.onTaskCompleted("passedUpMyGuess", null);
                } else if (theMessage.equals(guessWasWrongMessage)) {
                    theListener.onTaskCompleted("myGuessWasWrong", null);
                } else if (theMessage.equals(wonMessage)){
                    theListener.onTaskCompleted("iWon", null);
                }
            }
        }.execute(data);
    }

    //PUT /friend_pool/grey
    public static void greyFriend(long gameId, long fbId) {
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            data.put("fb_id", fbId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("PUT", ROOT_URL + "/friend_pool/grey", getAuthToken()).execute(data);
    }

    //PUT /friend_pool/ungrey
    public static void ungreyFriend(long gameId, long fbId) {
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            data.put("fb_id", fbId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("PUT", ROOT_URL + "/friend_pool/ungrey", getAuthToken()).execute(data);
    }

    //PUT /game/player_quit
    public static void quitGame(long gameId) {
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("PUT", ROOT_URL + "/game/player_quit", getAuthToken()).execute(data);
    }

    //////////////////////////////////////////////////
    //Challenges
    //////////////////////////////////////////////////

    //POST /challenges
    public static void sendChallenge(/*OnTaskCompleted listener,*/ final String challengeeId) {
        //final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("challengee_fb_id", challengeeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/challenges", getAuthToken()) {
            /*@Override
            protected void onPostExecute(JSONObject jsonResult) {
                Long challengeid = 1L;
                //Attempt to parse the json
                try {

                        challengeid = Long.parseLong(jsonResult.getString("id"));

                }catch (JSONException e){
                    e.printStackTrace();
                }
                String a = new String();
                a= challengeid.toString();
                theListener.onTaskCompleted(a, null);
            }*/


        }.execute(data);
    }

    //DELETE challenge/respond_as_challengee
    public static void deleteChallengeFromChallengee(OnTaskCompleted listener, long challengeId, long challengerId, boolean accept) {
        final OnTaskCompleted theListener = listener;
        final boolean acceptedTheChallenge = accept;
        JSONObject data = new JSONObject();
        try {
            data.put("challenge_id", challengeId);
            data.put("challenger_id", challengerId);
            data.put("accept", accept);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("DELETE", ROOT_URL + "/challenge/respond_as_challengee", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (acceptedTheChallenge) {
                    HashMap<String, Long> gameData = new HashMap<String, Long>();
                    try {
                        Long gameID = Long.parseLong(result.getString("id"));
                        Long challengeeID = Long.parseLong(result.getString("player2id"));
                        gameData.put("gameID", gameID);
                        gameData.put("challengeeID", challengeeID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    theListener.onTaskCompleted("challenge accepted", gameData);
                }
            }
        }.execute(data);
    }

    //DELETE challenge/respond_as_challenger
    public static void deleteChallengeFromChallenger(long challengeeId) {
        JSONObject data = new JSONObject();
        try {
            data.put("challengee_fb_id", challengeeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("DELETE", ROOT_URL + "/challenge/respond_as_challenger", getAuthToken()).execute(data);
    }

    //GET user/incoming_challenges
    public static void getIncomingChallenges(OnTaskCompleted listener) {
        final OnTaskCompleted theListener = listener;
        new NetworkRequestRunner("GET", ROOT_URL + "/user/incoming_challenges", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject jsonResult) {
                //Create the challenge list
                IncomingChallengeListModel result = new IncomingChallengeListModel();

                //Attempt to parse the json
                try {
                    JSONArray jsonArray = jsonResult.getJSONArray("results");

                    //Loop through all the challenges
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Parse this challenge's properties
                        JSONObject challenge = jsonArray.getJSONObject(i);
                        long challengeId = Long.parseLong(challenge.getString("challenge_id"));
                        long challengerId = Long.parseLong(challenge.getString("challenger_id"));
                        String firstName = challenge.getString("first_name");
                        String lastName = challenge.getString("last_name");
                        long fbId = Long.parseLong(challenge.getString("fb_id"));

                        //Create and add a challenge
                        IncomingChallenge curr = new IncomingChallenge(challengeId, challengerId, firstName, lastName, fbId);
                        result.IncomingChallengeList.add(curr);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                theListener.onTaskCompleted("getIncomingChallenges", result);
            }
        }.execute();
    }

    //GET user/outgoing_challenges
    public static void getOutgoingChallenges(OnTaskCompleted listener) {
        final OnTaskCompleted theListener = listener;
        new NetworkRequestRunner("GET", ROOT_URL + "/user/outgoing_challenges", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject jsonResult) {
                //Create the challenge list
                OutgoingChallengeListModel result = new OutgoingChallengeListModel();

                //Attempt to parse the json
                try {
                    JSONArray jsonArray = jsonResult.getJSONArray("results");

                    //Loop through all the challenges
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Parse this challenge's properties

                        JSONObject challenge = jsonArray.getJSONObject(i);
                        /*   Extra Fields : TODO: Discuss with Brian to simplify this
                        long challengeId = Long.parseLong(challenge.getString("challenge_id"));
                        long challengeeId = Long.parseLong(challenge.getString("challengee_id"));
                        String firstName = challenge.getString("first_name");
                        String lastName = challenge.getString("last_name");
                        */
                        long fbId = Long.parseLong(challenge.getString("fb_id"));

                        //Create and add a challenge
                        OutgoingChallenge curr = new OutgoingChallenge(fbId);
                        result.outgoingChallengeList.add(curr);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                theListener.onTaskCompleted("getOutgoingChallenges", result);
            }
        }.execute();
    }

    //POST /game/match_making
    public static void enterMatchmaking(OnTaskCompleted listener) {
        final OnTaskCompleted theListener = listener;
        // Friend list needs to be sent to the server in order for the server to tell us if any
        //      of our friends are currently in matchmaking. If any of our friends are found in
        //      matchmaking, the server will create a game with the friend that has been in the
        //      matchmaking queue the longest and return us that game object.
        ArrayList<Long> listOfFriendIDs = DatabaseHelper.getListOfFriendIDs(Model.getAppContext());
        JSONArray friendIDs = new JSONArray(listOfFriendIDs);
        JSONObject friendIDJSONObject = new JSONObject();
        try {
            friendIDJSONObject.put("friends", friendIDs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/game/match_making", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                String message = "";
                String noFriendAvailableMessage = "There are no available friends in the " +
                        "matchmaking pool. You have been placed in the pool.";
                String alreadyInMatchmaking = "You are already in the matchmaking pool.";
                try {
                    if (result.has("errors")) {
                        Log.v("Matchmaking error: ", result.getString("errors"));
                        return;
                    }
                    message = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (message.equals(noFriendAvailableMessage)) {
                    theListener.onTaskCompleted("entered matchmaking queue", null);
                } else if (message.equals(alreadyInMatchmaking)) {
                    theListener.onTaskCompleted("already in matchmaking", null);
                } else {
                    JSONObject gameJSONObject = null;
                    Game game = new Game();
                    try {
                        gameJSONObject = result.getJSONObject("game");
                        game.ID = Long.parseLong(gameJSONObject.getString("id"));
                        game.opponentID = Long.parseLong(gameJSONObject.getString("player1id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    theListener.onTaskCompleted("found a game", game);
                }
            }
        }.execute(friendIDJSONObject);
    }

    // PUT /game/remove_from_match_making
    public static void leaveMatchmaking(OnTaskCompleted listener) {
        final OnTaskCompleted theListener = listener;
        new NetworkRequestRunner("PUT", ROOT_URL + "/game/remove_from_match_making", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                JSONObject result2 = result;
                String success = "Successfully removed yourself from the matchmaking pool.";
                try {
                    if (result.has("errors")) {
                        Log.v("Matchmaking error: ", result.getString("errors"));
                        return;
                    }
                    if (result.getString("message").equals(success)) {
                        theListener.onTaskCompleted("left the matchmaking queue", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    // GET /game/check_match_making
    public static void checkIfAlreadyInMatchmaking(OnTaskCompleted listener) {
        final OnTaskCompleted theListener = listener;
        new NetworkRequestRunner("GET", ROOT_URL + "/game/check_match_making", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                JSONObject result2 = result;
                String alreadyInMatchmaking = "true";
                try {
                    if (result.has("errors")) {
                        Log.v("inMatchmaking error: ", result.getString("errors"));
                        return;
                    }
                    if (result.getString("results").equals(alreadyInMatchmaking)) {
                        theListener.onTaskCompleted("already in matchmaking", null);
                    } else {
                        theListener.onTaskCompleted("not in matchmaking", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    //POST /friend_pools
    public static void postFriendPools(long gameId, long[] friendIds) {
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            JSONArray jsonFriendIds = new JSONArray();
            for (long fbId : friendIds) {
                JSONObject temp = new JSONObject();
                temp.put("fb_id", fbId);
                jsonFriendIds.put(temp);
            }
            data.put("friend_pool", jsonFriendIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/friend_pools", getAuthToken()).execute(data);
    }

    //POST /friend_pools/set_mystery_friend
    public static void setMysteryFriend(OnTaskCompleted listener, long gameId, long mysteryFriendId) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            data.put("mystery_friend", mysteryFriendId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/friend_pools/set_mystery_friend", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                JSONObject result2 = result;
                String alreadyInMatchmaking = "true";
                try {
                    if (result.has("errors")) {
                        Log.v("Error_setmysteryfriend ", result.getString("errors"));
                        return;
                    }
                    JSONObject game = result.getJSONObject("game");
                    Long mysteryFriend1 = Long.parseLong(game.getString("mystery_friend1"));
                    Long mysteryFriend2 = Long.parseLong(game.getString("mystery_friend2"));
                    if (mysteryFriend1 == -1 || mysteryFriend2 == -1) {
                        theListener.onTaskCompleted("waiting for other player to choose mystery friend", null);
                    } else {
                        theListener.onTaskCompleted("both players have chosen a friend mystery friend", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(data);

    }


    //PUT game/set_done
    public static void sendDone(OnTaskCompleted listener, long gameId) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new NetworkRequestRunner("PUT", ROOT_URL + "/game/set_done", getAuthToken()){
            protected void onPostExecute(JSONObject result) {
                JSONObject result2 = result;
                try {
                    if (result.has("errors")) {
                        Log.v("Error_setDone", result.getString("errors"));
                        return;
                    }
                    Long state = Long.parseLong(result2.getString("state"));

                    theListener.onTaskCompleted("Game state after end",state);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute(data);
    }

    //PUT game/set_rematch
    public static void sendRematch(OnTaskCompleted listener,long gameId) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new NetworkRequestRunner("PUT", ROOT_URL + "/game/set_rematch", getAuthToken()){
            protected void onPostExecute(JSONObject result) {
                JSONObject result2 = result;
                try {
                    if (result.has("errors")) {
                        Log.v("Error_setDone", result.getString("errors"));
                        return;
                    }
                    Long state = Long.parseLong(result2.getString("state"));

                    if (state != 1){
                    theListener.onTaskCompleted("Game state after end",state);}
                    else if (state == 1){
                        HashMap<String, String> returnData = new HashMap<String, String>();
                        try {
                            Long gameID = Long.parseLong(result2.getString("game_id"));
                            Long opponentID = Long.parseLong(result2.getString("opponent_id"));
                            String opponent_first_name = result2.getString("opponent_first_name");
                            String opponent_last_name = result2.getString("opponent_last_name");

                            returnData.put("game_id", gameID.toString());
                            returnData.put("opponent_id", opponentID.toString());
                            returnData.put("opponent_first_name", opponent_first_name);
                            returnData.put("opponent_last_name", opponent_last_name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        theListener.onTaskCompleted("Game state after rematch",returnData);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(data);
    }




}
