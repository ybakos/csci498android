package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.graphics.Typeface;

public class LunchListActivity extends Activity {
    
	Restaurant r = new Restaurant();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView field = (TextView)findViewById(R.id.name);  
        Typeface font = Typeface.createFromAsset(getAssets(), "Alphabits-Regular.ttf");  
        field.setTypeface(font);
        
        addLotsOfRadioButtons();
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
    		public void onClick(View v) {
    			EditText name = (EditText)findViewById(R.id.name);
    			EditText address = (EditText)findViewById(R.id.address);
    			RadioGroup types = (RadioGroup)findViewById(R.id.restaurantTypes);
    			
    			r.setName(name.getText().toString());
    			r.setAddress(address.getText().toString());
    			r.setType(restaurantTypeFromRadioGroup(types));
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
    
    private void addLotsOfRadioButtons() {
    	RadioGroup restaurantTypes = (RadioGroup)findViewById(R.id.restaurantTypes);
        for (int i = 0; i < 10; ++i) {
        	RadioButton rb = new RadioButton(this);
        	rb.setText(Integer.toString(i));
        	restaurantTypes.addView(rb);
        }
    }
    
}