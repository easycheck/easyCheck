package com.molihugo.easycheck.apis.googleplaces;

import java.util.List;

public class OpeningHoursPlaceDetails {
	
	private String open_now;
	private List<Period> periods;
	
	

	public OpeningHoursPlaceDetails(String open_now, List<Period> periods) {
		super();
		this.open_now = open_now;
		this.periods = periods;
	}



	public String getOpen_now() {
		return open_now;
	}



	public void setOpen_now(String open_now) {
		this.open_now = open_now;
	}



	public List<Period> getPeriods() {
		return periods;
	}



	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}



	public OpeningHoursPlaceDetails() {
		// TODO Auto-generated constructor stub
	}

}
