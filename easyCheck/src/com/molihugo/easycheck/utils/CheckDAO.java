package com.molihugo.easycheck.utils;

import java.util.ArrayList;
import java.util.List;

import com.molihugo.easycheck.beans.Business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class CheckDAO {

	public static abstract class DatabaseConstants implements BaseColumns {
		public static final String TABLE_NAME = "business";
		public static final String COLUMN_NAME_ID = "b_id";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_ADDRESS = "address";
		public static final String COLUMN_NAME_CATS = "categories";
		public static final String COLUMN_NAME_PHONE = "phone";
		public static final String COLUMN_NAME_LAT = "latitude";
		public static final String COLUMN_NAME_LON = "longitude";
		public static final String COLUMN_NAME_DATAORIGIN = "origin";
		public static final String TEXT_TYPE = " TEXT";
		public static final String COMMA_SEP = ",";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+ DatabaseConstants.TABLE_NAME + " (" + DatabaseConstants._ID
				+ " INTEGER PRIMARY KEY," + DatabaseConstants.COLUMN_NAME_ID
				+ TEXT_TYPE + COMMA_SEP + DatabaseConstants.COLUMN_NAME_NAME
				+ TEXT_TYPE + COMMA_SEP + DatabaseConstants.COLUMN_NAME_ADDRESS
				+ TEXT_TYPE + COMMA_SEP + DatabaseConstants.COLUMN_NAME_CATS
				+ TEXT_TYPE + COMMA_SEP + DatabaseConstants.COLUMN_NAME_PHONE
				+ TEXT_TYPE + COMMA_SEP + DatabaseConstants.COLUMN_NAME_LAT
				+ TEXT_TYPE + COMMA_SEP + DatabaseConstants.COLUMN_NAME_LON
				+ TEXT_TYPE + COMMA_SEP
				+ DatabaseConstants.COLUMN_NAME_DATAORIGIN + TEXT_TYPE +
				// Any other options for the CREATE command
				" )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ DatabaseConstants.TABLE_NAME;

		private DatabaseConstants() {
		};
	}

	Context context;
	CheckinsOpenHelper mDbHelper;

	public CheckDAO(Context cont) {
		context = cont;
		mDbHelper = new CheckinsOpenHelper(context);
	}

	public void insert(Business b) {
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(DatabaseConstants.COLUMN_NAME_ID, b.getReference());
		values.put(DatabaseConstants.COLUMN_NAME_NAME, b.getName());
		values.put(DatabaseConstants.COLUMN_NAME_ADDRESS, b.getAddress());
		values.put(DatabaseConstants.COLUMN_NAME_CATS, b.getTypes().toString());
		values.put(DatabaseConstants.COLUMN_NAME_PHONE, b.getPhoneNumber());
		values.put(DatabaseConstants.COLUMN_NAME_LAT, b.getLat());
		values.put(DatabaseConstants.COLUMN_NAME_LON, b.getLon());
		values.put(DatabaseConstants.COLUMN_NAME_DATAORIGIN, b.getApi()
				.toString());

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(DatabaseConstants.TABLE_NAME, null, values);

		db.close();
	}

	public List<Business> get() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { DatabaseConstants._ID,
				DatabaseConstants.COLUMN_NAME_ID,
				DatabaseConstants.COLUMN_NAME_NAME,
				DatabaseConstants.COLUMN_NAME_ADDRESS,
				DatabaseConstants.COLUMN_NAME_CATS,
				DatabaseConstants.COLUMN_NAME_PHONE,
				DatabaseConstants.COLUMN_NAME_LAT,
				DatabaseConstants.COLUMN_NAME_LON,
				DatabaseConstants.COLUMN_NAME_DATAORIGIN, };

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

		// ArrayList<HashMap<String, String>> result = new
		// ArrayList<HashMap<String, String>>();
		// HashMap<String, String> map;
		List<Business> result = new ArrayList<Business>();
		Business b;
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					b = new Business(
							null,
							c.getString(c
									.getColumnIndex(DatabaseConstants.COLUMN_NAME_ID)),
							c.getString(c
									.getColumnIndex(DatabaseConstants.COLUMN_NAME_NAME)),
							c.getString(c
									.getColumnIndex(DatabaseConstants.COLUMN_NAME_ADDRESS)),
							null,
							c.getString(c
									.getColumnIndex(DatabaseConstants.COLUMN_NAME_PHONE)),
							null,
							null,
							c.getString(c
									.getColumnIndex(DatabaseConstants.COLUMN_NAME_LAT)),
							c.getString(c
									.getColumnIndex(DatabaseConstants.COLUMN_NAME_LON)),
							null);
					result.add(b);
					/*
					 * map = new HashMap<String, String>(); String entryId =
					 * c.getString(c
					 * .getColumnIndex(DatabaseConstants.COLUMN_NAME_ID));
					 * String title = c.getString(c
					 * .getColumnIndex(DatabaseConstants.COLUMN_NAME_NAME));
					 * map.put("nombre", entryId); // Place name
					 * map.put("victorias", title); // adding HashMap to
					 * ArrayList result.add(map);
					 */
				} while (c.moveToNext());
			}
		}

		c.close();
		db.close();

		return result;
	}

	public void delete() {
		// Define 'where' part of query.
		String selection = DatabaseConstants.COLUMN_NAME_ID + " LIKE ?";
		// Specify arguments in placeholder order.
		// String[] selectionArgs = { String.valueOf(rowId) };
		// Issue SQL statement.
		// db.delete(table_name, selection, selectionArgs);
	}

	public void update() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// New value for one column
		ContentValues values = new ContentValues();
		// values.put(DatabaseConstants.COLUMN_NAME_NAME, title);

		// Which row to update, based on the ID
		String selection = DatabaseConstants.COLUMN_NAME_ID + " LIKE ?";
		// String[] selectionArgs = { String.valueOf(rowId) };

		// int count = db.update(DatabaseConstants.TABLE_NAME, values,
		// selection,
		// selectionArgs);
	}

}