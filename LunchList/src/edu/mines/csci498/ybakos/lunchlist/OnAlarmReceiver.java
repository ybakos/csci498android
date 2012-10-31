package edu.mines.csci498.ybakos.lunchlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class OnAlarmReceiver extends BroadcastReceiver {

	private static final int NOTIFY_ME_ID = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		if (preferences.getBoolean("use_notification", true)) {
			NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notice = new Notification(R.drawable.ic_popup_reminder,
												   context.getString(R.string.alarmMessage),
												   System.currentTimeMillis());
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, AlarmActivity.class), 0);
			notice.setLatestEventInfo(context,
									  context.getString(R.string.app_name),
									  context.getString(R.string.alarmMessage),
									  pendingIntent);
			notice.flags |= Notification.FLAG_AUTO_CANCEL;
			manager.notify(NOTIFY_ME_ID, notice);
		} else {
			Intent alarmIntent = new Intent(context, AlarmActivity.class);
			alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Necessary when calling startActivity outside of an Activity.
			context.startActivity(alarmIntent);
		}
	}
	
}
