package com.molihugo.easycheck.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.example.easycheck.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StatsList extends ArrayAdapter<Listable>{
	
	private Context context;
	private ArrayList<Listable> list;
	public StatsList(Context context, int textViewResourceId,
			ArrayList<Listable> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.list = objects;
		
	}
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.list_stats, null);
		TextView pos = (TextView) row.findViewById(R.id.pos); 
		TextView name = (TextView) row.findViewById(R.id.nameStats);  
		TextView number = (TextView) row.findViewById(R.id.numSales);  
		if (list.get(position) instanceof Stat){
			pos.setText(((Stat) list.get(position)).getPos());
			name.setText(((Stat) list.get(position)).getName());
			number.setText(((Stat) list.get(position)).getNumberSale());
			
		}
		return row;
	}

}
