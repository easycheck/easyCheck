package com.molihugo.easycheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.easycheck.R;
import com.molihugo.easycheck.apis.sugar.SaleResponse;
import com.molihugo.easycheck.apis.sugar.SugarConnection;
import com.molihugo.easycheck.beans.Business;
import com.molihugo.easycheck.utils.AlertDialogManager;
import com.molihugo.easycheck.utils.CheckDAO;
import com.molihugo.easycheck.utils.Stat;
import com.molihugo.easycheck.utils.StatsList;
import com.molihugo.easycheck.utils.Utils;

public class ReviewActivity extends FragmentActivity {

	private HashMap<String, String> map = new HashMap<String, String>();

	private Button btn1;
	private String fechaIni;

	private String fechaFin = "";

	CheckDAO ranking;
	// rankingItems data
	ArrayList<HashMap<String, String>> rankingItems = new ArrayList<HashMap<String, String>>();
	// Ranking Listview
	ListView lv;

	List<Business> list;

	String id;
	String username;

	private ProgressDialog pDialog;
	private List<SaleResponse> listSugar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jornada);
		ranking = new CheckDAO(this);

		// HashMap<String,String> hm = new HashMap<String,String>();

		// Getting listview
		lv = (ListView) findViewById(R.id.listStats);
		btn1 = (Button) findViewById(R.id.button1);

		SharedPreferences pref = getSharedPreferences(LoginActivity.PREFS_NAME,
				MODE_PRIVATE);
		username = pref.getString("nombre", null);
		id = pref.getString(LoginActivity.PREF_ID, null);

		// SugarConnection.getRevision(id, "", "", username);

		list = ranking.get();

		for (int i = 0; i < list.size(); i++) {
			map = new HashMap<String, String>();
			map.put("referencia", list.get(i).getReference());
			map.put("nombre", list.get(i).getName());
			rankingItems.add(map);
		}

		// list adapter
		ListAdapter adapter = new SimpleAdapter(ReviewActivity.this,
				rankingItems, R.layout.list_item, new String[] { "referencia",
						"nombre" }, new int[] { R.id.reference, R.id.name });

		// Adding data into listview
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String reference = ((TextView) view
						.findViewById(R.id.reference)).getText().toString();
				Business bu = null;

				for (Business b : list) {
					if (b.getReference().equals(reference)) {
						bu = new Business(b);
					}
				}

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						CheckinReviewActivity.class);

				in.putExtra("business", bu);

				startActivity(in);
			}
		});

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment dateFragment = new DatePickerFragment(false);
				dateFragment.show(getSupportFragmentManager(), "datePicker");
				DatePickerFragment dateFragment2 = new DatePickerFragment(true);
				dateFragment2.show(getSupportFragmentManager(), "datePicker");
			}
		});

	}

	@SuppressLint("ValidFragment")
	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		private boolean fin;

		@SuppressLint("ValidFragment")
		public DatePickerFragment(boolean fin) {
			super();
			this.fin = fin;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog dPD = new DatePickerDialog(getActivity(), this,
					year, month, day);
			if (fin) {
				dPD.setMessage("Buscar desde el día...");
			} else {
				dPD.setMessage("...hasta el día...");
			}

			return dPD;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			if (fin) {
				fechaIni = Utils.getStringFromDate(year, month, day);
			} else {
				fechaFin = Utils.getStringFromDate(year, month, day);
				new LoadSales().execute();
			}

		}
	}

	class LoadSales extends AsyncTask<String, String, String> {

		protected AlertDialogManager alert;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReviewActivity.this);

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

			
			if (!fechaFin.equals("")){
				Log.d("review",id );
				Log.d("review",fechaIni);
				Log.d("review",fechaFin );
				Log.d("review", username);
				listSugar = SugarConnection.getRevision(id, fechaIni, fechaFin,
						Utils.convertId(username), 1);
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
					if (!listSugar.isEmpty()) {
						list.clear();
						// loop through each place
						for (SaleResponse sale : listSugar) {
							map = new HashMap<String, String>();
							map.put("referencia", sale.getSaleName());
							map.put("nombre", sale.getSaleName());

							// adding HashMap to ArrayList
							rankingItems.add(map);
						}
						// list adapter
						// list adapter
						ListAdapter adapter = new SimpleAdapter(
								ReviewActivity.this, rankingItems,
								R.layout.list_item, new String[] {
										"referencia", "nombre" }, new int[] {
										R.id.reference, R.id.name });

						// Adding data into listview
						lv.setAdapter(adapter);
					} else {
						// Zero results found
						alert.showAlertDialog(ReviewActivity.this,
								"Revisión de visitas",
								"Disculpa, no se han encontrado resultados",
								false);
					}
				}

			});

		}

	}

}
