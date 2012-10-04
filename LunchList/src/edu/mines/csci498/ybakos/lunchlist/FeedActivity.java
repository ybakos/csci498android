package edu.mines.csci498.ybakos.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedActivity extends ListActivity {

	public static final String FEED_URL = "edu.mines.csci498.ybakos.lunchlist.FEED_URL";
	private InstanceState state;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		state = (InstanceState) getLastNonConfigurationInstance();
		
		if (state == null) {
			state = new InstanceState();
			state.task = new FeedTask(this);
			state.task.execute(getIntent().getStringExtra(FEED_URL));
		} else {
			if (state.task != null) state.task.attach(this);
			if (state.feed != null) setFeed(state.feed);
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (state.task != null) state.task.detach();
		return state;
	}
	
	private void setFeed(RSSFeed feed) {
		state.feed = feed;
		setListAdapter(new FeedAdapter(feed));
	}
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Exception").setMessage(t.toString()).setPositiveButton("Ok", null).show();
	}
	
	private static class InstanceState {
		RSSFeed feed;
		FeedTask task;
	}
	
	private class FeedAdapter extends BaseAdapter {
		RSSFeed feed = null;
		
		public FeedAdapter(RSSFeed feed) {
			super();
			this.feed = feed;
		}
		
		public int getCount() {
			return feed.getItems().size();
		}
		
		public Object getItem(int position) {
			return feed.getItems().get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			RSSItem item = (RSSItem) getItem(position);
			((TextView) row).setText(item.getTitle());
			return row;
		}
		
	}
	
	private static class FeedTask extends AsyncTask<String, Void, RSSFeed> {
		
		private RSSReader reader;
		private Exception ex;
		private FeedActivity activity;
		
		FeedTask(FeedActivity activity) {
			reader = new RSSReader();
			attach(activity);
		}
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result = null;
			try {
				result = reader.load(urls[0]);
			} catch (Exception e) {
				this.ex = e;
			}
			return result;
		}
		
		@Override
		public void onPostExecute(RSSFeed feed) {
			if (ex == null) {
				activity.setFeed(feed);
			} else {
				Log.e("LunchList", "Exception parsing feed: ", ex);
				activity.goBlooey(ex); // Tell the activity that we blooeyed. (sp?)
			}
		}
		
		private void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		private void detach() {
			this.activity = null;
		}
		
	}
	
}
