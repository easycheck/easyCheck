package com.molihugo.easycheck.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class Utils {
	public static String convertId(String user) {
		
		return "seed_"+user+"_id";
		
	}
	@SuppressLint("SimpleDateFormat")
	/**
	  * Formats a date into YYYY-MM-DD
	  *
	  * @param Date  date -- The date to be converted
	  * @return String - The date in YYYY-MM-DD format 
	  */
	public static String getStringFromDate(Date date){
		 
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 return dateFormat.format(date);
	 }
}
