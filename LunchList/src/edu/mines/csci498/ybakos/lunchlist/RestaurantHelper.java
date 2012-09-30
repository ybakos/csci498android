package edu.mines.csci498.ybakos.lunchlist;

import android.content.Context;
import android.database.SQLException;
import android.database.Cursor;
import android.database.sqlite.*;

public class RestaurantHelper extends SQLiteOpenHelper {

	private static final int SCHEMA_VERSION = 1;
	Context context;
	
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
		// not used yet
		// make temp table, copy data, fix up real schema, copy data back. Egads!
	}
	
}
