package com.molihugo.easycheck;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.easycheck.R;
import com.molihugo.easycheck.utils.CheckDAO;

public class JornadaActivity extends Activity {

	CheckDAO ranking;
	// rankingItems data
	ArrayList<HashMap<String, String>> rankingItems = new ArrayList<HashMap<String, String>>();
	// Ranking Listview
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jornada);
		ranking = new CheckDAO(this);
		
		//HashMap<String,String> hm = new HashMap<String,String>();

		// Getting listview
		lv = (ListView) findViewById(R.id.listView1);

		rankingItems = ranking.get();
		
		//hm.put("nombre", "hugo");
		//hm.put("victorias", "2");

		// adding HashMap to ArrayList
		//rankingItems.add(hm);
		
		
		// list adapter
		ListAdapter adapter = new SimpleAdapter(
				JornadaActivity.this, rankingItems,
				R.layout.list_item, new String[] {
						"nombre", "victorias" },
				new int[] { R.id.reference, R.id.name });

		// Adding data into listview
		lv.setAdapter(adapter);
		
		//Toast.makeText(JornadaActivity.this, rankingItems.get(1).toString(), Toast.LENGTH_LONG)
		//		.show();
	}

	@Override
	public void onPause() {

		//Log.d("HUGO ranking item 1", rankingItems.get(1).toString());
		super.onPause();
	}

}
