package com.molihugo.easycheck.apis.googleplaces;

import java.util.List;

public class PlaceDetailsResult {
	
	private List<String> html_attributions;
	private Result result;
	private String status;
	
	



	public PlaceDetailsResult(List<String> html_attributions, Result result,
			String status) {
		super();
		this.html_attributions = html_attributions;
		this.result = result;
		this.status = status;
	}



	public List<String> getHtml_attributions() {
		return html_attributions;
	}



	public void setHtml_attributions(List<String> html_attributions) {
		this.html_attributions = html_attributions;
	}






	public Result getResult() {
		return result;
	}



	public void setResult(Result result) {
		this.result = result;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}


	public PlaceDetailsResult() {
		// TODO Auto-generated constructor stub
	}

}
