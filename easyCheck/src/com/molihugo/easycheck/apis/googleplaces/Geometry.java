package com.molihugo.easycheck.apis.googleplaces;

import java.io.Serializable;


public class Geometry implements Serializable{
	private static final long serialVersionUID = 1L;
	private Location location;
	

	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public Geometry(Location location) {
		super();
		this.location = location;
	}


	public Geometry() {
		// TODO Auto-generated constructor stub
	}

}
