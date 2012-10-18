package edu.mines.csci498.ybakos.lunchlist;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

class RestaurantHelper extends SQLiteOpenHelper {

	private static final int SCHEMA_VERSION = 6;
	private Context context;
	
	public RestaurantHelper(Context context) {
		super(context, context.getString(R.string.db_name), null, SCHEMA_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(context.getString(R.string.db_create_table));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 2) {
			db.execSQL(context.getString(R.string.migration01_add_url_to_restaurants));
		}
		if (oldVersion < 3) {
			db.execSQL(context.getString(R.string.migration02_add_feed_to_restaurants));
		}
		if (oldVersion < 6) {
			db.execSQL(context.getString(R.string.migration03_add_lat_to_restaurants));
			db.execSQL(context.getString(R.string.migration04_add_lon_to_restaurants));
		}
	}

	public Cursor getAll(String orderBy) {
		return (getReadableDatabase().rawQuery(context.getString(R.string.db_select_restaurants) + " " + orderBy, null));
	}

	public Cursor getById(String id) {
		String[] args = {id};
		return getReadableDatabase().rawQuery(context.getString(R.string.db_select_restaurant), args);
	}
	
	public void insert(String name, String address, String type, String notes, String feed) {
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);
		getWritableDatabase().insert("restaurants", "name", cv);
	}
	
	public void update(String id, String name, String address, String type, String notes, String feed) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);
		getWritableDatabase().update("restaurants", cv, "_id=?", args);
	}

	public void updateLocation(String id, double lat, double lon) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		cv.put("lat", lat);
		cv.put("lon", lon);
		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
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
	
	public String getFeed(Cursor c) {
		return c.getString(6);
	}
	
	public double getLatitude(Cursor c) {
		return c.getDouble(7);
	}
	
	public double getLongitude(Cursor c) {
		return c.getDouble(8);
	}
	
	// Just a stub, to illustrate launching a browser activity via uri.
	public String getUrl(Cursor c) {
		return "http://developer.android.com";
	}
	
}