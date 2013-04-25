package com.molihugo.easycheck.utils;

import com.example.easycheck.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
	/**
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * @param status
	 *            - success/failure (used to set icon) - pass null if you don't
	 *            want icon
	 * */
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// Setting Dialog Title
		alertDialogBuilder.setTitle(title);

		// Setting Dialog Message
		alertDialogBuilder.setMessage(message)

		//.setIcon((status) ? R.drawable.success : R.drawable.fail)

		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// Showing Alert Message
		alertDialog.show();
	}
}