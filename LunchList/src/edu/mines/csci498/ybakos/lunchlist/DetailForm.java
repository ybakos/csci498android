package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class DetailForm extends Activity {

	EditText name;
	EditText address;
	EditText notes;
	EditText feed;
	TextView location;
	RadioGroup types;
	RestaurantHelper helper;
	String restaurantId;
	LocationManager locationManager;
	
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude()) + ", " + String.valueOf(fix.getLongitude()));
			locationManager.removeUpdates(onLocationChange);
			Log.d("X", "Before toast");
			Toast.makeText(DetailForm.this, "Location saved bwhah!", Toast.LENGTH_LONG).show();
			Log.d("X", "After toast");
		}
		
		public void onProviderDisabled(String provider) { /* not using */ }
		public void onProviderEnabled(String provider) { /* not using */ }
		public void onStatusChanged(String provider, int status, Bundle extras) { /* not using */ }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		helper = new RestaurantHelper(this);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.address);
		notes = (EditText) findViewById(R.id.notes);
		feed = (EditText) findViewById(R.id.feed);
		types = (RadioGroup) findViewById(R.id.restaurantTypes);
		location = (TextView) findViewById(R.id.location);
		
		restaurantId = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		if (restaurantId != null) load();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putString("feed", feed.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		feed.setText(state.getString("feed"));
		types.check(state.getInt("type"));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}
	
	@Override
	public void onPause() {
		save();
		locationManager.removeUpdates(onLocationChange);
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent intent = new Intent(this, FeedActivity.class);
				intent.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(intent);
			} else {
				Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
			}
			return true;
		} else if (item.getItemId() == R.id.location) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
		}
		return super.onOptionsItemSelected(item);
	}

	private void save() {
		if (name.getText().toString().length() > 0) {
			String type = null;
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					type = "sit_down";
					break;
				case R.id.take_out:
					type = "take_out";
					break;
				case R.id.delivery:
					type = "delivery";
					break;
			}
			if (restaurantId == null) {
				helper.insert(name.getText().toString(),
							  address.getText().toString(),
							  type,
							  notes.getText().toString(),
							  feed.getText().toString());
			} else {
				helper.update(restaurantId,
							  name.getText().toString(),
							  address.getText().toString(),
							  type,
							  notes.getText().toString(),
							  feed.getText().toString());
			}
		}
	}
	
 	private void load() {
		Cursor c = helper.getById(restaurantId);
		c.moveToFirst();

		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}
		location.setText(String.valueOf(helper.getLatitude(c)) + ", " + String.valueOf(helper.getLongitude(c)));
		c.close();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null);
	}

}