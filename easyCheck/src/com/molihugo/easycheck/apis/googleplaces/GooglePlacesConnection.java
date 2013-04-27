package com.molihugo.easycheck.apis.googleplaces;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;



public class GooglePlacesConnection {
	
	public static SearchPlaceResult searchPlaces(double lat, double lon, double radius, String types) throws IOException{
		
		String location = lat+","+lon;
		String json;
		StringBuilder url = new StringBuilder();
		url.append("https://maps.googleapis.com/maps/api/place/search/json?location=");
		url.append(location);
		url.append("&radius=");
		url.append(radius);
		if(types != null){
			url.append("&types=");
			url.append(types);
		}
		url.append("&sensor=true&key=AIzaSyCzS6ydk7z_7cOuOfqnDt0k4FQCdRoT58c");
		URL google = new URL(url.toString());
		HttpURLConnection connection = (HttpURLConnection) google.openConnection();
		connection.setDoOutput(true);
		DataInputStream in = new DataInputStream(connection.getInputStream());
		java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
		json=s.hasNext() ? s.next() : "";
		System.out.println(json);
		Gson mGson = new Gson(); 
		SearchPlaceResult response = null;
		try {
			response = mGson.fromJson(json, SearchPlaceResult.class);
	    } catch (JsonSyntaxException ex) {
	    	System.out.println("/*ERROR DE PARSEO*/");
	    }
		return response;
	}
	
	public static PlaceDetailsResult getDetails(String reference) throws IOException{
		
		String json;
		String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?reference=";
		StringBuilder url = new StringBuilder(PLACES_DETAILS_URL);
		url.append(reference);
		url.append("&sensor=true&key=AIzaSyCzS6ydk7z_7cOuOfqnDt0k4FQCdRoT58c");
		
		URL google = new URL(url.toString());
		HttpURLConnection connection = (HttpURLConnection) google.openConnection();
		connection.setDoOutput(true);
		DataInputStream in = new DataInputStream(connection.getInputStream());
		java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
		json=s.hasNext() ? s.next() : "";
		System.out.println(json);
		Gson mGson = new Gson(); 
		PlaceDetailsResult response = null;
		try {
			response = mGson.fromJson(json, PlaceDetailsResult.class);
	    } catch (JsonSyntaxException ex) {
	    	System.out.println("/*ERROR DE PARSEO*/");
	    }
		
		return response;
		
	}
	

}
