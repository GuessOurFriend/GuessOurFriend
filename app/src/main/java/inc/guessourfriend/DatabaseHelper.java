package inc.guessourfriend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sellmaurer on 10/19/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "User Info";
    private static final String FBPROFILE_TABLE = "FBProfileTable";
    private static final String FACEBOOKIDBLACKLISTPAIR_TABLE = "FacebookIDBlacklistPairTable";
    private static final String FRIEND_TABLE = "FriendTable";
    private static final String CHALLENGES_TABLE = "ChallengesTable";
    // authToken is the unique user token needed to make queries to our Ruby on Rails Heroku server
    private static final String CREATE_FBPROFILE_TABLE_QUERY = "CREATE TABLE " + FBPROFILE_TABLE +
            " (facebookID INTEGER PRIMARY KEY, " + "authToken TEXT, " + "firstName TEXT, " +
            "lastName TEXT, " + "profilePicture TEXT);";
    private static final String CREATE_FACEBOOKIDBLACKLISTPAIR_TABLE_QUERY = "CREATE TABLE " + FACEBOOKIDBLACKLISTPAIR_TABLE +
            " (facebookID INTEGER PRIMARY KEY, " + "isBlacklisted INTEGER);";
    private static final String CREATE_FRIEND_TABLE_QUERY = "CREATE TABLE " + FRIEND_TABLE +
            " (facebookID INTEGER PRIMARY KEY, " + "firstName TEXT, " + "lastName TEXT, " + "profilePicture TEXT);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FBPROFILE_TABLE_QUERY);
        db.execSQL(CREATE_FACEBOOKIDBLACKLISTPAIR_TABLE_QUERY);
        db.execSQL(CREATE_FRIEND_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + FBPROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FACEBOOKIDBLACKLISTPAIR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHALLENGES_TABLE);
        onCreate(db);
    }

    public static long insertOrUpdateFriend(Context context, long facebookID, String firstName,
                                            String lastName, String profilePicture){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("facebookID", facebookID);
        row.put("firstName", firstName);
        row.put("lastName", lastName);
        row.put("profilePicture", profilePicture);
        long id = db.insertWithOnConflict(FRIEND_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public static long deleteFriendWithID(Context context, long facebookID){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FRIEND_TABLE, "facebookID=?", new String[]{String.valueOf(facebookID)});
        db.close();
        return id;
    }

    public static Friend getFriendWithID(Context context, long facebookID){
        Friend friend = null;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FRIEND_TABLE +
                " WHERE facebookID = ?", new String[]{Long.toString(facebookID)});
        if(cur.moveToNext()){
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            friend = new Friend(facebookID, firstName, lastName, profilePicture);
        }
        return friend;
    }

    public static ArrayList<Friend> getFriendList(Context context)
    {
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FRIEND_TABLE, new String[]{});
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            Friend friend = new Friend(facebookID, firstName, lastName, profilePicture);
            friendList.add(friend);
        }
        db.close();
        return friendList;
    }

    public static boolean isThisFacebookIDBlacklisted(Context context, long facebookID)
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
    }

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
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FBPROFILE_TABLE, null, new String[]{});
        db.close();
        return id;
    }

    public static FBProfileModel getFBProfile(Context context)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FBPROFILE_TABLE, new String[]{});

        //Declare a profile to return
        FBProfileModel profile = null;

        //If we have a profile in the table, get it
        if (cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            String authToken = cur.getString(cur.getColumnIndex("authToken"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            profile = new FBProfileModel(facebookID, authToken, firstName, lastName, profilePicture, null);
            Log.v("facebookID: ", "" + facebookID);
            Log.v("authToken: ", authToken);
            Log.v("firstName: ", firstName);
            Log.v("lastName: ", lastName);
            Log.v("profilePicture: ", profilePicture);
        }

        //There should only be one profile model, throw an exception
        //TODO: Support multiple accounts on a device
        if (cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            String authToken = cur.getString(cur.getColumnIndex("authToken"));
            String firstName = cur.getString(cur.getColumnIndex("firstName"));
            String lastName = cur.getString(cur.getColumnIndex("lastName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            Log.v("second facebookID ", "" + facebookID);
            Log.v("authToken: ", authToken);
            Log.v("firstName: ", firstName);
            Log.v("lastName: ", lastName);
            Log.v("profilePicture: ", profilePicture);

            throw new IllegalStateException();
        }

        //Close the database
        db.close();

        //Return the profile if we found one
        return profile;
    }

}

