package com.molihugo.easycheck.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.molihugo.easycheck.apis.googleplaces.SearchPlaceResults;
import com.molihugo.easycheck.apis.yelp.BusinessDetail;

import fi.foyt.foursquare.api.entities.CompactVenue;

public class Business implements Serializable {

	public enum ApiProcedence {
		GPLACES, YELP, FOUSQUARE;
		
		public String toString(){
			if (this.equals(GPLACES)){
				return "GooglePlaces";
			} else if (this.equals(YELP)){
				return "Yelp";
			} else if (this.equals(FOUSQUARE)){
				return "Foursquare";
			} else return "unknown";
			
		}
	}

	private static final long serialVersionUID = 1L;

	private ApiProcedence api;

	private String reference;

	private String name;
	private String address;
	private List<String> types;
	private String phoneNumber;
	private String lat, lon;
	private String email;
	private String description;
	private String openingHours;

	public Business(ApiProcedence api, String reference, String name, String address,
			List<String> types, String phoneNumber, String email,
			String description, String lat, String lon, String openingHours) {
		super();
		this.api = api;
		this.reference = reference;
		this.name = name;
		this.address = address;
		this.types = types;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.description = description;
		this.lat = lat;
		this.lon = lon;
		this.openingHours = openingHours;
	}

	public Business(SearchPlaceResults googleBusiness) {
		super();
		this.api = ApiProcedence.GPLACES;
		this.reference = googleBusiness.getReference();
		this.name = googleBusiness.getName();
		this.address = googleBusiness.getVicinity();
		this.types = googleBusiness.getTypes();
		this.phoneNumber = null;
		this.email = null;
		this.description = null;
		this.lat = googleBusiness.getGeometry().getLocation().getLat();
		this.lon = googleBusiness.getGeometry().getLocation().getLng();
//		this.openingHours = googleBusiness.getOpening_hours().toString();

	}

	public Business(BusinessDetail yelpBusiness) {
		super();
		this.api = ApiProcedence.YELP;
		this.reference = yelpBusiness.getId();
		this.name = yelpBusiness.getName();
		this.address = yelpBusiness.getLocation().getAddress().toString();
		this.types = new ArrayList<String>();
		List<String> t;
		if (yelpBusiness.getCategories()!=null){
			for (int i = 0; i<yelpBusiness.getCategories().size(); i++){
				t = yelpBusiness.getCategories().get(i);
				for (int j = 0; j<t.size(); j++){
					this.types.add(t.get(j));
				}
				
			}
		}
		this.phoneNumber = yelpBusiness.getDisplayPhone();
		this.email = null;
		this.description = yelpBusiness.getSnippetText();
		this.lat = String.valueOf(yelpBusiness.getLocation().getCoordinate()
				.getLatitude());
		this.lon = String.valueOf(yelpBusiness.getLocation().getCoordinate()
				.getLongitude());
		this.openingHours = String.valueOf(yelpBusiness.isClosed());

	}
	
	public Business(CompactVenue foursquareBusiness) {
		super();
		this.api = ApiProcedence.FOUSQUARE;
		this.reference = foursquareBusiness.getId();
		this.name = foursquareBusiness.getName();
		this.address = foursquareBusiness.getLocation().getAddress();
		this.types = new ArrayList<String>();
		for (int i = 0; i<foursquareBusiness.getCategories().length; i++){
			this.types.add(foursquareBusiness.getCategories()[i].getName());
		}
		this.phoneNumber = foursquareBusiness.getContact().getPhone();
		this.email = foursquareBusiness.getContact().getEmail();
		this.description = null;
		this.lat = String.valueOf(foursquareBusiness.getLocation().getLat());
		this.lon = String.valueOf(foursquareBusiness.getLocation().getLng());
		this.openingHours = null;

	}

	public Business(Business b) {
		super();
		this.api = b.api;
		this.reference = b.reference;
		this.name = b.name;
		this.address = b.address;
		this.types = b.types;
		this.phoneNumber = b.phoneNumber;
		this.email = b.email;
		this.description = b.description;
		this.lat = b.lat;
		this.lon = b.lon;
		this.openingHours = b.openingHours;
	}

	public ApiProcedence getApi() {
		return api;
	}

	public void setApi(ApiProcedence api) {
		this.api = api;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}
	

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

}
