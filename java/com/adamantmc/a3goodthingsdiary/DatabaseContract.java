package com.adamantmc.a3goodthingsdiary;

import android.provider.BaseColumns;

/**
 * Created by Adamantios on 2/5/2016.
 */
public final class DatabaseContract {

    public DatabaseContract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "good_things";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_THING1 = "thing1";
        public static final String COLUMN_NAME_THING2 = "thing2";
        public static final String COLUMN_NAME_THING3 = "thing3";
    }

    /*
        Database Schema. Each entry has an ID as a primary key, then a date in the format of YYYY-MM-DD, then the 3 good things.
        The first good thing cannot be null, as there is no point in having an entry if the first thing is null.
    */

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                    Entry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    Entry.COLUMN_NAME_DATE + " TEXT NOT NULL ," +
                    Entry.COLUMN_NAME_THING1 + " TEXT NOT NULL," +
                    Entry.COLUMN_NAME_THING2 + " TEXT," +
                    Entry.COLUMN_NAME_THING3 + " TEXT )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

}
