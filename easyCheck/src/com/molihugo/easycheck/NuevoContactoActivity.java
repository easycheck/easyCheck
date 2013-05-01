package com.molihugo.easycheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.easycheck.R;
import com.molihugo.easycheck.beans.Contacto;

public class NuevoContactoActivity extends Activity {
	
	Button btnGuardar;
	Contacto contacto;
	EditText nombre, telefono, mail, posicion;
	String nom, tel, mai, pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacto);
		
		/** button jornada **/
		btnGuardar = (Button) findViewById(R.id.button3);
		
		nombre = (EditText) findViewById(R.id.editText1);
		telefono = (EditText) findViewById(R.id.editText5);
		mail = (EditText) findViewById(R.id.editText3);
		posicion = (EditText) findViewById(R.id.editText4);

		/** para mostrar los checkins sin almacenar de la jornada actual */
		btnGuardar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				nom = ((Editable)nombre.getText()).toString();
				tel = ((Editable)telefono.getText()).toString();
				mai =((Editable)mail.getText()).toString();
				pos = ((Editable)posicion.getText()).toString();
				contacto = new Contacto (nom,tel,mai,pos);
				
				try {
					 Intent returnIntent = new Intent();
					 returnIntent.putExtra("result",contacto);
					 setResult(RESULT_OK,returnIntent);     
					 finish();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		super.onBackPressed();
	}

}
