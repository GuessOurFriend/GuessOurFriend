package inc.guessourfriend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sellmaurer on 10/19/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "User Info";
    private static final String FBPROFILE_TABLE = "FBProfileTable";
    private static final String FRIEND_TABLE = "FriendTable";
    private static final String CREATE_FBPROFILE_TABLE_QUERY = "CREATE TABLE " + FBPROFILE_TABLE +
            " (facebookID INTEGER PRIMARY KEY, " + "fullName TEXT, " + "profilePicture TEXT);";
    private static final String CREATE_FRIEND_TABLE_QUERY = "CREATE TABLE " + FRIEND_TABLE +
            " (facebookID INTEGER PRIMARY KEY, " + "fullName TEXT, " + "profilePicture TEXT, " +
            "groups TEXT);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FBPROFILE_TABLE_QUERY);
        db.execSQL(CREATE_FRIEND_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+FBPROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+FRIEND_TABLE);
        onCreate(db);
    }

    public static long insertOrUpdateFriend(Context context, long facebookID, String fullName,
                                            String profilePicture, String groups){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("facebookID", facebookID);
        row.put("fullName", fullName);
        row.put("profilePicture", profilePicture);
        row.put("groups", groups);
        long id = db.insertWithOnConflict(FRIEND_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    /*public static long deleteFriendWithID(Context context, long facebookID){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FRIEND_TABLE, "facebookID=?", new String[]{String.valueOf(facebookID)});
        db.close();
        return id;
    }*/

    public static void getFriendTableRows(Context context)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FRIEND_TABLE, new String[]{});
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            String fullName = cur.getString(cur.getColumnIndex("fullName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            String groups = cur.getString(cur.getColumnIndex("groups"));
            Log.v("facebookID: ", "" + facebookID);
            Log.v("fullName: ", fullName);
            Log.v("profilePicture: ", profilePicture);
            Log.v("groups: ", groups);
        }
        db.close();
    }

    public static long insertOrUpdateFBProfile(Context context, long facebookID, String fullName, String profilePicture){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("facebookID", facebookID);
        row.put("fullName", fullName);
        row.put("profilePicture", profilePicture);
        long id = db.insertWithOnConflict(FBPROFILE_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    /*
    public static long deleteFBProfile(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.delete(FBPROFILE_TABLE, null, new String[]{});
        db.close();
        return id;
    }*/

    public static void getFBProfileTableRows(Context context)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FBPROFILE_TABLE, new String [] {});
        while(cur.moveToNext()) {
            long facebookID = cur.getLong(cur.getColumnIndex("facebookID"));
            String fullName = cur.getString(cur.getColumnIndex("fullName"));
            String profilePicture = cur.getString(cur.getColumnIndex("profilePicture"));
            Log.v("facebookID: ", "" + facebookID);
            Log.v("fullName: ", fullName);
            Log.v("profilePicture: ", profilePicture);
        }
        db.close();
    }
}

