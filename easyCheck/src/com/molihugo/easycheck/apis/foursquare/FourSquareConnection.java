package com.molihugo.easycheck.apis.foursquare;


import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;

import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import fi.foyt.foursquare.api.io.DefaultIOHandler;


public class FourSquareConnection {

	
	/**
	   * Returns a list of venues near the current location, optionally matching the search term. 
	   *    
	   * @see <a href="https://developer.foursquare.com/docs/venues/search.html" target="_blank">https://developer.foursquare.com/docs/venues/search.html</a>
	   * 
	   * @param ll latitude and longitude of the user's location. (Required for query searches)
	   * @param llAcc accuracy of latitude and longitude, in meters. (Does not currently affect search results.)
	   * @param alt altitude of the user's location, in meters. (Does not currently affect search results.)
	   * @param altAcc accuracy of the user's altitude, in meters. (Does not currently affect search results.)
	   * @param query a search term to be applied against titles.
	   * @param limit number of results to return, up to 50.
	   * @param intent one of checkin, match or specials
	   * @param categoryId a category to limit results to
	   * @param url a third-party URL
	   * @param providerId identifier for a known third party
	   * @param linkedId identifier used by third party specifed in providerId parameter
	   * @return VenuesSearchResult object wrapped in Result object
	   * @throws FoursquareApiException when something unexpected happens
	   * **/
	
	public static Result<VenuesSearchResult>  search(String ll, Double llAcc, Double alt, Double altAcc, String query, Integer limit, String intent, String categoryId, String url, String providerId, String linkedId) {

		 String cId = "NNEVRQJ4MZLRPENJODMEP34ILCYE5Y1NSZFKZ5AGG4Z0QNJS";
		 String cScr="HIQ2ZN3URJXLWZR2G3QN3ZFJWUO45PNV1FKN3E1ZQ0FENQWT";
		 String oauth="QYFVDPFCVIAAII4BYRP0FKM41RTAK0XJS4R23CLCYHVW1XTV";

	    //String ll = "43.25,-2.9";
	    FoursquareApi foursquareApi = new FoursquareApi(cId, cScr, "http://localhost:8080/",oauth,new DefaultIOHandler());
	    Result<VenuesSearchResult> result = null;
		try {
			
			result = foursquareApi.venuesSearch(ll, llAcc, alt, altAcc, query, limit, intent, categoryId, url, providerId, linkedId);
			
			
		} catch (FoursquareApiException e) {
			
		e.printStackTrace();
		}
		return result;
	    


		
	}
	

}
