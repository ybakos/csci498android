package edu.mines.csci498.ybakos.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class FeedService extends IntentService {
	
	public static final String EXTRA_FEED_URL = "edu.mines.csci498.ybakos.lunchlist.FEED_URL";
	public static final String EXTRA_FEED_MESSENGER = "edu.mines.csci498.ybakos.lunchlist.EXTRA_FEED_MESSENGER";
	
	public FeedService() {
		super("FeedService");
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		RSSReader reader = new RSSReader();
		Messenger messenger = (Messenger) intent.getExtras().get(EXTRA_FEED_MESSENGER);
		Message message = Message.obtain();
		try {
			RSSFeed result = reader.load(intent.getStringExtra(EXTRA_FEED_URL));
			message.arg1 = Activity.RESULT_OK;
			message.obj = result;
		} catch (Exception e) {
			Log.e("LunchList", "Exception parsing RSS Feed", e);
			message.arg1 = Activity.RESULT_CANCELED;
			message.obj = e;
		}
		try {
			messenger.send(message);
		} catch (Exception e) {
			Log.w("LunchList", "Exception sending results to Activity", e);
		}
	}
	
}
