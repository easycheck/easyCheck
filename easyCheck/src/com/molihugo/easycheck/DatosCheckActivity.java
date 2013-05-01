package com.molihugo.easycheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.easycheck.R;
import com.molihugo.easycheck.apis.sugar.SugarConnection;
import com.molihugo.easycheck.beans.Business;
import com.molihugo.easycheck.beans.Contacto;
import com.molihugo.easycheck.utils.CheckDAO;

public class DatosCheckActivity extends Activity {

	private String sugarComId;

	private ProgressDialog pDialog2;
	private ProgressDialog pDialog;

	private Button btnConfirmar, btnNuevoContacto;
	private CheckDAO dao;

	private Business bu;
	private List<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

	private EditText descriptor, cantidad, fechaCierre;
	private String id;

	private Contacto contacto;

	private Spinner sp;
	private LinkedList<String> tipos;
	ArrayAdapter<String> spinner_adapter;
	
	private Spinner sp2;
	private LinkedList<String> tipos2;
	ArrayAdapter<String> spinner_adapter2;
	
	
	private String selectedType;
	
	private Boolean nuevoContacto;
	
	private String conId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datoscheck);

		dao = new CheckDAO(this);

		SharedPreferences pref = getSharedPreferences(LoginActivity.PREFS_NAME,
				MODE_PRIVATE);
		id = pref.getString(LoginActivity.PREF_ID, null);

		if (id == null) {
			this.finish();
		}

		descriptor = (EditText) findViewById(R.id.editText1);
		cantidad = (EditText) findViewById(R.id.editText2);
		fechaCierre = (EditText) findViewById(R.id.editText3);

		Intent i = getIntent();
		bu = (Business) i.getSerializableExtra("business");

		// spinner con tipos de Visita
		sp = (Spinner) findViewById(R.id.spinner1);
		tipos = new LinkedList<String>();
		tipos.add("Seleccione tipo visita");
		tipos.add("Check-in");
		tipos.add("Presupuesto");
		tipos.add("Pre-Acuerdo");
		tipos.add("Venta Cerrada");
		spinner_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, tipos);
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinner_adapter);

		// spinner con contactos
		sp2 = (Spinner) findViewById(R.id.spinner2);
		
		new LoadContacts().execute();
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener(){
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	selectedType = tipos.get(position+1);
		    	
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		sp2.setOnItemSelectedListener(new OnItemSelectedListener(){
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	if (placesListItems.size()!=0){
		    		conId = placesListItems.get(position+1).get("id");
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		

		

		// botón para un nuevo contacto
		btnNuevoContacto = (Button) findViewById(R.id.button2);
		btnNuevoContacto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int in = 1;
				Intent i = new Intent(getApplicationContext(),
						NuevoContactoActivity.class);
				startActivityForResult(i, in);
			}
		});

		// botón para confirmar visita y volver a FirstActivity
		btnConfirmar = (Button) findViewById(R.id.button3);
		btnConfirmar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dao.insert(bu);
				new DataTransaction().execute();
				Intent i = new Intent(getApplicationContext(),
						FirstActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				contacto = (Contacto) data.getSerializableExtra("result");
				nuevoContacto = true;
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	class LoadContacts extends AsyncTask<String, String, String> {

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

		protected String doInBackground(String... args) {

			try {

				// LLAMADA A SUGAR

				sugarComId = SugarConnection.getCompanyId(bu.getName(), id);
				if (sugarComId == null) {
					sugarComId = SugarConnection
							.newCompany(id, bu.getName(), null,
									bu.getAddress(), bu.getPhoneNumber(), bu
											.getEmail(), null, null, bu
											.getTypes().toString());
				}

				placesListItems = SugarConnection.getContacts(id, sugarComId);
				

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("AAAAAAAAAAAAAAA", e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {

					tipos2 = new LinkedList<String>();
					
					if (!placesListItems.isEmpty()) {
						tipos2.add("Seleccione un contacto");
						for (Iterator<HashMap<String,String>> iterator = placesListItems.iterator(); iterator
								.hasNext();) {
							HashMap<String,String> type = iterator.next();
							tipos2.add(type.get("name").toString());
					
						}
						
						
					} else {
						tipos2.add("Ningun contacto para esta empresa");
					}

					// Log.d("ID", com);
					//Log.d("CONTAAAAAACTS", placesListItems.get(0).get("name"));
					
					spinner_adapter2 = new ArrayAdapter<String>(DatosCheckActivity.this,
							android.R.layout.simple_spinner_item, tipos2);
					spinner_adapter2
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sp2.setAdapter(spinner_adapter2);
				}

			});

		}
	}

	class DataTransaction extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * pDialog2 = new ProgressDialog(DatosCheckActivity.this);
			 * 
			 * pDialog2.setMessage(Html
			 * .fromHtml("<b>Busqueda</b><br/>Conectando con Sugar..."));
			 * pDialog2.setIndeterminate(false); pDialog2.setCancelable(false);
			 * pDialog2.show();
			 */}

		protected String doInBackground(String... args) {

			try {

				// LLAMADA A SUGAR
				if (nuevoContacto){
					conId = SugarConnection.newContact(id,
							contacto.getNombre(), contacto.getTelefono(),
							contacto.getMail(), contacto.getPosicion(), sugarComId);
				}
				
				Log.d("CONIDDDDD", conId);
				SugarConnection.newSale(id,  ((Editable)descriptor.getText()).toString(),
						 "fecha", "tipo tipo",
						 "100", sugarComId, conId);
				Log.d("COMIDDDDDDD", sugarComId);
				

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("AAAAAAAAAAAAAAA", e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			// pDialog2.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {

				}

			});

		}
	}

}
