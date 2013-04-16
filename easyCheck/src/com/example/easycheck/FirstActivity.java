package com.example.easycheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.easycheck.utils.AlertDialogManager;
import com.example.easycheck.utils.ConnectionDetector;

public class FirstActivity extends Activity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	// Button
	Button btnCheck;
	
	boolean apis[] = new boolean[3];
	String fondo = null, nombre = null;
	int color;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(FirstActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		/** button check **/
		btnCheck = (Button) findViewById(R.id.button1);

		/** Button check click event for showing the business around you */
		btnCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				i.putExtra("google", apis[0]);
				i.putExtra("foursquare", apis[1]);
				i.putExtra("yelp", apis[2]);
				i.putExtra("fondo", color);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, R.string.menu_estats);
		menu.add(Menu.NONE, 1, 0, R.string.menu_settings);
		menu.add(Menu.NONE, 2, 0, R.string.menu_logOut);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this, StatsActivity.class));
			return true;

		case 1:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;

		case 2:
			getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE)
	        .edit()
	        .putString(LoginActivity.PREF_USER, null)
	        .commit();
			onResume();
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences pref = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);   
		String username = pref.getString(LoginActivity.PREF_USER, null);

		if (username == null) {
			Intent i = new Intent(getApplicationContext(),
					LoginActivity.class);
			this.finish();
			startActivity(i);
		}
		
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		apis[0] = sharedPrefs.getBoolean("Google", false);
		apis[1] = sharedPrefs.getBoolean("Foursquare", false);
		apis[2] = sharedPrefs.getBoolean("Yelp", false);
		fondo = sharedPrefs.getString("Fondo", "negro");

		if (fondo.equalsIgnoreCase("azul")) {
			color = Color.BLUE;
		} else if (fondo.equalsIgnoreCase("blanco")) {
			color = Color.WHITE;
		} else if (fondo.equalsIgnoreCase("negro")) {
			color = Color.BLACK;
		}

	}

}