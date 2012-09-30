package edu.mines.csci498.ybakos.lunchlist;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

class RestaurantHelper extends SQLiteOpenHelper {

	private static final int SCHEMA_VERSION = 1;
	private Context context;

	public RestaurantHelper(Context context) {
		super(context, context.getString(R.string.db_name), null,
				SCHEMA_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(context.getString(R.string.db_create_table));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// intentionally blank
	}

	public Cursor getAll() {
		return (getReadableDatabase().rawQuery(context.getString(R.string.db_select_restaurants), null));
	}

	public void insert(String name, String address, String type, String notes) {
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		getWritableDatabase().insert("restaurants", "name", cv);
	}

	public String getName(Cursor c) {
		return c.getString(1);
	}

	public String getAddress(Cursor c) {
		return c.getString(2);
	}

	public String getType(Cursor c) {
		return c.getString(3);
	}

	public String getNotes(Cursor c) {
		return c.getString(4);
	}
	
}