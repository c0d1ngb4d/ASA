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

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.thesis.asa.R;
import com.thesis.asa.resourcemvc.EditableResourceView;
import com.thesis.asa.resourcemvc.Resource;
import com.thesis.asa.resourcemvc.ResourceActivity;

public class WifiSettingsView extends EditableResourceView {

	
	public WifiSettingsView(ResourceActivity a, Resource r) {
		super(a, r);
		
		dataInitialization();
		activity.setContentView(R.layout.wifi_layout);
		controller = new WifiController(this, (WifiSettings) model);
		initializeEditable();
	}

	private void dataInitialization() {
		properties = new String[6];
		properties[0] = "Ssid";
		properties[1] = "Bssid";
		properties[2] = "IpAddress";
		properties[3] = "MacAddress";
		properties[4] = "ConfiguredNetworks";
		properties[5] = "ScanResults";
		
		ids = new int[6];
		ids[0] = R.id.radioGroupSsid;
		ids[1] = R.id.radioGroupBssid;
		ids[2] = R.id.radioGroupIpAddress;
		ids[3] = R.id.radioGroupMacAddress;
		ids[4] = R.id.radioGroupConfiguredNetworks;
		ids[5] = R.id.radioGroupScanResults;
	}

	@Override
	public void showResourceViewIn() {
	}

	@Override
	public Object[] getSelectedConfiguration() {
		if (((WifiSettings) model).isCustomSelected())
			return ((WifiSettings) model).getWifiState().getWifiInfo();
		String[] configuration = new String[ids.length];
		RadioGroup group;
		RadioButton button;

		for (int i = 0; i < ids.length; i++) {
			group = (RadioGroup) activity.findViewById(ids[i]);
			int id = group.getCheckedRadioButtonId();
			button = (RadioButton) activity.findViewById(id);
			configuration[i] = button.getText().toString();
		}

		return configuration;
	}

	@Override
	public int getLayoutID() {
		return R.id.wifi_linear_layout;
	}

}
