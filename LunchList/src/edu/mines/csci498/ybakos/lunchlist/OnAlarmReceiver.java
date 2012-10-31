package edu.mines.csci498.ybakos.lunchlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

public class OnAlarmReceiver extends BroadcastReceiver {

	private static final int NOTIFY_ME_ID = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (preferences.getBoolean("use_notification", true)) generateNotification(context);
		else showAlarmActivity(context);
	}
	
	private void generateNotification(Context context) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, AlarmActivity.class), 0);
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Notification notice = new Notification.Builder(context)
			.setTicker("Time for a drink OMG squeeal!")
			.setContentTitle(context.getString(R.string.alarmMessage))
			.setSmallIcon(R.drawable.ic_popup_reminder)
			.setContentText(context.getString(R.string.app_name))
//			.setSound(uri)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND).getNotification();
		manager.notify(NOTIFY_ME_ID, notice);
	}
	
	private void showAlarmActivity(Context context) {
		Intent alarmIntent = new Intent(context, AlarmActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Necessary when calling startActivity outside of an Activity.
		context.startActivity(alarmIntent);
	}
	
}
