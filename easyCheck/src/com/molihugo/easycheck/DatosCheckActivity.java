package com.molihugo.easycheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.example.easycheck.R;
import com.molihugo.easycheck.utils.Business;
import com.molihugo.easycheck.utils.CheckDAO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class DatosCheckActivity extends Activity {

	private Button btnConfirmar, btnNuevoContacto;
	private CheckDAO dao;

	Business bu;
	private List<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datoscheck);
		dao = new CheckDAO(this);

		Intent i = getIntent();
		bu = (Business) i.getSerializableExtra("business");

		Spinner sp = (Spinner) findViewById(R.id.spinner1);
		// Creamos la lista
		LinkedList<String> estados = new LinkedList<String>();
		// La poblamos con los estados
		estados.add("Check-in");
		estados.add("Presupuesto");
		estados.add("Pre-Acuerdo");
		estados.add("Venta Cerrada");
		// Creamos el adaptador
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, estados);
		// Añadimos el layout para el menú y se lo damos al spinner
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinner_adapter);

		sp = (Spinner) findViewById(R.id.spinner2);

		estados = new LinkedList<String>();
		// La poblamos con los estados
		estados.add("Manolito gafotas");
		estados.add("Molino");
		estados.add("Antonio");
		estados.add("Ion Jauregui");
		// Creamos el adaptador
		spinner_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, estados);
		// Añadimos el layout para el menú y se lo damos al spinner
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinner_adapter);

		sp.setAdapter(new SimpleAdapter(DatosCheckActivity.this,
				placesListItems, R.layout.list_item, new String[] { "id",
						"name" }, new int[] { R.id.reference, R.id.name }));

		btnNuevoContacto = (Button) findViewById(R.id.button2);

		/** Button check click event for showing the business around you */
		btnNuevoContacto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int in = 0;
				Intent i = new Intent(getApplicationContext(),
						NuevoContactoActivity.class);
				startActivityForResult(i,in);
			}
		});

		/** button check **/
		btnConfirmar = (Button) findViewById(R.id.button3);

		/** Button check click event for showing the business around you */
		btnConfirmar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dao.insert(bu);
				Intent i = new Intent(getApplicationContext(),
						FirstActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.datos_check, menu);
		return true;
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DatosCheckActivity.this);

			pDialog.setMessage(Html
					.fromHtml("<b>Busqueda</b><br/>Conectando con Sugar..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {

			try {

				// LLAMADA A

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("AAAAAAAAAAAAAAA", e.getMessage());
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

					/*
					 * // Updating parsed Places into LISTVIEW if
					 * (!negocios.isEmpty()) { // loop through each place for
					 * (Business b : negocios) { HashMap<String, String> map =
					 * new HashMap<String, String>();
					 * 
					 * // Place reference won't display in listview - // it will
					 * be hidden // Place reference is used to get //
					 * "place full details" map.put(KEY_REFERENCE,
					 * b.getReference()); // Place name map.put(KEY_NAME,
					 * b.getName());
					 * 
					 * // adding HashMap to ArrayList placesListItems.add(map);
					 * } // list adapter ListAdapter adapter = new
					 * SimpleAdapter( MainActivity.this, placesListItems,
					 * R.layout.list_item, new String[] { KEY_REFERENCE,
					 * KEY_NAME }, new int[] { R.id.reference, R.id.name });
					 * 
					 * // Adding data into listview lv.setAdapter(adapter); }
					 * else { // Zero results found alert.showAlertDialog(
					 * MainActivity.this, "Near Places",
					 * "Sorry no places found. Try to change the types of places"
					 * , false); }
					 */}

			});

		}
	}

}
