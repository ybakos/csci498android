package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ArrayAdapter;
import android.graphics.Typeface;
import java.util.List;
import java.util.ArrayList;

public class LunchListActivity extends Activity {
    
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	ArrayAdapter<Restaurant> restaurantsAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView field = (TextView)findViewById(R.id.name);  
        Typeface font = Typeface.createFromAsset(getAssets(), "Alphabits-Regular.ttf");  
        field.setTypeface(font);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView restaurantList = (ListView)findViewById(R.id.restaurants);
        restaurantsAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, restaurants);
        restaurantList.setAdapter(restaurantsAdapter);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
    		public void onClick(View v) {
    			Restaurant r = new Restaurant();
    			
    			EditText name = (EditText)findViewById(R.id.name);
    			EditText address = (EditText)findViewById(R.id.address);
    			RadioGroup types = (RadioGroup)findViewById(R.id.restaurantTypes);
    			
    			r.setName(name.getText().toString());
    			r.setAddress(address.getText().toString());
    			r.setType(restaurantTypeFromRadioGroup(types));
    			
    			restaurantsAdapter.add(r);
    		}
    	};
    	
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
    
}