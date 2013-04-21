package com.example.easycheck;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;

import com.example.easycheck.bean.Place;
import com.example.easycheck.bean.PlacesList;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlacesMapActivity extends android.support.v4.app.FragmentActivity {

	// Nearest places
	PlacesList nearPlaces;
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Getting intent data
		Intent i = getIntent();

		// Users current geo location
		double user_latitude = i.getExtras().getDouble("user_latitude");
		double user_longitude = i.getExtras().getDouble("user_longitude");

		// Near places list
		nearPlaces = (PlacesList) i.getSerializableExtra("near_places");

		GoogleMap mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		// MAP_TYPE_NORMAL / MAP_TYPE_HYBRID / MAP_TYPE_SATELLITE /
		// MAP_TYPE_TERRAIN

		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng ll = new LatLng(user_latitude, user_longitude);
		CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(ll);

		mapa.moveCamera(camUpd1);

		camUpd1 = CameraUpdateFactory.zoomTo(15);
		mapa.moveCamera(camUpd1);

		final HashMap<String, String> map = new HashMap<String, String>();

		for (Place place : nearPlaces.results) {
			double latitude = place.geometry.location.lat; // latitude
			double longitude = place.geometry.location.lng; // longitude

			MarkerOptions m = new MarkerOptions()
					.position(new LatLng(latitude, longitude))
					.title(place.name)
					.snippet(place.vicinity);

			// Map item
			Marker marker = mapa.addMarker(m);

			map.put(marker.getId(), place.reference);
		}

		mapa.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {

				Intent in = new Intent(getApplicationContext(),
						SinglePlaceActivity.class);

				// Sending place refrence id to single place activity
				// place refrence id used to get "Place full details"
				String ref = map.get(marker.getId());
				String name = marker.getTitle();

				in.putExtra(KEY_REFERENCE, ref);
				in.putExtra(KEY_NAME, name);
				startActivity(in);
			}
		});

	}
}