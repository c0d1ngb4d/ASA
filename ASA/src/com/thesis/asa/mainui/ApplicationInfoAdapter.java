/*******************************************************************************
 * Copyright (c) 2014 CodingBad.
 * All rights reserved.  This file is part of ASA.
 * 
 * ASA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASA.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * Ayelén Chavez - ashy.on.line@gmail.com
 * Joaquín Rinaudo - jmrinaudo@gmail.com
 ******************************************************************************/
 
 
package com.thesis.asa.mainui;

import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;

public class ApplicationInfoAdapter extends BaseAdapter {
	private Context context;
	private List<String> permissions;
	private List<ApplicationInfo> appInfoList;
	private PackageManager packManager;

	public ApplicationInfoAdapter(Context c, List<String> p,
			List<ApplicationInfo> list, PackageManager pm) {
		context = c;
		permissions = p;
		appInfoList = list;
		packManager = pm;
	}

	@Override
	public int getCount() {
		return appInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return appInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApplicationInfo entry = appInfoList.get(position);
		View view = convertView;
		try {

			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.appinfo_layout, null);
			}

			ImageView icon = (ImageView) view.findViewById(R.id.ivIcon);
			TextView tvAppName = (TextView) view.findViewById(R.id.tvName);
			TextView tvDefault = (TextView) view
					.findViewById(R.id.appliedDefault);

			icon.setImageDrawable(entry.loadIcon(packManager));
			tvAppName.setText(entry.loadLabel(packManager));
			if (Utilities.hasDefaultConfiguration(permissions, context, entry)) {
				tvDefault.setText("Default");
			} else {
				tvDefault.setText("Custom");
			}
		} catch (Exception e) {
			Log.d(Utilities.ERROR,
					"><<<<<<<<<<<<<<<" + Log.getStackTraceString(e));
		}
		return view;
	}
}
