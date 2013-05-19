package com.molihugo.easycheck;

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

import com.example.easycheck.R;
import com.molihugo.easycheck.apis.foursquare.FourSquareConnection;
import com.molihugo.easycheck.apis.googleplaces.GooglePlacesConnection;
import com.molihugo.easycheck.apis.googleplaces.SearchPlaceResult;
import com.molihugo.easycheck.apis.googleplaces.SearchPlaceResults;
import com.molihugo.easycheck.apis.yelp.BusinessDetail;
import com.molihugo.easycheck.apis.yelp.BusinessListBean;
import com.molihugo.easycheck.apis.yelp.YelpConnector;
import com.molihugo.easycheck.beans.Business;
import com.molihugo.easycheck.utils.AlertDialogManager;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class PlacesListActivity extends Activity {

	private LocationManager locationManager;
	private MyLocationListener listener;

	private AlertDialogManager alert = new AlertDialogManager();
	private SearchPlaceResult nearPlaces;
	private ArrayList<Business> places;
	private Button btnShowOnMap;
	private ProgressDialog pDialog;
	private ListView lv;
	private ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place

	private static double lon = 0, lat;

	private int ratio;
	private boolean apis[] = new boolean[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listener = new MyLocationListener();

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		final boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			// Build an alert dialog here that requests that the user enable
			// the location services, then when the user clicks the "OK" button,
			// call enableLocationSettings()
		}

		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, listener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, listener);

		setContentView(R.layout.activity_main);

		// get the variables passed by the previous activity.
		ratio = getIntent().getExtras().getInt("fondo");
		apis[0] = getIntent().getExtras().getBoolean("google");
		apis[1] = getIntent().getExtras().getBoolean("foursquare");
		apis[2] = getIntent().getExtras().getBoolean("yelp");

		if (lon == 0) {
			Log.d("ERROR Location", "NOT LOADED");
		} else {
			Log.d("Your Location", "latitude:" + lat + ", longitude: " + lon);
		}

		// Getting listview
		lv = (ListView) findViewById(R.id.list);

		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

		/** Button click event for shown on map */
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(PlacesListActivity.this,
						PlacesMapActivity.class);

				// Sending user current geo location

				i.putExtra("user_latitude", lat);
				i.putExtra("user_longitude", lon);
				// passing near places to map activity
				i.putExtra("near_places", places);
				i.putExtra("ref", "0");
				// staring activity
				startActivity(i);
			}
		});

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

				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SinglePlaceActivity.class);

				// Sending place refrence id to single place activity
				// place refrence id used to get "Place full details"
				in.putExtra(KEY_REFERENCE, reference);
				in.putExtra(KEY_NAME, name);
				// passing near places to map activity
				in.putExtra("near_places", places);

				startActivity(in);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Remove the listener you previously added
		locationManager.removeUpdates(listener);
	}

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
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
			lon = 0;
			pDialog = new ProgressDialog(PlacesListActivity.this);

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
			// googlePlaces = new GooglePlaces();

			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				//
				// String types = "cafe|restaurant"; // Listing places only
				// cafes, restaurants

				// Radius in meters - increase this value if you don't find any
				// places
				double radius = 1000; // 1000 meters

				Log.d("AAAAAAAAAAAAAAA", lat + "-lat & long-" + lon);

				while (lon == 0) {
				}
				;

				Log.d("AAAAAAAAAAAAAAA", lat + "-lat & long-" + lon);
				places = new ArrayList<Business>();

				// FROM GOOGLE PLACES
				if (apis[0]) {
					nearPlaces = GooglePlacesConnection.searchPlaces(lat, lon,
							radius, null);
					Log.d("GOOOGLEEEEPLACEEESS", nearPlaces.getResults()
							.toString()
							+ "Total de negocios:"
							+ nearPlaces.getResults().size());

					String status = nearPlaces.getStatus();

					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						if (nearPlaces.getResults() != null) {
							// loop through each place
							for (SearchPlaceResults p : nearPlaces.getResults()) {

								places.add(new Business(p));

							}
						} else if (status.equals("ZERO_RESULTS")) {
							// Zero results found
							alert.showAlertDialog(
									PlacesListActivity.this,
									"Near Places",
									"Sorry no places found. Try to change the types of places",
									false);
						} else if (status.equals("UNKNOWN_ERROR")) {
							alert.showAlertDialog(PlacesListActivity.this,
									"Places Error",
									"Sorry unknown error occured.", false);
						} else if (status.equals("OVER_QUERY_LIMIT")) {
							alert.showAlertDialog(
									PlacesListActivity.this,
									"Places Error",
									"Sorry query limit to google places is reached",
									false);
						} else if (status.equals("REQUEST_DENIED")) {
							alert.showAlertDialog(PlacesListActivity.this,
									"Places Error",
									"Sorry error occured. Request is denied",
									false);
						} else if (status.equals("INVALID_REQUEST")) {
							alert.showAlertDialog(PlacesListActivity.this,
									"Places Error",
									"Sorry error occured. Invalid Request",
									false);
						} else {
							alert.showAlertDialog(PlacesListActivity.this,
									"Places Error", "Sorry error occured.",
									false);
						}
					}
				}

				// FROM FOURSQUARE
				if (apis[1]) {

					Result<VenuesSearchResult> result = null;
					result = FourSquareConnection.search(lat, lon);

					if (result.getMeta().getCode() == 200) {
						// if query was ok we can finally we do something with
						// the
						// data
						for (CompactVenue venue : result.getResult()
								.getVenues()) {
							places.add(new Business(venue));
							;
						}
					} else {
						Log.d("FOUSQUAAAAREEEE", "Error occured: ");
						Log.d("FOUSQUAAAAREEEE", "  code: "
								+ result.getMeta().getCode());
						Log.d("FOUSQUAAAAREEEE", "  type: "
								+ result.getMeta().getErrorType());
						Log.d("FOUSQUAAAAREEEE", "  detail: "
								+ result.getMeta().getErrorDetail());
					}
				}

				// FROM YELP
				if (apis[2]) {
					new YelpConnector();
					BusinessListBean blb = YelpConnector.search("", lat, lon);
					Log.d("YEEEEEEEEELP", blb.getBusinesses().toString()
							+ "Total de negocios:" + blb.getTotal());

					for (BusinessDetail bD : blb.getBusinesses()) {

						places.add(new Business(bD));

					}

				}

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
					 * Updating parsed Places into LISTVIEW 683591264 618456527
					 * 681456527 juantxu sodupe
					 * */
					if (!places.isEmpty()) {
						// loop through each place
						for (Business b : places) {
							HashMap<String, String> map = new HashMap<String, String>();

							// Place reference won't display in listview -
							// it will be hidden
							// Place reference is used to get
							// "place full details"
							map.put(KEY_REFERENCE, b.getReference());
							// Place name
							map.put(KEY_NAME, b.getName());

							// adding HashMap to ArrayList
							placesListItems.add(map);
						}
						// list adapter
						ListAdapter adapter = new SimpleAdapter(
								PlacesListActivity.this, placesListItems,
								R.layout.list_item, new String[] {
										KEY_REFERENCE, KEY_NAME }, new int[] {
										R.id.reference, R.id.name });

						// Adding data into listview
						lv.setAdapter(adapter);
					} else {
						// Zero results found
						alert.showAlertDialog(
								PlacesListActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
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
