package edu.mines.csci498.ybakos.lunchlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	private Context context;
	private RestaurantHelper helper;
	private Cursor restaurants;
	
	public ListViewsFactory(Context context, Intent intent) {
		this.context = context;
	}
	
	@Override
	public void onCreate() {
		helper = new RestaurantHelper(context);
		restaurants = helper.getReadableDatabase().rawQuery("SELECT _ID, name FROM restaurants", null);
	}
	
	@Override
	public void onDestroy() {
		restaurants.close();
		helper.close();
	}
	
	@Override
	public int getCount() {
		return restaurants.getCount();
	}
	
	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_row);
		restaurants.moveToPosition(position);
		row.setTextViewText(android.R.id.text1, restaurants.getString(1));
		Intent intent = new Intent();
		Bundle extras = new Bundle();
		extras.putString(LunchListActivity.ID_EXTRA, String.valueOf(restaurants.getInt(0)));
		intent.putExtras(extras);
		row.setOnClickFillInIntent(android.R.id.text1, intent);
		return row;
	}
	
	@Override
	public RemoteViews getLoadingView() {
		return null;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public long getItemId(int position) {
		restaurants.moveToPosition(position);
		return restaurants.getInt(0);
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public void onDataSetChanged() {
		
	}
	
}
