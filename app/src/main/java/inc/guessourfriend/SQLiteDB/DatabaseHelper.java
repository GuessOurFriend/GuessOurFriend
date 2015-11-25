package inc.guessourfriend.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import inc.guessourfriend.Models.FBProfileModel;
import inc.guessourfriend.SupportingClasses.Friend;

/**
 * Created by sellmaurer on 10/19/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "User Info";
    private static final String FBPROFILE_TABLE = "FBProfileTable";
    private static final String FACEBOOKIDBLACKLISTPAIR_TABLE = "FacebookIDBlacklistPairTable";
    private static final String FRIEND_TABLE = "FriendTable";
    private static final String CURRENTUSER_TABLE = "CurrentUserTable";
    //private static final String CHALLENGES_TABLE = "ChallengesTable";
    // authToken is the unique user token needed to make queries to our Ruby on Rails Heroku server
    private static final String CREATE_FBPROFILE_TABLE_QUERY = "CREATE TABLE " + FBPROFILE_TABLE +
            " (facebookID INTEGER PRIMARY KEY, " + "authToken TEXT, " + "firstName TEXT, " +
            "lastName TEXT, " + "profilePicture TEXT);";
    private static final String CREATE_FRIEND_TABLE_QUERY = "CREATE TABLE " + FRIEND_TABLE +
            " (userID INTEGER, friendID INTEGER, firstName TEXT, lastName TEXT, profilePicture TEXT, " +
            "isBlacklisted INTEGER, PRIMARY KEY (userID, friendID));";
    private static final String CREATE_CURRENTUSER_TABLE_QUERY = "CREATE TABLE " + CURRENTUSER_TABLE +
            " (facebookID INTEGER PRIMARY KEY);";
    /*private static final String CREATE_FACEBOOKIDBLACKLISTPAIR_TABLE_QUERY = "CREATE TABLE " + FACEBOOKIDBLACKLISTPAIR_TABLE +
                " (facebookID INTEGER PRIMARY KEY, " + "isBlacklisted INTEGER);";*/

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FBPROFILE_TABLE_QUERY);
        //db.execSQL(CREATE_FACEBOOKIDBLACKLISTPAIR_TABLE_QUERY);
        db.execSQL(CREATE_FRIEND_TABLE_QUERY);
        db.execSQL(CREATE_CURRENTUSER_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + FBPROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FACEBOOKIDBLACKLISTPAIR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CURRENTUSER_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + CHALLENGES_TABLE);
        onCreate(db);
    }

    /* This method updates who the current user is */
    public static long updateCurrentUser(Context context, long facebookID){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // delete the last current user
        db.delete(CURRENTUSER_TABLE, null, new String[]{});
        // insert the new current user
        ContentValues row = new ContentValues();
        row.put("facebookID", facebookID);
        long id = db.insertWithOnConflict(CURRENTUSER_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public static long getCurrentUser(Context context)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + CURRENTUSER_TABLE, new String[]{});

        long currentUserID = -1;
        if (cur.moveToNext()) {
            currentUserID = cur.getLong(cur.getColumnIndex("facebookID"));
        }
        db.close();
        return currentUserID;
    }

    /* This method creates an FBProfileModel in the SQLite database for the user */
    public static long insertOrUpdateFBProfile(Context context, long facebookID, String authToken,
                                               String firstName, String lastName, String profilePicture){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("facebookID", facebookID);
        row.put("authToken", authToken);
        row.put("firstName", firstName);
        row.put("lastName", lastName);
        row.put("profilePicture", profilePicture);
        long id = db.insertWithOnConflict(FBPROFILE_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public static long deleteFBProfile(Context context){
        long facebookID = DatabaseHelper.getCurrentUser(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FBPROFILE_TABLE, "facebookID=?", new String[]{String.valueOf(facebookID)});
        db.close();
        return id;
    }

    public static long deleteFBProfiles(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FBPROFILE_TABLE, null, new String[]{});
        db.close();
        return id;
    }

    public static FBProfileModel getFBProfile(Context context)
    {
        long facebookID = DatabaseHelper.getCurrentUser(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FBPROFILE_TABLE + " WHERE facebookID = ?",
                new String[]{Long.toString(facebookID)});

        //Declare a profile to return
        FBProfileModel profile = null;

        //If we have a profile in the table matching the facebookID, get it
        if (cur.moveToNext()) {
            String authToken = cur.getString(cur.getColumnIndex("authToken"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            profile = new FBProfileModel(facebookID, authToken, firstName, lastName, profilePicture, null);
        }
        db.close();
        return profile;
    }

    public static ArrayList<FBProfileModel> getAllFBProfileModels(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FBPROFILE_TABLE, new String[]{});
        ArrayList<FBProfileModel> fbProfileModels = new ArrayList<FBProfileModel>();
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            String authToken = cur.getString(cur.getColumnIndex("authToken"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            FBProfileModel fbProfileModel = new FBProfileModel(facebookID, authToken, firstName, lastName, profilePicture, null);
            fbProfileModels.add(fbProfileModel);
        }
        db.close();
        return fbProfileModels;
    }

    public static long insertOrUpdateFriend(Context context, long friendID, String firstName,
                                            String lastName, String profilePicture, boolean isBlacklisted){
        long userID = DatabaseHelper.getCurrentUser(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("userID", userID);
        row.put("friendID", friendID);
        row.put("firstName", firstName);
        row.put("lastName", lastName);
        row.put("profilePicture", profilePicture);
        row.put("isBlacklisted", isBlacklisted);
        long id = db.insertWithOnConflict(FRIEND_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public static long deleteFriendsForCurrentUser(Context context){
        long userID = DatabaseHelper.getCurrentUser(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FRIEND_TABLE, "userID = ?", new String[]{Long.toString(userID)});
        db.close();
        return id;
    }

    public static long deleteFriendWithID(Context context, long friendID){
        long userID = DatabaseHelper.getCurrentUser(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FRIEND_TABLE, "userID = ? AND friendID = ?",
                new String[]{String.valueOf(userID), String.valueOf(friendID)});
        db.close();
        return id;
    }

    public static Friend getCurrentFriendWithID(Context context, long friendID){
        long userID = DatabaseHelper.getCurrentUser(context);
        Friend friend = null;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FRIEND_TABLE +
                " WHERE userID = ? AND friendID = ?", new String[]{Long.toString(userID), Long.toString(friendID)});
        if(cur.moveToNext()){
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            long isBlacklisted = cur.getLong(cur.getColumnIndex("isBlacklisted"));
            boolean blacklisted = false;
            if(isBlacklisted == 1){
                blacklisted = true;
            }
            friend = new Friend(friendID, firstName, lastName, profilePicture, blacklisted);
        }
        return friend;
    }

    public static HashMap<Long, Friend> getFriendMap(Context context)
    {
        long userID = DatabaseHelper.getCurrentUser(context);
        HashMap<Long, Friend> friendMap = new HashMap<Long, Friend>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FRIEND_TABLE + " WHERE userID = ?", new String[]{Long.toString(userID)});
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("friendID"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            long isBlacklisted = cur.getLong(cur.getColumnIndex("isBlacklisted"));
            boolean blacklisted = false;
            if(isBlacklisted == 1){
                blacklisted = true;
            }
            Friend friend = new Friend(facebookID, firstName, lastName, profilePicture, blacklisted);
            friendMap.put(facebookID, friend);
        }
        db.close();
        return friendMap;
    }

    public static ArrayList<Friend> getFriendList(Context context)
    {
        long userID = DatabaseHelper.getCurrentUser(context);
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FRIEND_TABLE + " WHERE userID = ?", new String[]{Long.toString(userID)});
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("friendID"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            long isBlacklisted = cur.getLong(cur.getColumnIndex("isBlacklisted"));
            boolean blacklisted = false;
            if(isBlacklisted == 1){
                blacklisted = true;
            }
            Friend friend = new Friend(facebookID, firstName, lastName, profilePicture, blacklisted);
            friendList.add(friend);
        }
        db.close();
        return friendList;
    }

    public static ArrayList<Friend> findDeletedFriends(Context context, HashMap<Long, Friend> newFriendList) {
        ArrayList<Friend> deletedFriendList = new ArrayList<Friend>();
        ArrayList<Friend> storedFriendList = DatabaseHelper.getFriendList(context);
        for (int i = 0; i < storedFriendList.size(); i++) {
            Friend friend = storedFriendList.get(i);
            if (newFriendList.get(friend.facebookID) == null) {
                deletedFriendList.add(friend);
            }
        }
        return deletedFriendList;
    }

     /*public static boolean isThisFacebookIDBlacklisted(Context context, long facebookID)
    {
        boolean isBlacklisted = false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FACEBOOKIDBLACKLISTPAIR_TABLE +
                " WHERE facebookID = ?", new String[]{Long.toString(facebookID)});
        if(cur.moveToNext()){
            if(cur.getLong(cur.getColumnIndex("isBlacklisted")) == 1){
                isBlacklisted = true;
            }
        }
        db.close();
        return isBlacklisted;
    }

    public static long deleteFriendWithIDFromFriendTableAndFacebookIDBlacklistPairTable(Context context, long facebookID){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FRIEND_TABLE, "facebookID=?", new String[]{String.valueOf(facebookID)});
        db.delete(FACEBOOKIDBLACKLISTPAIR_TABLE, "facebookID=?", new String[]{String.valueOf(facebookID)});
        db.close();
        return id;
    }

    public static long insertOrUpdateFacebookIDBlacklistPair(Context context, long facebookID, long isBlacklisted){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("facebookID", facebookID);
        row.put("isBlacklisted", isBlacklisted);
        long id = db.insertWithOnConflict(FACEBOOKIDBLACKLISTPAIR_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public static long deleteFacebookIDBlacklistPairWithID(Context context, long facebookID){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FACEBOOKIDBLACKLISTPAIR_TABLE, "facebookID=?", new String[]{String.valueOf(facebookID)});
        db.close();
        return id;
    }

    public static void printFacebookIDBlacklistPairTableRows(Context context)
    {
        //Get the database and select from friends
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FACEBOOKIDBLACKLISTPAIR_TABLE, new String[]{});

        //Fill up the list of friends
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            long isBlacklisted = cur.getLong(cur.getColumnIndex("isBlacklisted"));
            Log.v("facebookID: ", "" + facebookID);
            Log.v("isBlacklisted: ", "" + isBlacklisted);
        }

        //Close the database
        db.close();
    }

    public static long deleteFacebookIDBlacklistPairTableRows(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FACEBOOKIDBLACKLISTPAIR_TABLE, null, new String[]{});
        db.close();
        return id;
    }*/
}

