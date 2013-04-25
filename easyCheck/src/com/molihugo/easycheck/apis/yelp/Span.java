package com.molihugo.easycheck.apis.yelp;

public class Span {
	private String latitude_delta;
	private String longitude_delta;
	private Center center;

	public Span(String latitude_delta, String longitude_delta, Center center) {
		super();
		this.latitude_delta = latitude_delta;
		this.longitude_delta = longitude_delta;
		this.center = center;
	}

	public String getLatitude_delta() {
		return latitude_delta;
	}

	public void setLatitude_delta(String latitude_delta) {
		this.latitude_delta = latitude_delta;
	}

	public String getLongitude_delta() {
		return longitude_delta;
	}

	public void setLongitude_delta(String longitude_delta) {
		this.longitude_delta = longitude_delta;
	}

	public Center getCenter() {
		return center;
	}

	public void setCenter(Center center) {
		this.center = center;
	}

}
