package com.molihugo.easycheck.apis.yelp;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class YelpConnector {

	static OAuthService service;
	static Token accessToken;

	/**
	 * Setup the Yelp API OAuth credentials.
	 * 
	 * OAuth credentials are available from the developer site, under Manage API
	 * access (version 2 API).
	 * 
	 * @param consumerKey
	 *            Consumer key
	 * @param consumerSecret
	 *            Consumer secret
	 * @param token
	 *            Token
	 * @param tokenSecret
	 *            Token secret
	 */
	public YelpConnector() {
		// Update tokens here from Yelp developers site, Manage API access.
		String consumerKey = "_4B5BlmQ5dXjSr-0Ue6pKw";
		String consumerSecret = "k1fCJQcwFSvO7sYTor3IEi7xKWs";
		String token = "6zCX-pw_W85VZSVMzp2BVhSwKJDzE-eY";
		String tokenSecret = "JoNQLDIQ_jQT_OUnG5YOVZuVJ2I";
		this.service = new ServiceBuilder().provider(YelpAux.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}

	/**
	 * Search with term and location.
	 * 
	 * @param term
	 *            Search term
	 * @param String.valueOF(lat)
	 *            Latitude
	 * @param String.valueOF(lon)
	 *            Longitude
	 * @return JSON string response
	 */
	public static BusinessListBean search(String term, double lat,
			double lon, String radius) {
		String ll = String.valueOf(lat) + "," + String.valueOf(lon);
		OAuthRequest request = new OAuthRequest(
				Verb.GET,
				"http://api.yelp.com/v2/search?ll="+ll);
		 request.addQuerystringParameter("term", term);
		 request.addQuerystringParameter("radius_filter", radius);
		service.signRequest(accessToken, request);
		Response response = request.send();

		BusinessListBean blb = null;
		String jsonResult = response.getBody();
		Gson gson = new GsonBuilder().setFieldNamingPolicy(
				FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		try {
			blb = gson.fromJson(jsonResult, BusinessListBean.class);
		} catch (JsonSyntaxException ex) {
			// Log.e(YelpService.class.getName(), ex.getCause() + " : " +
			// ex.getLocalizedMessage());
		}
		// System.out.println("Total de negocios:"+blb.getTotal());

		return blb;
	}

}