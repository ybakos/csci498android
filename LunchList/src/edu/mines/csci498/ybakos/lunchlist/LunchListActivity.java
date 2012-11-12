package edu.mines.csci498.ybakos.lunchlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LunchListActivity extends FragmentActivity implements LunchListFragment.OnRestaurantListener {
	
	public static final String ID_EXTRA="edu.mines.csci498.ybakos.lunchlist._ID";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		LunchListFragment lunch = (LunchListFragment) getSupportFragmentManager().findFragmentById(R.id.lunch);
		lunch.setOnRestaurantListener(this);
	}
	
	public void onRestaurantSelected(long id) {
		Intent intent = new Intent(this, DetailForm.class);
		intent.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(intent);
	}
	
}