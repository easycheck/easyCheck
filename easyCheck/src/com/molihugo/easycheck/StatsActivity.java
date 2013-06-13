package com.molihugo.easycheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.easycheck.R;
import com.molihugo.easycheck.PlacesListActivity.LoadPlaces;
import com.molihugo.easycheck.apis.foursquare.FourSquareConnection;
import com.molihugo.easycheck.apis.googleplaces.GooglePlacesConnection;
import com.molihugo.easycheck.apis.googleplaces.SearchPlaceResults;
import com.molihugo.easycheck.apis.sugar.SugarConnection;
import com.molihugo.easycheck.apis.yelp.BusinessDetail;
import com.molihugo.easycheck.apis.yelp.BusinessListBean;
import com.molihugo.easycheck.apis.yelp.YelpConnector;
import com.molihugo.easycheck.beans.Business;
import com.molihugo.easycheck.utils.AlertDialogManager;
import com.molihugo.easycheck.utils.Listable;
import com.molihugo.easycheck.utils.Stat;
import com.molihugo.easycheck.utils.StatsList;
import com.molihugo.easycheck.utils.Utils;

import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class StatsActivity extends Activity {
	
	private String id;
	private ListView lv;
	private ProgressDialog pDialog;
	private AlertDialogManager alert = new AlertDialogManager();
	private TreeMap<String, Integer> list = new TreeMap<String, Integer>();
	private ArrayList<Listable> statsList = new ArrayList<Listable>();
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_stats);
		lv = (ListView) findViewById(R.id.listStats);
		
		
		
		SharedPreferences pref = getSharedPreferences(LoginActivity.PREFS_NAME,
				MODE_PRIVATE);
		id = pref.getString(LoginActivity.PREF_ID, null);
		
		new LoadStats().execute();
		
	}
	
	class LoadStats extends AsyncTask<String, String, String> {

		

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(StatsActivity.this);

			pDialog.setMessage(Html
					.fromHtml("<b>Busqueda</b><br/>Cargando Stadisticas..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {

			
			try {
				list = SugarConnection.getStatics(id, "", "");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					if (!list.isEmpty()) {
						int pos = 1;
						// loop through each place
						for (Map.Entry<String,Integer> entry : list.entrySet()) {
							Stat st = new Stat(String.valueOf(pos),entry.getKey(),String.valueOf(entry.getValue()));
							
							// adding HashMap to ArrayList
							statsList.add(st);
							pos++;
						}
						// list adapter
						StatsList adapter = new StatsList(
								StatsActivity.this,
								R.layout.list_item,  statsList);

						// Adding data into listview
						lv.setAdapter(adapter);
					} else {
						// Zero results found
						alert.showAlertDialog(
								StatsActivity.this,
								"Stats",
								"Sorry no stats found",
								false);
					}
				}

			});

		}

	}
	

}
