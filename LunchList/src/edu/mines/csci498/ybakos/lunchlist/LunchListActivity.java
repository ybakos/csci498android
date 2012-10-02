package edu.mines.csci498.ybakos.lunchlist;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

@SuppressWarnings("deprecation")
public class LunchListActivity extends ListActivity {
	
	public static final String ID_EXTRA="edu.mines.csci498.ybakos.lunchlist._ID";
	
	Cursor model;
	RestaurantAdapter adapter;
	RestaurantHelper helper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		helper = new RestaurantHelper(this);
		model = helper.getAll();
		startManagingCursor(model);
		adapter = new RestaurantAdapter(model);
		setListAdapter(adapter);
		Log.d("LUNCHLIST", "DONE");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		Intent intent = new Intent(LunchListActivity.this, DetailForm.class);
		intent.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(LunchListActivity.this, DetailForm.class));
			return true;
		} else if (item.getItemId() == R.id.www) {
			startActivity(new Intent("android.intent.ACTION_VIEW", Uri.parse(helper.getUrl(model))));
		} else if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(LunchListActivity.this, EditPreferences.class));
		}
		return super.onOptionsItemSelected(item);
	}
	
	class RestaurantAdapter extends CursorAdapter {
		
		RestaurantAdapter(Cursor c) {
			super(LunchListActivity.this, c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();

			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			return (row);
		}
	}

	static class RestaurantHolder {

		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder(View row) {
			name = (TextView) row.findViewById(R.id.name);
			address = (TextView) row.findViewById(R.id.address);
			icon = (ImageView) row.findViewById(R.id.icon);
		}

		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));
			if (helper.getType(c).equals("sit_down")) {
				icon.setImageResource(R.drawable.icon_sit_down);
			} else if (helper.getType(c).equals("take_out")) {
				icon.setImageResource(R.drawable.icon_take_out);
			} else {
				icon.setImageResource(R.drawable.icon_delivery);
			}
		}
	}
}