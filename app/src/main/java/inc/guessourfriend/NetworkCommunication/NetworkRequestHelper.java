package inc.guessourfriend.NetworkCommunication;

import org.json.JSONException;
import org.json.JSONObject;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.SQLiteDB.DatabaseHelper;

public class NetworkRequestHelper {

    private static final String ROOT_URL = "https://guess-our-friend.herokuapp.com";

    private static String getAuthToken() {
        return DatabaseHelper.getFBProfile(Model.getAppContext()).authToken;
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
                    authToken = result.getString("token");
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
    public static void getAllGames() {
        new NetworkRequestRunner("GET", ROOT_URL + "/user/all_games", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                //TODO: Implement
            }
        }.execute();
    }

    //GET /game_board
    public static void getGameBoard(int gameId) {
        new NetworkRequestRunner("GET", ROOT_URL + "/game_board?game_id=" + gameId, getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                //TODO: Implement
            }
        }.execute();
    }

    //////////////////////////////////////////////////
    //Challenges
    //////////////////////////////////////////////////

    //POST /challenges
    public static void sendChallenge(String challengeeId) {
        JSONObject data = new JSONObject();
        try {
            data.put("challengee_fb_id", challengeeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new NetworkRequestRunner("POST", ROOT_URL + "/challenges", getAuthToken()).execute(data);
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
    public static void getIncomingChallenges() {
        new NetworkRequestRunner("GET", ROOT_URL + "/user/incoming_challenges", getAuthToken()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                //TODO: Implement
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
