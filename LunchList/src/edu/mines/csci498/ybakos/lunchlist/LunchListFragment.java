package edu.mines.csci498.ybakos.lunchlist;

import android.support.v4.app.ListFragment;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

@SuppressWarnings("deprecation")
public class LunchListFragment extends ListFragment {
	
	public static final String ID_EXTRA="edu.mines.csci498.ybakos.lunchlist._ID";
	private SharedPreferences preferences;
	
	Cursor model;
	RestaurantAdapter adapter;
	RestaurantHelper helper;
	OnRestaurantListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		helper = new RestaurantHelper(getActivity());
		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		initList();
		preferences.registerOnSharedPreferenceChangeListener(prefListener);
	}
	
	private void initList() {
		if (model != null) {
			model.close();
		}
		model = helper.getAll(preferences.getString("sort_order", "name"));
		adapter = new RestaurantAdapter(model);
		setListAdapter(adapter);
	}
	
	@Override
	public void onPause() {
		helper.close();
		super.onPause();
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		if (listener != null) listener.onRestaurantSelected(id);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.option, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(getActivity(), DetailForm.class));
			return true;
		} else if (item.getItemId() == R.id.www) {
			startActivity(new Intent("android.intent.ACTION_VIEW", Uri.parse(helper.getUrl(model))));
		} else if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(getActivity(), EditPreferences.class));
		} else if (item.getItemId() == R.id.help) {
			startActivity(new Intent(getActivity(), HelpActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setOnRestaurantListener(OnRestaurantListener listener) {
		this.listener = listener;
	}
	
	class RestaurantAdapter extends CursorAdapter {
		
		RestaurantAdapter(Cursor c) {
			super(getActivity(), c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
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
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener =
		new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
				if (key.equals("sort_order")) initList();
			}
		};	
	
	public interface OnRestaurantListener {
		void onRestaurantSelected(long id);
	}
		
}