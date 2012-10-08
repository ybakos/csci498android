package edu.mines.csci498.ybakos.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class FeedService extends IntentService {
	
	public static final String EXTRA_FEED_URL = "edu.mines.csci498.ybakos.lunchlist.FEED_URL";
	
	public FeedService() {
		super("FeedService");
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		RSSReader reader = new RSSReader();
		try {
			RSSFeed result = reader.load(intent.getStringExtra(EXTRA_FEED_URL));
		} catch (Exception e) {
			Log.e("LunchList", "Exception parsing RSS Feed", e);
		}
	}
	
}
