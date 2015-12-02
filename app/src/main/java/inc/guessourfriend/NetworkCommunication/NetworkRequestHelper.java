package inc.guessourfriend.NetworkCommunication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.Models.IncomingChallengeListModel;
import inc.guessourfriend.SQLiteDB.DatabaseHelper;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.SupportingClasses.IncomingChallenge;

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
                    if(result != null){
                        authToken = result.getString("token");
                        String token = authToken;
                    }else{
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

                try{
                    JSONObject jsonResults = jsonResult.getJSONObject("results");
                    JSONArray incomingGames = jsonResults.getJSONArray("incoming_games");
                    JSONArray outgoingGames = jsonResults.getJSONArray("outgoing_games");

                    //Loop through all the games
                    for (int i=0; i < incomingGames.length(); i++) {
                        JSONObject curr = incomingGames.getJSONObject(i);
                        Game temp = new Game();
                        temp.myID = curr.getLong("game_id");
                        temp.opponentID = curr.getLong("fb_id");
                        temp.opponentFirstName = curr.getString("first_name");
                        temp.opponentLastName = curr.getString("last_name");
                        result.add(temp);
                    }

                    for (int i=0; i < outgoingGames.length(); i++) {
                        JSONObject curr = outgoingGames.getJSONObject(i);
                        Game temp = new Game();
                        temp.myID = curr.getLong("game_id");
                        temp.opponentID = curr.getLong("fb_id");
                        temp.opponentFirstName = curr.getString("first_name");
                        temp.opponentLastName = curr.getString("last_name");
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

    //GET /game_board
    public static void getGameBoard(OnTaskCompleted listener, long gameId) {
        new NetworkRequestRunner("GET", ROOT_URL + "/game_board?game_id=" + gameId, getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                JSONObject json = result;
                Log.v("Stupid json: ", result.toString());
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

        new NetworkRequestRunner("POST", ROOT_URL + "/questions", getAuthToken()){
            @Override
            protected void onPostExecute(JSONObject result) {
                String success = "Successfully sent the question";
                String actualResult = "";
                try{
                    actualResult = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(actualResult.equals(success)){
                    theListener.onTaskCompleted("questionSent", null);
                }else{
                    Log.v("questionSent error: ", actualResult);
                }
            }
        }.execute(data);
    }

    //POST /question/answer
    public static void answerQuestion(OnTaskCompleted listener, long gameId, int questionId, int answer) {
        final OnTaskCompleted theListener = listener;
        JSONObject data = new JSONObject();
        try {
            data.put("game_id", gameId);
            data.put("question_id", questionId);
            data.put("answer", answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/question/answer", getAuthToken()){
            @Override
            protected void onPostExecute(JSONObject result) {
                String success = "Successfully answered the question";
                String actualResult = "";
                try{
                    System.out.println(result);
                    if (result.has("errors")) {
                        System.err.println(result.getString("errors"));
                        return;
                    }
                    actualResult = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(actualResult.equals(success)){
                    theListener.onTaskCompleted("questionAnswered", null);
                }else{
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
            data.put("guess_id", guessedFacebookID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/game/guess", getAuthToken()){
            @Override
            protected void onPostExecute(JSONObject result) {
                String passedMessage = "Player has given up the guess opportunity";
                String wonMessage = "You have won the game";
                String guessWasWrongMessage = "Your guess is wrong. Your opponent will be rewarded with two questions";
                String theMessage = "";
                String theError = "";
                try{
                    System.out.println(result);
                    if (result.has("errors")) {
                        System.err.println(result.getString("errors"));
                        return;
                    }
                    theMessage = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(theMessage.equals(passedMessage)){
                    theListener.onTaskCompleted("passedUpMyGuess", null);
                }else if(theMessage.equals(guessWasWrongMessage)){
                    theListener.onTaskCompleted("myGuessWasWrong", null);
                }else{
                    theListener.onTaskCompleted("iWon", null);
                }
            }
        }.execute(data);
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

        new NetworkRequestRunner("POST", ROOT_URL + "/challenges", getAuthToken()){
            /*@Override
            protected void onPostExecute(JSONObject jsonResult) {
                Long challengeid = 1L;
                //Attempt to parse the json
                try {

                        challengeid = jsonResult.getLong("id");

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
    public static void deleteChallengeFromChallengee(long challengeId, long challengerId, boolean accept) {
        JSONObject data = new JSONObject();
        try {
            data.put("challenge_id", challengeId);
            data.put("challenger_id", challengerId);
            data.put("accept", accept);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("DELETE", ROOT_URL + "/challenge/respond_as_challengee", getAuthToken()).execute(data);
    }

    //DELETE challenge/respond_as_challenger
    public static void deleteChallengeFromChallenger(long challengeId, long challengeeId) {
        JSONObject data = new JSONObject();
        try {
            data.put("challenge_id", challengeId);
            data.put("challengee_id", challengeeId);
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
                    for (int i=0; i < jsonArray.length(); i++) {
                        //Parse this challenge's properties
                        JSONObject challenge = jsonArray.getJSONObject(i);
                        long challengeId = challenge.getLong("challenge_id");
                        long challengerId = challenge.getLong("challenger_id");
                        String firstName = challenge.getString("first_name");
                        String lastName = challenge.getString("last_name");
                        long fbId = challenge.getLong("fb_id");

                        //Create and add a challenge
                        IncomingChallenge curr = new IncomingChallenge(challengeId, challengerId, firstName, lastName, fbId);
                        result.IncomingChallengeList.add(curr);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                theListener.onTaskCompleted("getIncomingChallenges", result);
            }
        }.execute();
    }

    //GET user/outgoing_challenges
    public static void getOutgoingChallenges() {
        new NetworkRequestRunner("GET", ROOT_URL + "/user/outgoing_challenges", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                //TODO: Implement
            }
        }.execute();
    }
}
