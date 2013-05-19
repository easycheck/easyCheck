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
import com.molihugo.easycheck.beans.Contact;

public class NewContactActivity extends Activity {
	
	Button btnSave;
	Contact contact;
	EditText name, telephone, mail, position;
	String nam, tel, mai, pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacto);
		
		/** button jornada **/
		btnSave = (Button) findViewById(R.id.button3);
		
		name = (EditText) findViewById(R.id.editText1);
		telephone = (EditText) findViewById(R.id.editText5);
		mail = (EditText) findViewById(R.id.editText3);
		position = (EditText) findViewById(R.id.editText4);

		/** para mostrar los checkins sin almacenar de la jornada actual */
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				nam = ((Editable)name.getText()).toString();
				tel = ((Editable)telephone.getText()).toString();
				mai =((Editable)mail.getText()).toString();
				pos = ((Editable)position.getText()).toString();
				contact = new Contact (nam,tel,mai,pos);
				
				try {
					 Intent returnIntent = new Intent();
					 returnIntent.putExtra("result",contact);
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
