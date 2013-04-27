package com.molihugo.easycheck.apis.googleplaces;

import java.io.Serializable;


public class OpeningHours implements Serializable {
	private static final long serialVersionUID = 1L;
	private String open_now;

	public String getOpen_now() {
		return open_now;
	}

	public void setOpen_now(String open_now) {
		this.open_now = open_now;
	}

	public OpeningHours(String open_now) {
		super();
		this.open_now = open_now;
	}

	public OpeningHours() {
		// TODO Auto-generated constructor stub
	}

}
