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
 
 
package com.thesis.asa.wifi;

import java.util.List;


import android.view.View;
import android.widget.Toast;

public class WifiController implements android.view.View.OnClickListener {

	private WifiSettingsView view;
	private WifiSettings model;
	
	public WifiController(WifiSettingsView v, WifiSettings m) {
		view = v;	
		model = m;
	}
	
	private void showCustomWifiStoredConfigurations() {
		new SavedWifiStatesDialog(view.getActivity(), model).show();
	}

	@Override
	public void onClick(View v) {
		List<WifiInfoItem> configurations = WifiConfigurationsModel.getCustomWifiStoredConfigurations(view.getContext());
		if (configurations.size() == 0) {
			Toast.makeText(view.getContext(), "No saved WiFi information available. Add one using Settings menu", Toast.LENGTH_SHORT).show();
		} else {
			showCustomWifiStoredConfigurations();
		}
	}
}
