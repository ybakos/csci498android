package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.view.*;
import android.widget.*;

public class DetailForm extends Activity {

	EditText name;
	EditText address;
	EditText notes;
	RadioGroup types;
	RestaurantHelper helper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		helper = new RestaurantHelper(this);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.address);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.restaurantTypes);

		Button save = (Button) findViewById(R.id.save);

		save.setOnClickListener(onSave);
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
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
		}
	};
	
}
