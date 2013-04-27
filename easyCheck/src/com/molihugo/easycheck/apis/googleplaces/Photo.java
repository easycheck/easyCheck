package com.molihugo.easycheck.apis.googleplaces;

import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String height;
	private List<String> html_attributions;
	private String photo_reference;
	private String width;
	
	
	
	public String getHeight() {
		return height;
	}



	public void setHeight(String height) {
		this.height = height;
	}



	public List<String> getHtml_attributions() {
		return html_attributions;
	}



	public void setHtml_attributions(List<String> html_attributions) {
		this.html_attributions = html_attributions;
	}



	public String getPhoto_reference() {
		return photo_reference;
	}



	public void setPhoto_reference(String photo_reference) {
		this.photo_reference = photo_reference;
	}



	public String getWidth() {
		return width;
	}



	public void setWidth(String width) {
		this.width = width;
	}



	public Photo(String height, List<String> html_attributions,
			String photo_reference, String width) {
		super();
		this.height = height;
		this.html_attributions = html_attributions;
		this.photo_reference = photo_reference;
		this.width = width;
	}



	public Photo() {
		// TODO Auto-generated constructor stub
	}

}
