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
 

package com.thesis.asa.internet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.thesis.asa.R;
import com.thesis.asa.resourcemvc.Resource;
import com.thesis.asa.resourcemvc.ResourceActivity;
import com.thesis.asa.resourcemvc.ResourceView;

public class InternetSettingsView extends ResourceView {
	
	private int[] ids;
	private String[] properties;

	public InternetSettingsView(ResourceActivity a, Resource r) {
		super(a, r);
		
		dataInitialization();		
		
		activity.setContentView(R.layout.internet_layout);

	}

	private void dataInitialization() {
		properties = new String[1];
		properties[0] = "JavaScriptEnable";

		ids = new int[1];
		ids[0] = R.id.radioGroupJavascriptEnable;
	}

	@Override
	public void showResourceViewIn() {
	}

	@Override
	public Object[] getSelectedConfiguration() {

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
		return R.id.internet_linear_layout;
	}

	@Override
	public void displaySettingsFromConfiguration(Object[] settings) {
		InternetItem item = new InternetItem(settings);
		RadioButton check;
		if(item.getJavascriptEnabled()){
			check= (RadioButton) activity.findViewById(R.id.JavaScriptEnableId);
		}
		else{
			check = (RadioButton) activity.findViewById(R.id.JavaScriptDisableId);			
		}
		check.setChecked(true);
	}
}
