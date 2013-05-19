package com.molihugo.easycheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.easycheck.R;
import com.molihugo.easycheck.apis.sugar.SugarConnection;
import com.molihugo.easycheck.beans.Business;
import com.molihugo.easycheck.utils.CheckDAO;

public class ReviewActivity extends Activity {

	CheckDAO ranking;
	// rankingItems data
	ArrayList<HashMap<String, String>> rankingItems = new ArrayList<HashMap<String, String>>();
	// Ranking Listview
	ListView lv;

	List<Business> list;
	
	String id;
	String username;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jornada);
		ranking = new CheckDAO(this);
		
		//HashMap<String,String> hm = new HashMap<String,String>();

		// Getting listview
		lv = (ListView) findViewById(R.id.listView1);
		
		SharedPreferences pref = getSharedPreferences(LoginActivity.PREFS_NAME,
				MODE_PRIVATE);
		username = pref.getString("nombre", null);
		id = pref.getString(LoginActivity.PREF_ID, null);
		
		//SugarConnection.getRevision(id, "", "", username);

		
		
		list = ranking.get();
		
		HashMap<String,String> map = new HashMap<String,String>();
		
		for (int i= 0; i<list.size(); i++){
			map = new HashMap<String,String>();
			map.put("referencia",list.get(i).getReference());
			map.put("nombre", list.get(i).getName());
			rankingItems.add(map);
		}
		
		
		// list adapter
		ListAdapter adapter = new SimpleAdapter(
				ReviewActivity.this, rankingItems,
				R.layout.list_item, new String[] {
						"referencia", "nombre" },
				new int[] { R.id.reference, R.id.name });

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

				for(Business b: list){
					if (b.getReference().equals(reference)){
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
	}

}
