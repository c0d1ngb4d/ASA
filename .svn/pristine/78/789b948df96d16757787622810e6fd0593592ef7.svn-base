package com.thesis.asa.location;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LocationInfoAdapter extends ArrayAdapter<LocationInfoItem> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;
	
	public LocationInfoAdapter(Context c, int textViewResourceId) {
		super(c, textViewResourceId);
		context = c;
		layoutId = textViewResourceId;
		inflater = ((Activity) context).getLayoutInflater();
	}
	
	public LocationInfoAdapter(Context c, int textViewResourceId, List<LocationInfoItem> items) {
		super(c, textViewResourceId, items);
		context = c;
		layoutId = textViewResourceId;
		inflater = ((Activity) context).getLayoutInflater();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null)
			view = inflater.inflate(layoutId, parent, false);
		LocationInfoItem item = getItem(position);
	    ((TextView) view).setText(item.getLabel());
	    
	    return view;
	}
}
