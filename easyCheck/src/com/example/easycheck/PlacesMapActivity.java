package com.example.easycheck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.easycheck.bean.Place;
import com.example.easycheck.bean.PlacesList;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlacesMapActivity extends android.support.v4.app.FragmentActivity {

	// Nearest places
	PlacesList nearPlaces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Getting intent data
		Intent i = getIntent();

		// Users current geo location
		String user_latitude = i.getExtras().getString("user_latitude");
		String user_longitude = i.getExtras().getString("user_longitude");

		// Near places list
		nearPlaces = (PlacesList) i.getSerializableExtra("near_places");
		
		GoogleMap mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		
		//MAP_TYPE_NORMAL / MAP_TYPE_HYBRID / MAP_TYPE_SATELLITE / MAP_TYPE_TERRAIN

		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		LatLng ll = new LatLng(Double.parseDouble(user_latitude), Double.parseDouble(user_longitude));
		CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(ll);
		
		mapa.moveCamera(camUpd1);
		
		camUpd1 = CameraUpdateFactory.zoomTo(15);
		mapa.moveCamera(camUpd1);
		
		for (Place place : nearPlaces.results) {
			  double latitude = place.geometry.location.lat; // latitude 
			  double longitude = place.geometry.location.lng; // longitude
			  
			  // Map item 
			 mapa.addMarker(new MarkerOptions()
		        .position(new LatLng(latitude, longitude))
		        .title("Negocio: "+place.name)
		        .snippet("Direccion: "+place.vicinity));
			  }
		
		/* mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
		    public boolean onMarkerClick(Marker marker) {
		        Toast.makeText(
		            PlacesMapActivity.this,
		            "Marcador pulsado:\n" +
		            marker.getTitle(),
		            Toast.LENGTH_SHORT).show();
		 
		        return false;
		    }
		});
		*/
	}
}