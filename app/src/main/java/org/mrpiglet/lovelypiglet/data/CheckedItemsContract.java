package org.mrpiglet.lovelypiglet.data;

import android.provider.BaseColumns;

public class CheckedItemsContract {
    //for each table we make a new entry class with columns description
    public static final class CheckedItemsEntry implements BaseColumns {
        public static final String TABLE_NAME = "checkeditems";
        public static final String COLUMN_DESCRIPTION_NAME = "itemDescription";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
