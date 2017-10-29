package org.mrpiglet.lovelypiglet.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mrpiglet.lovelypiglet.data.CheckedItemsContract;

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

}
