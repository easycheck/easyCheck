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
import com.molihugo.easycheck.beans.Contact;
import com.molihugo.easycheck.utils.AlertDialogManager;
import com.molihugo.easycheck.utils.CheckDAO;
import com.molihugo.easycheck.utils.Utils;

public class DatosCheckActivity extends Activity {

	private String sugarComId;
	
	private AlertDialogManager alert = new AlertDialogManager();

	private ProgressDialog pDialog2;
	private ProgressDialog pDialog;

	private Button btnConfirm, btnNewContact;
	private CheckDAO dao;

	private Business bu;
	private List<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

	private EditText descriptor, quantity, closingDate;
	private String id;

	private Contact contact;

	private Spinner sp;
	private LinkedList<String> categories;
	private ArrayAdapter<String> spinner_adapter;
	
	private Spinner sp2;
	private LinkedList<String> categories2;
	private ArrayAdapter<String> spinner_adapter2;
	
	
	private String selectedType;
	
	private boolean newContact = false;
	
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
		quantity = (EditText) findViewById(R.id.editText5);
		closingDate = (EditText) findViewById(R.id.editText6);

		Intent i = getIntent();
		bu = (Business) i.getSerializableExtra("business");

		// spinner con tipos de Visita
		sp = (Spinner) findViewById(R.id.spinner1);
		categories = new LinkedList<String>();
		categories.add("Seleccione tipo visita");
		categories.add("Check-in");
		categories.add("Presupuesto");
		categories.add("Pre-Acuerdo");
		categories.add("Venta Cerrada");
		spinner_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinner_adapter);

		// spinner con contactos
		sp2 = (Spinner) findViewById(R.id.spinner2);
		
		new LoadContacts().execute();
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener(){
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	if (position != 0){

			    	selectedType = categories.get(position);
			    	Log.d("---TYPEEEE---", selectedType);
		    	}else{
		    		selectedType = null;
		    	}
		    	
		    	
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		sp2.setOnItemSelectedListener(new OnItemSelectedListener(){
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	if (placesListItems.size()!=0 && position!=0){
		    		conId = placesListItems.get(position-1).get("id");
		    		Log.d("---CONID---", conId);
		    	}
		    	if (!newContact && position==0){
		    		conId=null;
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		

		

		// botón para un nuevo contacto
		btnNewContact = (Button) findViewById(R.id.button2);
		btnNewContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int in = 1;
				Intent i = new Intent(getApplicationContext(),
						NewContactActivity.class);
				startActivityForResult(i, in);
			}
		});

		// botón para confirmar visita y volver a FirstActivity
		btnConfirm = (Button) findViewById(R.id.button3);
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dao.insert(bu);
				if (conId==null && !newContact){
					alert.showAlertDialog(DatosCheckActivity.this,"Error", "No se ha seleccionado contacto", false);
				}
				else if (selectedType==null){
					alert.showAlertDialog(DatosCheckActivity.this,"Error", "No se ha seleccionado tipo visita", false);
				}
				else {
					new DataTransaction().execute();
				Intent i = new Intent(getApplicationContext(),
						MenuActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				}
					
				
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				contact = (Contact) data.getSerializableExtra("result");
				newContact = true;
				categories2 = new LinkedList<String>();
				categories2.add(contact.getName());
				sp2.setClickable(false);
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		if (newContact){
			spinner_adapter2 = new ArrayAdapter<String>(DatosCheckActivity.this,
				android.R.layout.simple_spinner_item, categories2);
		spinner_adapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp2.setAdapter(spinner_adapter2);
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
				if (sugarComId.equals("")) {
					sugarComId = SugarConnection
							.newCompany(id, bu.getName().toString(), null,
									bu.getAddress().toString(), bu.getPhoneNumber(), bu
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

					categories2 = new LinkedList<String>();
					
					if (!placesListItems.isEmpty()) {
						categories2.add("Seleccione un contacto");
						for (Iterator<HashMap<String,String>> iterator = placesListItems.iterator(); iterator
								.hasNext();) {
							HashMap<String,String> type = iterator.next();
							categories2.add(type.get("name").toString());
					
						}
						
						
					} else {
						categories2.add("Ningun contacto para esta empresa");
					}

					// Log.d("ID", com);
					//Log.d("CONTAAAAAACTS", placesListItems.get(0).get("name"));
					spinner_adapter2 = new ArrayAdapter<String>(DatosCheckActivity.this,
							android.R.layout.simple_spinner_item, categories2);
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
				if (newContact){
					conId = SugarConnection.newContact(id,
							contact.getName(), contact.getTelephone(),
							contact.getMail(), contact.getPosition(), sugarComId);
				}

				
				Log.d("CONIDDDDD", conId);
				Log.d("COMIDDDDDDD", sugarComId);
				Log.d("COMIDDDDDDD", selectedType);
				Log.d("DESCRIPCION", ((Editable)descriptor.getText()).toString());
				Log.d("FECHA", ((Editable)closingDate.getText()).toString());
				
				Log.d("Cantidad",((Editable)quantity.getText()).toString());
				
				SugarConnection.newSale(id,  ((Editable)descriptor.getText()).toString(),
						((Editable)closingDate.getText()).toString(), selectedType ,
						((Editable)quantity.getText()).toString(), sugarComId, conId, conId, conId);
				

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
