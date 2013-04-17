package com.example.easycheck;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DatosCheckActivity extends Activity {
	
	private Button btnConfirmar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datoscheck);
		
		Spinner sp = (Spinner) findViewById(R.id.spinner1);
		//Creamos la lista
		LinkedList<String> estados = new LinkedList<String>();
		//La poblamos con los estados
		estados.add("Check-in");
		estados.add("Presupuesto");
		estados.add("Pre-Acuerdo");
		estados.add("Venta Cerrada");
		//Creamos el adaptador
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados);
		//Añadimos el layout para el menú y se lo damos al spinner
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinner_adapter);
		
		sp = (Spinner) findViewById(R.id.spinner2);
		
		estados = new LinkedList<String>();
		//La poblamos con los estados
		estados.add("Manolito gafotas");
		estados.add("Molino");
		estados.add("Antonio");
		estados.add("Ion Jauregui");
		//Creamos el adaptador
		spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados);
		//Añadimos el layout para el menú y se lo damos al spinner
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinner_adapter);
		
		/** button check **/
		btnConfirmar = (Button) findViewById(R.id.button1);

		/** Button check click event for showing the business around you */
		btnConfirmar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						FirstActivity.class);
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

}
