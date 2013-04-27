package com.molihugo.easycheck.apis.googleplaces;

import java.io.Serializable;


public class Location implements Serializable{
	private static final long serialVersionUID = 1L;
	private String lat;
	private String lng;
	
	public String getLat() {
	return lat;
}
public void setLat(String lat) {
	this.lat = lat;
}
public String getLng() {
	return lng;
}
public void setLng(String lng) {
	this.lng = lng;
}
	public Location(String lat, String lng) {
	super();
	this.lat = lat;
	this.lng = lng;
}
	public Location() {
		// TODO Auto-generated constructor stub
	}

}
