package edu.mines.csci498.ybakos.lunchlist;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public class WidgetService extends IntentService {

	public WidgetService() {
		super("WidgetService");
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		ComponentName me = new ComponentName(this, LunchListWidget.class);
		RemoteViews updateViews = new RemoteViews("edu.mines.csci498.ybakos.lunchlist", R.layout.widget);
		RestaurantHelper helper = new RestaurantHelper(this);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		
		try {
			Cursor cursor = helper.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM restaurants", null);
			cursor.moveToFirst();
			int count = cursor.getInt(0);
			cursor.close();
			
			if (count > 0) {
				int offset = (int) (count * Math.random());
				String args[] = {String.valueOf(offset)};
				cursor = helper.getReadableDatabase().rawQuery("SELECT _ID, name FROM restaurants LIMIT 1 OFFSET ?", args);
				cursor.moveToFirst();
				updateViews.setTextViewText(R.id.name, cursor.getString(1));
				Intent detailFormIntent = new Intent(this, DetailForm.class);
				detailFormIntent.putExtra(LunchListActivity.ID_EXTRA, cursor.getString(0));
				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, detailFormIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				updateViews.setOnClickPendingIntent(R.id.name, pendingIntent);
				cursor.close();
			} else {
				updateViews.setTextViewText(R.id.name, this.getString(R.string.widget_empty));
			}
		} finally {
			helper.close();
		}
		Intent widgetIntent = new Intent(this, WidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, widgetIntent, 0);
		updateViews.setOnClickPendingIntent(R.id.next, pendingIntent);
		manager.updateAppWidget(me, updateViews);
	}
	
}
