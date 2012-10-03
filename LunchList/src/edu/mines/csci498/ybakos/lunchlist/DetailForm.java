package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class DetailForm extends Activity {

	EditText name;
	EditText address;
	EditText notes;
	RadioGroup types;
	RestaurantHelper helper;
	String restaurantId;
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			Log.d("LUNCHLIST:", "onSave called");
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
				helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString());
			} else {
				helper.update(restaurantId, name.getText().toString(),
										    address.getText().toString(),
										    type,
										    notes.getText().toString());
			}
			finish();
		}
	};
	
	
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
		
		restaurantId = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		if (restaurantId != null) load();
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		types.check(state.getInt("type"));
	}
	
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	private void load() {
		Cursor c = helper.getById(restaurantId);
		c.moveToFirst();

		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}

		c.close();
	}

}