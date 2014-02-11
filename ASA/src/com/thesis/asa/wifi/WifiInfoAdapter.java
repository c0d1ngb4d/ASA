package com.thesis.asa.wifi;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WifiInfoAdapter extends ArrayAdapter<WifiInfoItem> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;
	
	public WifiInfoAdapter(Context c, int textViewResourceId) {
		super(c, textViewResourceId);
		context = c;
		layoutId = textViewResourceId;
		inflater = ((Activity) context).getLayoutInflater();
	}
	
	public WifiInfoAdapter(Context c, int textViewResourceId, List<WifiInfoItem> items) {
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
		WifiInfoItem item = getItem(position);
	    ((TextView) view).setText(item.getLabel());
	    
	    return view;
	}
}
