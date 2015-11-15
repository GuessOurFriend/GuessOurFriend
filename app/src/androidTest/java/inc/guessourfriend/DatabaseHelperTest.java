package inc.guessourfriend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class DatabaseHelperTest extends AndroidTestCase {

    //Create the constants for the database names
    private static final String DATABASE_NAME = "User Info";
    private static final String FBPROFILE_TABLE = "FBProfileTable";
    private static final String FACEBOOKIDBLACKLISTPAIR_TABLE = "FacebookIDBlacklistPairTable";
    private static final String FRIEND_TABLE = "FriendTable";
    private static final String CHALLENGES_TABLE = "ChallengesTable";

    @Test
    public void testDeleteFBProfile() throws Exception {
        //Arrange - add a FBProfile to the local database
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("facebookID", 1234);
        row.put("authToken", "authToken");
        row.put("firstName", "firstName");
        row.put("lastName", "lastName");
        row.put("profilePicture", "profilePicture");
        long id = db.insertWithOnConflict(FBPROFILE_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        //Act - call the method we are testing
        DatabaseHelper.deleteFBProfile(this.getContext());

        //Assert
        db = databaseHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + FBPROFILE_TABLE, new String[]{});
        if (cur.moveToNext()) {
            Assert.fail("The profile was not deleted.");
        }
    }

    @Test
    public void testGetFBProfile() throws Exception {
        try {
            //Arrange - add a FBProfile to the local database
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues row = new ContentValues();
            row.put("facebookID", 1234);
            row.put("authToken", "authToken");
            row.put("firstName", "firstName");
            row.put("lastName", "lastName");
            row.put("profilePicture", "profilePicture");
            long id = db.insertWithOnConflict(FBPROFILE_TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();

            //Act - call the method we are testing
            FBProfileModel model = DatabaseHelper.getFBProfile(this.getContext());

            //Assert
            Assert.assertNotNull("The model was null.", model);
            Assert.assertEquals("The facebook id did not match.", 1234, model.facebookID);
        } finally {
            //Clean up the data
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.delete(FBPROFILE_TABLE, null, new String[]{});
            db.close();
        }
    }
}