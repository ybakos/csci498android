package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.graphics.Typeface;
import java.util.List;
import java.util.ArrayList;

public class LunchListActivity extends Activity {
    
	List<Restaurant> restaurants;
	RestaurantsAdapter restaurantsAdapter;
	List<String> addresses;
	ArrayAdapter<String> addressesAdapter;
	
	// A customized ArrayAdapter to customize it's getView behavior.
	class RestaurantsAdapter extends ArrayAdapter<Restaurant> {
	
		RestaurantsAdapter() {
			super(LunchListActivity.this, android.R.layout.simple_list_item_1, restaurants);
		}
		
		// Sets the icon, name and address of the Restaurant for the view.
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.row, null);
			}
			Restaurant r = restaurants.get(position);
			((TextView)row.findViewById(R.id.name)).setText(r.getName());
			((TextView)row.findViewById(R.id.address)).setText(r.getAddress());
			
			ImageView icon = (ImageView)row.findViewById(R.id.icon);
			
			if (r.getType().equals("sit_down")) {
				icon.setImageResource(R.drawable.icon_sit_down);
			} else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.icon_take_out);
			} else {
				icon.setImageResource(R.drawable.icon_delivery);
			}
			
			return row;
		}
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setTypeFaces();
        configureButton();
        configureRestaurantsList();
        configureAddressAutoComplete();
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
    		public void onClick(View v) {
    			Restaurant r = new Restaurant();
    			String address = ((EditText)findViewById(R.id.address)).getText().toString(); // used twice
    			r.setName(((EditText)findViewById(R.id.name)).getText().toString());
    			r.setAddress(address);
    			r.setType(restaurantTypeFromRadioGroup((RadioGroup)findViewById(R.id.restaurantTypes)));
    			restaurantsAdapter.add(r);
    			addressesAdapter.add(address);
    		}
    	};
    
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
    }
    
    // Create and configure the ArrayAdapter for managing the ArrayList of address strings.
    private void configureAddressAutoComplete() {
    	addresses = new ArrayList<String>();
    	AutoCompleteTextView address = (AutoCompleteTextView) findViewById(R.id.address);
        addressesAdapter = new ArrayAdapter<String>(this, R.layout.address_list_item_for_autocomplete, addresses);
        address.setAdapter(addressesAdapter);
    }
        
}