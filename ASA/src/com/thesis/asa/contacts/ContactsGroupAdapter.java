package com.thesis.asa.contacts;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class ContactsGroupAdapter extends ArrayAdapter<ContactsGroupItem> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;
	
	public ContactsGroupAdapter(Context c, int textViewResourceId,
			List<ContactsGroupItem> objects) {
		super(c, textViewResourceId, objects);
		context = c;
		layoutId = textViewResourceId;
		inflater = ((Activity) context).getLayoutInflater();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null)
			view = inflater.inflate(layoutId, parent, false);
		ContactsGroupItem item = getItem(position);
	    ((CheckedTextView) view).setText(item.getLabel());
	    
	    return view;
	}
}
