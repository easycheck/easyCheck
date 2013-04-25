package com.molihugo.easycheck.apis.googleplaces;

import java.util.List;

public class SearchPlaceResult {
	private List<String> html_attributions;
	private List<SearchPlaceResults> results;
	private String status;
	
	public SearchPlaceResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<String> getHtml_attributions() {
		return html_attributions;
	}

	public void setHtml_attributions(List<String> html_attributions) {
		this.html_attributions = html_attributions;
	}

	
	
	public SearchPlaceResult(List<String> html_attributions,
			List<SearchPlaceResults> results, String status) {
		super();
		this.html_attributions = html_attributions;
		this.results = results;
		this.status = status;
	}

	public List<SearchPlaceResults> getResults() {
		return results;
	}

	public void setResults(List<SearchPlaceResults> results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

	

}
