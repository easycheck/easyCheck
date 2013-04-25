package com.molihugo.easycheck.apis.googleplaces;

import java.util.List;

public class SearchPlaceResults {
	
	private Geometry geometry;
	private String icon;
	private String id;
	private String name;
	private OpeningHours opening_hours;
	private List<Photo> photos;
	private String price_level;
	private String rating;
	private String reference;
	private List<String> types;
	private String vicinity;
	
	
	

	public Geometry getGeometry() {
		return geometry;
	}




	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}




	public String getIcon() {
		return icon;
	}




	public void setIcon(String icon) {
		this.icon = icon;
	}




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public OpeningHours getOpening_hours() {
		return opening_hours;
	}




	public void setOpening_hours(OpeningHours opening_hours) {
		this.opening_hours = opening_hours;
	}




	public List<Photo> getPhotos() {
		return photos;
	}




	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}




	public String getPrice_level() {
		return price_level;
	}




	public void setPrice_level(String price_level) {
		this.price_level = price_level;
	}




	public String getRating() {
		return rating;
	}




	public void setRating(String rating) {
		this.rating = rating;
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




	public String getVicinity() {
		return vicinity;
	}




	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}




	public SearchPlaceResults(Geometry geometry, String icon, String id,
			String name, OpeningHours opening_hours, List<Photo> photos,
			String price_level, String rating, String reference,
			List<String> types, String vicinity) {
		super();
		this.geometry = geometry;
		this.icon = icon;
		this.id = id;
		this.name = name;
		this.opening_hours = opening_hours;
		this.photos = photos;
		this.price_level = price_level;
		this.rating = rating;
		this.reference = reference;
		this.types = types;
		this.vicinity = vicinity;
	}




	public SearchPlaceResults() {
		// TODO Auto-generated constructor stub
	}

}
