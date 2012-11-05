package edu.mines.csci498.ybakos.lunchlist;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class LunchListWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
		context.startService(new Intent(context, WidgetService.class));
	}
	
}
