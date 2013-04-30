package com.molihugo.easycheck;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.easycheck.R;
import com.molihugo.easycheck.apis.sugar.SugarConnection;
import com.molihugo.easycheck.utils.AlertDialogManager;
import com.molihugo.easycheck.utils.ConnectionDetector;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	public static final String PREFS_NAME = "EasyCheckPrefs";
	public static final String PREF_ID = "id";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	// color for the error font
	private int ecolor = Color.RED;
	private ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
	private SpannableStringBuilder ssbuilder;
	private String estring;

	private ConnectionDetector cd;
	private boolean isInternetPresent;
	private AlertDialogManager alert;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(
					LoginActivity.this,
					"Error de conectividad",
					"Por favor, revise su conexión de datos y vuelva a intentarlo",
					false);
			findViewById(R.id.sign_in_button).setEnabled(false);
			return;
		}

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public void onBackPressed() {
		super.finish();
		this.finish();

	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			estring = getString(R.string.error_field_required);
			ssbuilder = new SpannableStringBuilder(estring);
			ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);

			mPasswordView.setError(ssbuilder);
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			estring = getString(R.string.error_invalid_password);
			ssbuilder = new SpannableStringBuilder(estring);
			ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);

			mPasswordView.setError(ssbuilder);
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			estring = getString(R.string.error_field_required);
			ssbuilder = new SpannableStringBuilder(estring);
			ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);

			mEmailView.setError(ssbuilder);
			focusView = mEmailView;
			cancel = true;
		} else if (mEmail.length() < 4) {
			estring = getString(R.string.error_invalid_email);
			fgcspan = new ForegroundColorSpan(ecolor);
			ssbuilder = new SpannableStringBuilder(estring);
			ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);

			mEmailView.setError(ssbuilder);
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		private String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/**
			 * Shows the progress dialog and hides the login form.
			 */
			pDialog = new ProgressDialog(LoginActivity.this);

			pDialog.setMessage(Html
					.fromHtml("<b>Busqueda</b><br/>Cargando Negocios..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				id = SugarConnection.login(mEmail, mPassword);
				// Log.d("IIDD", id);
				if (id != null) {
					return true;
				} else if (mEmail.equals("noSugar")) {
					return true;
				} else {
					return false;
				}
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("IIDD", "PASO");
				e.printStackTrace();
			}
			
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			pDialog.dismiss();

			if (success) {
				getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
					.putString(PREF_ID, id).commit();
				Intent i = new Intent(getApplicationContext(),
						FirstActivity.class);
				i.putExtra("idUser", id);
				startActivity(i);
				finish();
			} else {

				estring = getString(R.string.error_incorrect_password);
				ssbuilder = new SpannableStringBuilder(estring);
				ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);

				mPasswordView.setError(ssbuilder);
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
}
