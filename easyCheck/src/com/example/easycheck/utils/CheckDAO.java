package com.example.easycheck.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.easycheck.utils.CheckinsOpenHelper.DatabaseConstants;

public class CheckDAO {

	Context context;
	CheckinsOpenHelper mDbHelper;

	public CheckDAO(Context cont) {
		context = cont;
		mDbHelper = new CheckinsOpenHelper(context);
	}

	public void insert(String id, String title) {
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(DatabaseConstants.COLUMN_NAME_ENTRY_ID, id);
		values.put(DatabaseConstants.COLUMN_NAME_TITLE, title);

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(DatabaseConstants.TABLE_NAME, null, values);
		
		db.close();
	}

	public ArrayList<HashMap<String, String>> get() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { DatabaseConstants._ID,
				DatabaseConstants.COLUMN_NAME_ENTRY_ID,
				DatabaseConstants.COLUMN_NAME_TITLE, };

		String selection = null;

		String[] selectionArgs = null;

		// How you want the results sorted in the resulting Cursor
		// String sortOrder = DatabaseConstants.COLUMN_NAME_UPDATED + " DESC";

		Cursor c = db.query(DatabaseConstants.TABLE_NAME, // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;

		if (c != null) {
			if (c.moveToFirst()) {
				do {
					map = new HashMap<String, String>();
					String entryId = c.getString(c
							.getColumnIndex(DatabaseConstants.COLUMN_NAME_ENTRY_ID));
					String title = c.getString(c
							.getColumnIndex(DatabaseConstants.COLUMN_NAME_TITLE));
					map.put("nombre", entryId);
					// Place name
					map.put("victorias", title);
					// adding HashMap to ArrayList
					result.add(map);
				} while (c.moveToNext());
			}
		}
		
		c.close();
		db.close();
		
		return result;
	}

	public void delete() {
		// Define 'where' part of query.
		String selection = DatabaseConstants.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		// Specify arguments in placeholder order.
		// String[] selectionArgs = { String.valueOf(rowId) };
		// Issue SQL statement.
		// db.delete(table_name, selection, selectionArgs);
	}

	public void update() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// New value for one column
		ContentValues values = new ContentValues();
		// values.put(DatabaseConstants.COLUMN_NAME_TITLE, title);

		// Which row to update, based on the ID
		String selection = DatabaseConstants.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		// String[] selectionArgs = { String.valueOf(rowId) };

		// int count = db.update(DatabaseConstants.TABLE_NAME, values,
		// selection,
		// selectionArgs);
	}

}