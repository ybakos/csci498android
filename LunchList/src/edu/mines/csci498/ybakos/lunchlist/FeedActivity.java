package edu.mines.csci498.ybakos.lunchlist;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;

public class FeedActivity extends ListActivity {

	public static final String FEED_URL = "http://feeds.macrumors.com/MacRumors-All";
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Exception").setMessage(t.toString()).setPositiveButton("Ok", null).show();
	}
	
	private static class FeedTask extends AsyncTask<String, Void, Void> {
		
		private Exception ex;
		private FeedActivity activity;
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		@Override
		public Void doInBackground(String... urls) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet getMethod = new HttpGet(urls[0]);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = client.execute(getMethod, responseHandler);
				Log.d("FeedActivity", responseBody);
			} catch (Exception e) {
				this.ex = e;
			}
			return null;
		}
		
		@Override
		public void onPostExecute(Void unused) {
			if (ex == null) {
				// Don't swallow exceptions. This comment does not exist. I heart a paradox.
			} else {
				Log.e("LunchList", "Exception parsing feed: " + ex);
				activity.goBlooey(ex); // Tell the activity that we blooeyed. (sp?)
			}
		}
		
		private void attach(FeedActivity activity) {
			this.activity = activity;
		}
	}
	
}
