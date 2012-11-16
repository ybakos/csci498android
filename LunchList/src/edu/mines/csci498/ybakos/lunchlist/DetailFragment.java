package edu.mines.csci498.ybakos.lunchlist;

import android.support.v4.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class DetailFragment extends Fragment {

	private static final String ARG_RESTAURANT_ID = "edu.mines.csci498.ybakos.lunchlist.ARG_RESTAURANT_ID";
	
	EditText name;
	EditText address;
	EditText notes;
	EditText feed;
	TextView location;
	RadioGroup types;
	RestaurantHelper helper;
	String restaurantId;
	LocationManager locationManager;
	double latitude;
	double longitude;
	
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			getHelper().updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude()) + ", " + String.valueOf(fix.getLongitude()));
			locationManager.removeUpdates(onLocationChange);
			Toast.makeText(getActivity(), "Location saved bwhah!", Toast.LENGTH_LONG).show();
		}
		
		public void onProviderDisabled(String provider) { /* not using */ }
		public void onProviderEnabled(String provider) { /* not using */ }
		public void onStatusChanged(String provider, int status, Bundle extras) { /* not using */ }
	};

	public static DetailFragment newInstance(long id) {
		DetailFragment result = new DetailFragment();
		Bundle args = new Bundle();
		args.putString(ARG_RESTAURANT_ID, String.valueOf(id));
		result.setArguments(args);
		return result;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		latitude = 0.0d;
		longitude = 0.0d;
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.detail_form, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				
		name = (EditText) getView().findViewById(R.id.name);
		address = (EditText) getView().findViewById(R.id.address);
		notes = (EditText) getView().findViewById(R.id.notes);
		feed = (EditText) getView().findViewById(R.id.feed);
		types = (RadioGroup) getView().findViewById(R.id.restaurantTypes);
		location = (TextView) getView().findViewById(R.id.location);
		
		Bundle arguments = getArguments();
		if (arguments != null) loadRestaurant(arguments.getString(ARG_RESTAURANT_ID));
	}
	
	private RestaurantHelper getHelper() {
		if (helper == null) helper = new RestaurantHelper(getActivity());
		return helper;
	}
	
	@Override
	public void onPause() {
		save();
		getHelper().close();
		locationManager.removeUpdates(onLocationChange);
		super.onPause();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.details_option, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
			menu.findItem(R.id.map).setEnabled(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent intent = new Intent(getActivity(), FeedActivity.class);
				intent.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
			}
			return true;
		} else if (item.getItemId() == R.id.location) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
			return true;
		} else if (item.getItemId() == R.id.map){
			Intent intent = new Intent(getActivity(), RestaurantMap.class);
			intent.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
			intent.putExtra(RestaurantMap.EXTRA_LONGITUDE, latitude);
			intent.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.help) {
			startActivity(new Intent(getActivity(), HelpActivity.class));
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
				getHelper().insert(name.getText().toString(),
							  address.getText().toString(),
							  type,
							  notes.getText().toString(),
							  feed.getText().toString());
			} else {
				getHelper().update(restaurantId,
							  name.getText().toString(),
							  address.getText().toString(),
							  type,
							  notes.getText().toString(),
							  feed.getText().toString());
			}
		}
	}
	
 	private void load() {
		Cursor c = getHelper().getById(restaurantId);
		c.moveToFirst();

		name.setText(getHelper().getName(c));
		address.setText(getHelper().getAddress(c));
		notes.setText(getHelper().getNotes(c));
		feed.setText(getHelper().getFeed(c));
		if (getHelper().getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		} else if (getHelper().getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}
		latitude = getHelper().getLatitude(c);
		longitude = getHelper().getLongitude(c);
		location.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));
		c.close();
	}

 	public void loadRestaurant(String restaurantId) {
 		this.restaurantId = restaurantId;
 		if (restaurantId != null) load();
 	}
 	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null);
	}

}