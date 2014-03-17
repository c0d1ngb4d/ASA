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

import com.google.android.gms.location.LocationClient;
import com.thesis.asa.Data;
import com.thesis.asa.R;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

public class AdvancedSettingsActivity extends PreferenceActivity {
	public static String SYSTEM = "is_system_app";
	private int pageNumber;
	
	private AdvancedSettingsController controller;
	private AdvancedLocationSettingsController locationController;
	private AdvancedWifiSettingsController wifiController;
	private LocationClient locationClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		pageNumber = intent.getIntExtra(SlidePageFragment.PAGE_NUMBER, -1);

		addPreferencesFromResource(R.layout.settings_layout);
		controller = new AdvancedSettingsController(this);
		
		if (Data.permissionsForPage(pageNumber).contains(
				Manifest.permission.ACCESS_WIFI_STATE)) {
			wifiController = new AdvancedWifiSettingsController(this);
			Preference custom = new Preference(this);
			custom.setTitle("Manage wifi saved state");
			custom.setSummary("Add, modify and remove custom wifi states");
			custom.setOnPreferenceClickListener(wifiController);
			PreferenceCategory category = (PreferenceCategory) findPreference("settings_key");
			category.addPreference(custom);
		}

		if (Data.permissionsForPage(pageNumber).contains(
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			locationController = new AdvancedLocationSettingsController(this);
			locationClient = new LocationClient(this, locationController, locationController);
			Preference custom = new Preference(this);
			custom.setTitle("Save current location");
			custom.setSummary("Add current location configuration for later use");
			custom.setOnPreferenceClickListener(locationController);
			PreferenceCategory category = (PreferenceCategory) findPreference("settings_key");
			category.addPreference(custom);
		}

		CheckBoxPreference preference = (CheckBoxPreference) findPreference("advanced_key");
		preference.setChecked(SlidePageFragment.ADVANCED_DISPLAYED);

		preference.setOnPreferenceChangeListener(controller);
		Preference system = (Preference) findPreference("system_key");
		system.setOnPreferenceClickListener(controller);

		Preference thirdParty = (Preference) findPreference("third_key");
		thirdParty.setOnPreferenceClickListener(controller);
	}

	public LocationClient getLocationClient() {
		return locationClient;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	@Override
    protected void onStop() {
        if (locationClient != null)
        	locationClient.disconnect();
        super.onStop();
    }
}
