package com.molihugo.easycheck.apis.foursquare;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import fi.foyt.foursquare.api.io.DefaultIOHandler;

public class FourSquareConnection {
	
	private static String cId = "NNEVRQJ4MZLRPENJODMEP34ILCYE5Y1NSZFKZ5AGG4Z0QNJS";
	private static String cScr = "HIQ2ZN3URJXLWZR2G3QN3ZFJWUO45PNV1FKN3E1ZQ0FENQWT";
	private static String oauth = "QYFVDPFCVIAAII4BYRP0FKM41RTAK0XJS4R23CLCYHVW1XTV";

	public static Result<VenuesSearchResult> search(double lat, double lon) {
		FoursquareApi foursquareApi = new FoursquareApi(cId, cScr, "http://localhost:8080/",oauth,new DefaultIOHandler());
		
		// After client has been initialized we can make queries.
		
		Result<VenuesSearchResult> result = null;
		String ll = lat+", "+lon;
		
		try {
			result = foursquareApi.venuesSearch(ll, null, null, null, null, null, null, null, null, null, null);
		} catch (FoursquareApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
}
