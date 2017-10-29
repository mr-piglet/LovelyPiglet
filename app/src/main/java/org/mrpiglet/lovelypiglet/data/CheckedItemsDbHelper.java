package org.mrpiglet.lovelypiglet.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CheckedItemsDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "checkeditems.db";

    private static final int DATABASE_VERSION = 1;

    public CheckedItemsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CHECKED_ITEMS_TABLE = "CREATE TABLE " + CheckedItemsContract.CheckedItemsEntry.TABLE_NAME + " (" +
                CheckedItemsContract.CheckedItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CheckedItemsContract.CheckedItemsEntry.COLUMN_DESCRIPTION_NAME + " TEXT NOT NULL, " +
                CheckedItemsContract.CheckedItemsEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_CHECKED_ITEMS_TABLE);
    }

    //this method currently destroys db when upgrading database version - change to ALTER in production
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + CheckedItemsContract.CheckedItemsEntry.TABLE_NAME);
        onCreate(db);
    }
}
