package com.molihugo.easycheck;

import com.example.easycheck.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class NuevoContactoActivity extends Activity {
	
	Button btnGuardar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacto);
		
		/** button jornada **/
		btnGuardar = (Button) findViewById(R.id.button3);

		/** para mostrar los checkins sin almacenar de la jornada actual */
		btnGuardar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

}
