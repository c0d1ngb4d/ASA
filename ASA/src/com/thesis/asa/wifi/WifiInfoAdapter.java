<<<<<<< HEAD
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
=======
/*******************************************************************************
 * Copyright (c) 2014 CodingBad.
 *  All rights reserved.  This file is part of ASA.
 *  
 *  ASA is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  ASA is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *    
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *     Ayelén Chavez - ashy.on.line@gmail.com
 *     Joaquín Rinaudo - jmrinaudo@gmail.com
 ******************************************************************************/
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
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
