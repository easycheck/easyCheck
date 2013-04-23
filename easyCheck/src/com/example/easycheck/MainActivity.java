package com.example.easycheck;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.easycheck.bean.Place;
import com.example.easycheck.bean.PlacesList;
import com.example.easycheck.utils.AlertDialogManager;
import com.example.easycheck.utils.GooglePlaces;

public class MainActivity extends Activity {

	private LocationManager locationManager;

	private static double lon = 0, lat;


	AlertDialogManager alert = new AlertDialogManager();
	GooglePlaces googlePlaces;
	PlacesList nearPlaces;
	Button btnShowOnMap;
	ProgressDialog pDialog;
	ListView lv;
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	int fondo;
	boolean apis[] = new boolean[3];

	MyLocationListener listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listener = new MyLocationListener();
		
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		 final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		    if (!gpsEnabled) {
		        // Build an alert dialog here that requests that the user enable
		        // the location services, then when the user clicks the "OK" button,
		        // call enableLocationSettings()
		    }
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

		setContentView(R.layout.activity_main);

		// get the variables passed by the previous activity.
		fondo = getIntent().getExtras().getInt("fondo");
		apis[0] = getIntent().getExtras().getBoolean("google");
		apis[1] = getIntent().getExtras().getBoolean("foursquare");
		apis[2] = getIntent().getExtras().getBoolean("yelp");
		
		if (lon == 0) {
			Log.d("ERROR Location", "NOT LOADED");
		} else {
			Log.d("Your Location", "latitude:" + lat
					+ ", longitude: " + lon);
		}
		
		// Getting listview
		lv = (ListView) findViewById(R.id.list);

		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);
		
		/** Button click event for shown on map */
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(MainActivity.this,
						PlacesMapActivity.class);

				// Sending user current geo location

				i.putExtra("user_latitude", lat);
				i.putExtra("user_longitude", lon);
				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();

		/**
		 * ListItem click event On selecting a listitem SinglePlaceActivity is
		 * launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String reference = ((TextView) view
						.findViewById(R.id.reference)).getText().toString();
				
				String name = ((TextView) view
						.findViewById(R.id.name)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SinglePlaceActivity.class);

				// Sending place refrence id to single place activity
				// place refrence id used to get "Place full details"
				in.putExtra(KEY_REFERENCE, reference);
				in.putExtra(KEY_NAME, name);
				startActivity(in);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Remove the listener you previously added
		locationManager.removeUpdates(listener);
	}
	
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lon=0;
			pDialog = new ProgressDialog(MainActivity.this);

			pDialog.setMessage(Html
					.fromHtml("<b>Busqueda</b><br/>Cargando Negocios..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				//
				//String types = "cafe|restaurant"; // Listing places only cafes, restaurants

				// Radius in meters - increase this value if you don't find any places
				double radius = 1000; // 1000 meters

				Log.d("AAAAAAAAAAAAAAA", lat + "-lat & long-" + lon);
				
				while (lon == 0){
				};
				
				Log.d("AAAAAAAAAAAAAAA", lat + "-lat & long-" + lon);

				// get nearest places
				nearPlaces = googlePlaces.search(lat, lon, radius, null);

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("AAAAAAAAAAAAAAA", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;

					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();

								// Place reference won't display in listview -
								// it will be hidden
								// Place reference is used to get
								// "place full details"
								map.put(KEY_REFERENCE, p.reference);

								// Place name
								map.put(KEY_NAME, p.name);

								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(
									MainActivity.this, placesListItems,
									R.layout.list_item, new String[] {
											KEY_REFERENCE, KEY_NAME },
									new int[] { R.id.reference, R.id.name });

							// Adding data into listview
							lv.setAdapter(adapter);
						}
					} else if (status.equals("ZERO_RESULTS")) {
						// Zero results found
						alert.showAlertDialog(
								MainActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					} else if (status.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(MainActivity.this,
								"Places Error", "Sorry unknown error occured.",
								false);
					} else if (status.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(
								MainActivity.this,
								"Places Error",
								"Sorry query limit to google places is reached",
								false);
					} else if (status.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(MainActivity.this,
								"Places Error",
								"Sorry error occured. Request is denied", false);
					} else if (status.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(MainActivity.this,
								"Places Error",
								"Sorry error occured. Invalid Request", false);
					} else {
						alert.showAlertDialog(MainActivity.this,
								"Places Error", "Sorry error occured.", false);
					}
				}
			});

		}

	}

	private class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			lon = location.getLongitude();
			lat = location.getLatitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

}
