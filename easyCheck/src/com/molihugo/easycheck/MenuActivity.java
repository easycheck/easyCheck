package com.molihugo.easycheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.easycheck.R;
import com.molihugo.easycheck.utils.AlertDialogManager;
import com.molihugo.easycheck.utils.ConnectionDetector;

public class MenuActivity extends Activity {

	// variables for internet connection check
	private Boolean isInternetPresent = false;
	private ConnectionDetector cd;
	private AlertDialogManager alert = new AlertDialogManager();

	// view components
	private Button btnCheck, btnReview;

	// settings
	private boolean apis[] = new boolean[3];
	private String ratio = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cd = new ConnectionDetector(getApplicationContext());

		setContentView(R.layout.activity_first);

		PreferenceManager.setDefaultValues(this, R.xml.opciones, false);

		/** button check **/
		btnCheck = (Button) findViewById(R.id.button3);

		/** Button check click event for showing the business around you */
		btnCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						PlacesListActivity.class);
				i.putExtra("google", apis[0]);
				i.putExtra("foursquare", apis[1]);
				i.putExtra("yelp", apis[2]);
				i.putExtra("radio", ratio);
				startActivity(i);
			}
		});

		/** button jornada **/
		btnReview = (Button) findViewById(R.id.button2);

		/** para mostrar los checkins sin almacenar de la jornada actual */
		btnReview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						ReviewActivity.class);
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
			getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).edit()
					.putString(LoginActivity.PREF_ID, null).commit();
			onResume();
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(
					MenuActivity.this,
					"Error de conectividad",
					"Por favor, revise su conexión de datos y vuelva a intentarlo",
					false);
			btnCheck.setEnabled(false);
			return;
		}

		SharedPreferences pref = getSharedPreferences(LoginActivity.PREFS_NAME,
				MODE_PRIVATE);
		String username = pref.getString(LoginActivity.PREF_ID, null);

		if (username == null) {
			Intent i = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(i);
			this.finish();
		}

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		apis[0] = sharedPrefs.getBoolean("Google", false);
		apis[1] = sharedPrefs.getBoolean("Foursquare", false);
		apis[2] = sharedPrefs.getBoolean("Yelp", false);
		ratio = sharedPrefs.getString("Radio", "1000");
		Log.d("APIS-0", String.valueOf(apis[0]));
		Log.d("APIS-1", String.valueOf(apis[1]));
		Log.d("APIS-2", String.valueOf(apis[2]));

	}

}