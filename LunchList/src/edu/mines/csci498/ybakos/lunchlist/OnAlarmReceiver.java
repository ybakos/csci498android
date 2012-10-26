package edu.mines.csci498.ybakos.lunchlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent alarmIntent = new Intent(context, AlarmActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Necessary when calling startActivity outside of an Activity.
		context.startActivity(alarmIntent);
	}
	
}
