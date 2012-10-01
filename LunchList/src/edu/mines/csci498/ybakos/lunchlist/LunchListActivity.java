package edu.mines.csci498.ybakos.lunchlist;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

@SuppressWarnings("deprecation")
public class LunchListActivity extends TabActivity {
	Cursor model;
	RestaurantAdapter adapter;
	EditText name;
	EditText address;
	EditText notes;
	RadioGroup types;
	RestaurantHelper helper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		helper = new RestaurantHelper(this);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.address);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.restaurantTypes);

		Button save = (Button) findViewById(R.id.save);

		save.setOnClickListener(onSave);

		ListView list = (ListView) findViewById(R.id.restaurants);

		model = helper.getAll();
		startManagingCursor(model);
		adapter = new RestaurantAdapter(model);
		list.setAdapter(adapter);

		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");

		spec.setContent(R.id.restaurants);
		spec.setIndicator("List", getResources().getDrawable(R.drawable.icon_list));
		getTabHost().addTab(spec);

		spec = getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details", getResources().getDrawable(R.drawable.icon_restaurant));
		getTabHost().addTab(spec);

		getTabHost().setCurrentTab(0);

		list.setOnItemClickListener(onListClick);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
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
			helper.insert(name.getText().toString(), address.getText().toString(),
						  type, notes.getText().toString());
			model.requery();
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(LunchListActivity.this, DetailForm.class);
			startActivity(intent);
		}
	};

	class RestaurantAdapter extends CursorAdapter {
		
		RestaurantAdapter(Cursor c) {
			super(LunchListActivity.this, c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();

			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
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
}