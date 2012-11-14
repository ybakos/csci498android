package edu.mines.csci498.ybakos.lunchlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetailForm extends FragmentActivity {

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.detail_activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		String restaurantId = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		if (restaurantId != null) {
			DetailFragment details = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.details);
			if (details != null) details.loadRestaurant(restaurantId);
		}
	}
	
}