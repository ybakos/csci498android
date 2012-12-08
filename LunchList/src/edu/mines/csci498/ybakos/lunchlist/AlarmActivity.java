package edu.mines.csci498.ybakos.lunchlist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class AlarmActivity extends Activity implements MediaPlayer.OnPreparedListener {

	MediaPlayer player;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		player = new MediaPlayer();
		setContentView(R.layout.alarm);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String sound = preferences.getString("alarm_ringtone", null);
		if (sound != null) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			try {
				player.setDataSource(sound);
				player.setOnPreparedListener(this);
				player.prepareAsync();
			} catch (Exception e) {
				Log.e("LunchList", "Exception in playing ringtone", e);
			}
		}
	}
	
	@Override
	public void onPause() {
		if (player.isPlaying()) {
			player.stop();
		}
		super.onPause();
	}
	
	public void onPrepared(MediaPlayer player) {
		player.start();
	}
	
}
