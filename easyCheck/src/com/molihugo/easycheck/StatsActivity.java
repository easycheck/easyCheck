package com.molihugo.easycheck;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.easycheck.R;
import com.molihugo.easycheck.apis.sugar.SugarConnection;
import com.molihugo.easycheck.utils.Utils;

public class StatsActivity extends Activity {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		 TreeMap<String, Integer> list = null;
		try {
			list = SugarConnection.getStatics(Utils.getStringFromDate(GregorianCalendar
					.getInstance().getTime()), Utils
					.getStringFromDate(GregorianCalendar.getInstance().getTime()), null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 Log.d("MOLIELFEO",Utils.getStringFromDate(GregorianCalendar.getInstance().getTime()));
		 
		 Iterator<String> it = list.descendingKeySet().descendingIterator();
		 while (it.hasNext()){
			 Log.d("lista", list.get(it.next()).toString());
		 }
	}

}
