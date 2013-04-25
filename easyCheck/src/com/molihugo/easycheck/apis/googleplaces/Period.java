package com.molihugo.easycheck.apis.googleplaces;

public class Period {
	
	private OpenClose open;
	private OpenClose close;

	public Period(OpenClose open, OpenClose close) {
		super();
		this.open = open;
		this.close = close;
	}

	public OpenClose getOpen() {
		return open;
	}

	public void setOpen(OpenClose open) {
		this.open = open;
	}

	public OpenClose getClose() {
		return close;
	}

	public void setClose(OpenClose close) {
		this.close = close;
	}

	public Period() {
		// TODO Auto-generated constructor stub
	}

}
