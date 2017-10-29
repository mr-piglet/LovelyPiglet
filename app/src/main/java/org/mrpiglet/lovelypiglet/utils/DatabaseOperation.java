package org.mrpiglet.lovelypiglet.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.mrpiglet.lovelypiglet.data.CheckedItemsContract;

import java.util.ArrayList;
import java.util.List;

//simple class to encapsulate basic database operations
public final class DatabaseOperation {
    //gets all items sorted by timestamp
    public static Cursor getAllItems(SQLiteDatabase db) {
    return db.query(
            CheckedItemsContract.CheckedItemsEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            CheckedItemsContract.CheckedItemsEntry.COLUMN_TIMESTAMP
    );
}


    public static long addNewCheckedItem(SQLiteDatabase db, String description) {
        ContentValues cv = new ContentValues();
        cv.put(CheckedItemsContract.CheckedItemsEntry.COLUMN_DESCRIPTION_NAME, description);
        return db.insert(CheckedItemsContract.CheckedItemsEntry.TABLE_NAME, null, cv);
    }

    public static boolean removeCheckedItem(SQLiteDatabase db, long id) {
        return db.delete(CheckedItemsContract.CheckedItemsEntry.TABLE_NAME, CheckedItemsContract.CheckedItemsEntry._ID + "=" + id, null) > 0;
    }

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(CheckedItemsContract.CheckedItemsEntry.COLUMN_DESCRIPTION_NAME, "Locked after Piglets have eaten");
        list.add(cv);

        cv = new ContentValues();
        cv.put(CheckedItemsContract.CheckedItemsEntry.COLUMN_DESCRIPTION_NAME, "Locked after Chinese food");
        list.add(cv);

        cv = new ContentValues();
        cv.put(CheckedItemsContract.CheckedItemsEntry.COLUMN_DESCRIPTION_NAME, "Locked after little Piglet pooped");
        list.add(cv);

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (CheckedItemsContract.CheckedItemsEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(CheckedItemsContract.CheckedItemsEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }

}
