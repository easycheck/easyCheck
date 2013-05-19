package com.molihugo.easycheck;

import com.example.easycheck.R;
import com.molihugo.easycheck.beans.Business;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckinReviewActivity extends Activity {

	Business bu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visita);
		
		Intent i = getIntent();
		bu = (Business) i.getSerializableExtra("business");
		
		TextView lbl_name = (TextView) findViewById(R.id.name);
		TextView lbl_address = (TextView) findViewById(R.id.address);
		TextView lbl_phone = (TextView) findViewById(R.id.phone);
		TextView lbl_location = (TextView) findViewById(R.id.location);
		TextView lbl_origin = (TextView) findViewById(R.id.TextView01);
		TextView lbl_category = (TextView) findViewById(R.id.category);

		lbl_name.setText(bu.getName());
		lbl_address.setText("Dir: "+bu.getAddress());
	
		lbl_phone.setText("Tlf: "+bu.getPhoneNumber());
		String la, lo;
		if (bu.getLat().length() > 6) {
			la = bu.getLat().substring(0, 6);
		} else {
			la = bu.getLat();
		}
		if (bu.getLon().length() > 6) {
			lo = bu.getLon().substring(0, 6);
		} else {
			lo = bu.getLon();
		}
		lbl_location.setText(Html.fromHtml("<b>Lat:</b> " + la
				+ ", <b>Lon:</b> " + lo));
		lbl_category.setText("Tipo: "+bu.getTypes().toString());
		
		lbl_origin.setText("Datos de: "+bu.getApi().toString());
	
		/** button vale **/
		Button btnVale = (Button) findViewById(R.id.button3);

		/** Button vale para volver a la revisión de la jornada */
		btnVale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
					CheckinReviewActivity.this.onBackPressed();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visita, menu);
		return true;
	}
	

}
