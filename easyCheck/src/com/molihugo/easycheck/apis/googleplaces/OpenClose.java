package com.molihugo.easycheck.apis.googleplaces;


public class OpenClose {
	
	private String day;
	private String time;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public OpenClose(String day, String time) {
		super();
		this.day = day;
		this.time = time;
	}

	public OpenClose() {
		// TODO Auto-generated constructor stub
	}

}
