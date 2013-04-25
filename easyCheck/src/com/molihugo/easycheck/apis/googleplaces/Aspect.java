package com.molihugo.easycheck.apis.googleplaces;


public class Aspect {
	
	private String rating;
	private String type;
	
	

	public String getRating() {
		return rating;
	}



	public void setRating(String rating) {
		this.rating = rating;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public Aspect(String rating, String type) {
		super();
		this.rating = rating;
		this.type = type;
	}



	public Aspect() {
		// TODO Auto-generated constructor stub
	}

}
