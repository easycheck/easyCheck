package com.molihugo.easycheck.apis.googleplaces;

import java.util.List;

public class Review {
	
	private List<Aspect> aspects;
	private String author_name;
	private String author_url;
	private String text;
	private String time;
	

	public Review(List<Aspect> aspects, String author_name, String author_url,
			String text, String time) {
		super();
		this.aspects = aspects;
		this.author_name = author_name;
		this.author_url = author_url;
		this.text = text;
		this.time = time;
	}


	public List<Aspect> getAspects() {
		return aspects;
	}


	public void setAspects(List<Aspect> aspects) {
		this.aspects = aspects;
	}


	public String getAuthor_name() {
		return author_name;
	}


	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}


	public String getAuthor_url() {
		return author_url;
	}


	public void setAuthor_url(String author_url) {
		this.author_url = author_url;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public Review() {
		// TODO Auto-generated constructor stub
	}

}
