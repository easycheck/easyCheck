package com.molihugo.easycheck;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easycheck.R;
import com.molihugo.easycheck.beans.Business;
import com.molihugo.easycheck.utils.AlertDialogManager;
import com.molihugo.easycheck.utils.ConnectionDetector;

public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Progress dialog
	ProgressDialog pDialog;

	// Button
	Button btnContinue;
	
	private String reference, name;
	
	// Nearest places
	ArrayList<Business> nearPlaces;
	
	Business bu;

	private Button btnMap;

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);

		Intent i = getIntent();

		// Place referece id
		reference = i.getStringExtra(KEY_REFERENCE);
		name = i.getStringExtra(KEY_NAME);
		nearPlaces = (ArrayList<Business>) i.getSerializableExtra("near_places");

// Calling a Async Background thread
//		new LoadSinglePlaceDetails().execute(reference);
		
		for (Business b : nearPlaces) {
			
			if (b.getReference().equalsIgnoreCase(reference)){
				bu = new Business(b);
			}
			
		}

		TextView lbl_name = (TextView) findViewById(R.id.name);
		TextView lbl_address = (TextView) findViewById(R.id.address);
		TextView lbl_phone = (TextView) findViewById(R.id.phone);
		TextView lbl_location = (TextView) findViewById(R.id.location);
		TextView lbl_origin = (TextView) findViewById(R.id.TextView01);
		TextView lbl_category = (TextView) findViewById(R.id.category);

		lbl_name.setText(bu.getName());
		lbl_address.setText(bu.getAddress());
		lbl_category.setText(bu.getTypes().toString());
		lbl_phone.setText(bu.getPhoneNumber());
		String la, lo;
		if (bu.getLat().length() > 6) {
			la = bu.getLat().substring(0, 6);
		} else {
			la = bu.getLat();
		}
		if (bu.getLon().length() > 6) {
			lo = bu.getLon().substring(0, 6);
		} else {
			lo = bu.getLon();
		}
		lbl_location.setText(Html.fromHtml("<b>Lat:</b> " + la
				+ ", <b>Lon:</b> " + lo));
		lbl_origin.setText(bu.getApi().toString());

		/** button seguir **/
		btnContinue = (Button) findViewById(R.id.button3);

		/** Button seguir click event for continue with this business */
		btnContinue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						DatosCheckActivity.class);
				i.putExtra(KEY_REFERENCE, reference);
				i.putExtra(KEY_NAME, name);
				i.putExtra("business", bu);
				startActivity(i);
			}
		});
		
		/** button seguir **/
		btnMap = (Button) findViewById(R.id.button1);

		/** Button seguir click event for continue with this business */
		btnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						PlacesMapActivity.class);

				i.putExtra("user_latitude", Double.parseDouble(bu.getLat()));
				i.putExtra("user_longitude",  Double.parseDouble(bu.getLon()));
				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				i.putExtra("ref", bu.getReference());
				startActivity(i);
			}
		});
	}
	
/*
	 //* Background Async Task to Load Google places
	
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		
		//Before starting background thread Show Progress Dialog
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Cargando perfil ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		
		 // getting Profile JSON
		 
		protected String doInBackground(String... args) {
			String reference = args[0];

			// creating Places class object
//			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
//				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		
		 // After completing background task Dismiss the progress dialog
		 
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					 // Updating parsed Places into LISTVIEW
					 
					if (placeDetails != null) {
						String status = placeDetails.status;

						// check place deatils status
						// Check for all possible status
						if (status.equals("OK")) {
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double
										.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double
										.toString(placeDetails.result.geometry.location.lng);

								Log.d("Place ", name + address + phone
										+ latitude + longitude);

								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);

								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if
																			// name
																			// is
																			// null
																			// display
																			// as
																			// "Not present"
								address = address == null ? "Not present"
										: address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present"
										: latitude;
								longitude = longitude == null ? "Not present"
										: longitude;

								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html
										.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html
										.fromHtml("<b>Latitude:</b> "
												+ latitude
												+ ", <b>Longitude:</b> "
												+ longitude));
							}
						} else if (status.equals("ZERO_RESULTS")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Near Places", "Sorry no place found.",
									false);
						} else if (status.equals("UNKNOWN_ERROR")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry unknown error occured.", false);
						} else if (status.equals("OVER_QUERY_LIMIT")) {
							alert.showAlertDialog(
									SinglePlaceActivity.this,
									"Places Error",
									"Sorry query limit to google places is reached",
									false);
						} else if (status.equals("REQUEST_DENIED")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry error occured. Request is denied",
									false);
						} else if (status.equals("INVALID_REQUEST")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry error occured. Invalid Request",
									false);
						} else {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error", "Sorry error occured.",
									false);
						}
					} else {
						alert.showAlertDialog(SinglePlaceActivity.this,
								"Places Error", "Sorry error occured.", false);
					}

				}
			});

		}

	}
*/
}