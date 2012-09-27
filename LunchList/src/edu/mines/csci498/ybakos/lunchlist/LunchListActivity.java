package edu.mines.csci498.ybakos.lunchlist;

import android.app.TabActivity;
import android.widget.*;
import android.view.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.graphics.Typeface;
import android.util.Log;
import java.util.*;

@SuppressWarnings("deprecation")
public class LunchListActivity extends TabActivity {

	List<Restaurant> restaurants;
	RestaurantsAdapter restaurantsAdapter;
	List<String> addresses;
	ArrayAdapter<String> addressesAdapter;
	EditText nameField;
	AutoCompleteTextView addressField;
	RadioGroup restaurantTypesGroup;
	EditText notesField;
	Restaurant currentRestaurant; // for Toast
	int progress; // for progress bar

	// A customized ArrayAdapter to customize it's getView behavior.
	class RestaurantsAdapter extends ArrayAdapter<Restaurant> {

		RestaurantsAdapter() {
			super(LunchListActivity.this, android.R.layout.simple_list_item_1, restaurants);
		}

		// Sets the icon, name and address of the Restaurant for the view.
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder viewHolder;

			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				if (restaurants.get(position).getType() == "delivery") {
					row = inflater.inflate(R.layout.row_delivery, null);
				} else if (restaurants.get(position).getType() == "take_out") {
					row = inflater.inflate(R.layout.row_take_out, null);
				} else {
					row = inflater.inflate(R.layout.row_sit_down, null);
				}
				viewHolder = new RestaurantHolder(row);
				row.setTag(viewHolder);
			} else {
				viewHolder = (RestaurantHolder)row.getTag();
			}

			viewHolder.populateFrom(restaurants.get(position));

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

		void populateFrom(Restaurant r) {
			Log.d("LunchList", r.getNotes());
			name.setText(r.getName());
			address.setText(r.getAddress());
		}

	}

	private Runnable longTask = new Runnable() {
		public void run() {
			for (int i = 0; i < 20; i++) {
				doSomeLongWork(500);
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
			restaurantsAdapter.add(currentRestaurant);
			addressesAdapter.add(address);
		}
    };

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		currentRestaurant = restaurants.get(position);
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

        instantiateFormElements();
        setTypeFaces();
        configureButton();
        configureRestaurantsList();
        configureAddressAutoComplete();
        configureTabs();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.toast) {
			String message= "No restaurant selected";
			if (currentRestaurant != null) {
				message = currentRestaurant.getNotes();
			}
			
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			
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
    	restaurants = new ArrayList<Restaurant>();
        ListView restaurantList = (ListView)findViewById(R.id.restaurants);
        restaurantsAdapter = new RestaurantsAdapter();
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
    	SystemClock.sleep(250); // simulating long task
    }

}