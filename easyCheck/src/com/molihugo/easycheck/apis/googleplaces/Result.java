package com.molihugo.easycheck.apis.googleplaces;

import java.util.List;

public class Result {
	
	private List<AddressComponents> address_components;
	private String formatted_address;
	private String formatted_phone_number;
	private Geometry geometry;
	private String icon;
	private String id;
	private String international_phone_number;
	private String name;
	private OpeningHoursPlaceDetails opening_hours;
	private List<Photo> photos;
	private String rating;
	private String reference;
	private List<Review> reviews;
	
	
	
	
	public Result(List<AddressComponents> address_components,
			String formatted_address, String formatted_phone_number,
			Geometry geometry, String icon, String id,
			String international_phone_number, String name,
			OpeningHoursPlaceDetails opening_hours, List<Photo> photos,
			String rating, String reference, List<Review> reviews,
			List<String> types, String url, String utc_offset, String vicinity,
			String website) {
		super();
		this.address_components = address_components;
		this.formatted_address = formatted_address;
		this.formatted_phone_number = formatted_phone_number;
		this.geometry = geometry;
		this.icon = icon;
		this.id = id;
		this.international_phone_number = international_phone_number;
		this.name = name;
		this.opening_hours = opening_hours;
		this.photos = photos;
		this.rating = rating;
		this.reference = reference;
		this.reviews = reviews;
		this.types = types;
		this.url = url;
		this.utc_offset = utc_offset;
		this.vicinity = vicinity;
		this.website = website;
	}

	public List<AddressComponents> getAddress_components() {
		return address_components;
	}

	public void setAddress_components(List<AddressComponents> address_components) {
		this.address_components = address_components;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getFormatted_phone_number() {
		return formatted_phone_number;
	}

	public void setFormatted_phone_number(String formatted_phone_number) {
		this.formatted_phone_number = formatted_phone_number;
	}

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

	public String getInternational_phone_number() {
		return international_phone_number;
	}

	public void setInternational_phone_number(String international_phone_number) {
		this.international_phone_number = international_phone_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OpeningHoursPlaceDetails getOpening_hours() {
		return opening_hours;
	}

	public void setOpening_hours(OpeningHoursPlaceDetails opening_hours) {
		this.opening_hours = opening_hours;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
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

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUtc_offset() {
		return utc_offset;
	}

	public void setUtc_offset(String utc_offset) {
		this.utc_offset = utc_offset;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	private List<String> types;
	private String url;
	private String utc_offset;
	private String vicinity;
	private String website;

	public Result() {
		// TODO Auto-generated constructor stub
	}

}
