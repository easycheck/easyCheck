package com.example.easycheck.utils;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class CheckinsOpenHelper extends SQLiteOpenHelper {
	
	public static abstract class DatabaseConstants implements BaseColumns {
		public static final String TABLE_NAME = "entry";
		public static final String COLUMN_NAME_ENTRY_ID = "entryid";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_SUBTITLE = "subtitle";
		public static final String TEXT_TYPE = " TEXT";
		public static final String COMMA_SEP = ",";
		public static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + DatabaseConstants.TABLE_NAME + " (" +
		    		DatabaseConstants._ID + " INTEGER PRIMARY KEY," +
		    		DatabaseConstants.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
		    DatabaseConstants.COLUMN_NAME_TITLE + TEXT_TYPE +
		    // Any other options for the CREATE command
		    " )";

		private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_NAME;
		
		private DatabaseConstants() {};
	}
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    
    public CheckinsOpenHelper(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DatabaseConstants.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

