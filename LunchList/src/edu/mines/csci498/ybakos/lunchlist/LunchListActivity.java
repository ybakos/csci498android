package edu.mines.csci498.ybakos.lunchlist;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.widget.*;
import android.view.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.graphics.Typeface;
import android.util.Log;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("deprecation")
public class LunchListActivity extends TabActivity {

	Cursor restaurants;
	RestaurantsAdapter restaurantsAdapter;
	List<String> addresses;
	ArrayAdapter<String> addressesAdapter;
	EditText nameField;
	AutoCompleteTextView addressField;
	RadioGroup restaurantTypesGroup;
	EditText notesField;
	Restaurant currentRestaurant; // for Toast
	int progress; // for progress bar
	AtomicBoolean isActive = new AtomicBoolean(true);
	RestaurantHelper restaurantHelper;
		
	// A customized ArrayAdapter to customize it's getView behavior.
	class RestaurantsAdapter extends CursorAdapter {

		RestaurantsAdapter(Cursor c) {
			super(LunchListActivity.this, c);
		}

		@Override
		public void bindView(View row, Context context, Cursor cursor) {
			RestaurantHolder viewHolder = (RestaurantHolder) row.getTag();
			viewHolder.populateFrom(cursor, restaurantHelper);
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row;
			if (cursor.getString(3) == "delivery") {
				row = inflater.inflate(R.layout.row_delivery, parent, false);
			} else if (cursor.getString(3) == "take_out") {
				row = inflater.inflate(R.layout.row_take_out, parent, false);
			} else {
				row = inflater.inflate(R.layout.row_sit_down, parent, false);
			}
			RestaurantHolder viewHolder = new RestaurantHolder(row);
			row.setTag(viewHolder);
			return row;
		}

	}

	static class RestaurantHolder {

		private TextView name;
		private TextView address;

		RestaurantHolder(View row) {
			name = (TextView)row.findViewById(R.id.name);
			address = (TextView)row.findViewById(R.id.address);
		}

		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));
		}

	}

	private Runnable longTask = new Runnable() {
		public void run() {
			for (int i = 0; i < 10000 && isActive.get(); i += 200) {
				doSomeLongWork(200);
			}
			if (isActive.get()) {
				runOnUiThread(new Runnable() {
					public void run() {
						setProgressBarVisibility(false);
						progress = 0; // reset progress only if we are active (contrived)
					}
				});
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(LunchListActivity.this, "All done yo!", Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	};
	
    private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			currentRestaurant = new Restaurant();
			String address = ((EditText)findViewById(R.id.address)).getText().toString(); // used twice
			currentRestaurant.setName(((EditText)findViewById(R.id.name)).getText().toString());
			currentRestaurant.setAddress(address);
			currentRestaurant.setType(restaurantTypeFromRadioGroup((RadioGroup)findViewById(R.id.restaurantTypes)));
			currentRestaurant.setNotes(((EditText)findViewById(R.id.notes)).getText().toString());
			restaurantHelper.insert(currentRestaurant);
			addressesAdapter.add(address);
		}
    };

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		restaurants.moveToPosition(position);
    		currentRestaurant.setName(restaurantHelper.getName(restaurants));
    		currentRestaurant.setAddress(restaurantHelper.getAddress(restaurants));
    		currentRestaurant.setNotes(restaurantHelper.getNotes(restaurants));
    		currentRestaurant.setType(restaurantHelper.getType(restaurants));
    		nameField.setText(currentRestaurant.getName());
    		addressField.setText(currentRestaurant.getAddress());
    		if (currentRestaurant.getType() == "sit_down") {
    			restaurantTypesGroup.check(R.id.sit_down);
    		} else if (currentRestaurant.getType() == "take_out") {
    			restaurantTypesGroup.check(R.id.take_out);
    		} else {
    			restaurantTypesGroup.check(R.id.delivery);
    		}
    		getTabHost().setCurrentTab(1);
    	}
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main);
        restaurantHelper = new RestaurantHelper(this);
        
        instantiateFormElements();
        setTypeFaces();
        configureButton();
        configureRestaurantsList();
        configureAddressAutoComplete();
        configureTabs();
    }

	@Override
	public void onPause() {
		super.onPause();
		isActive.set(false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isActive.set(true);
		if (progress > 0) startWork();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		restaurantHelper.close();
	}
	
	private void startWork() {
		setProgressBarVisibility(true);
		new Thread(longTask).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.toast) {
			String message = getString(R.string.option_unselected_message);
			if (currentRestaurant != null) {
				message = currentRestaurant.getNotes();
			}
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return true;
		} else if (item.getItemId() == R.id.run) {
			startWork();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    private void instantiateFormElements() {
    	nameField = (EditText)findViewById(R.id.name);
        addressField = (AutoCompleteTextView)findViewById(R.id.address);
        restaurantTypesGroup = (RadioGroup)findViewById(R.id.restaurantTypes);
        notesField = (EditText)findViewById(R.id.name);
    }

    private void configureTabs() {
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.icon_list));
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.icon_restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
    }

    // Returns the restaurant type given a RadioGroup representing those types.
    private String restaurantTypeFromRadioGroup(RadioGroup group) {
    	if (group == null) return "";
    	switch (group.getCheckedRadioButtonId()) {
    		case R.id.sit_down:
    			return "sit_down";
    		case R.id.take_out:
    			return "take_out";
    		case R.id.delivery:
    			return "delivery";
    		default:
    			return "";
    	}
    }

    // For fun.
    private void setTypeFaces() {
        TextView field = (TextView)findViewById(R.id.name);
        Typeface font = Typeface.createFromAsset(getAssets(), "Alphabits-Regular.ttf");
        field.setTypeface(font);
    }

    // The main save button in this Activity.
    private void configureButton() {
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }

    // Creates and configures the ArrayAdapter for managing the ArrayList of Restaurant objects.
    private void configureRestaurantsList() {
    	restaurants = restaurantHelper.getAll();
    	startManagingCursor(restaurants);
        ListView restaurantList = (ListView)findViewById(R.id.restaurants);
        restaurantsAdapter = new RestaurantsAdapter(restaurants);
        restaurantList.setAdapter(restaurantsAdapter);
        restaurantList.setOnItemClickListener(onListClick);
    }

    // Create and configure the ArrayAdapter for managing the ArrayList of address strings.
    private void configureAddressAutoComplete() {
    	addresses = new ArrayList<String>();
        addressesAdapter = new ArrayAdapter<String>(this, R.layout.address_list_item_for_autocomplete, addresses);
        addressField.setAdapter(addressesAdapter);
    }
    
    private void doSomeLongWork(final int increment) {
    	runOnUiThread(new Runnable() {
    		public void run() {
    			progress += increment;
    			setProgress(progress);
    		}
    	});
    	SystemClock.sleep(250); // simulating long task
    }

}