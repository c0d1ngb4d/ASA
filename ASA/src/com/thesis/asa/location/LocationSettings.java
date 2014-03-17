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
 

package com.thesis.asa.location;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

public class LocationSettings extends Resource {

	protected static String[] columns = SettingsDB.LOCATION_TABLE_COLUMNS;

	private LocationInfoItem locationSelectedSate;

	public LocationSettings(Context c) {
		super(c);
	}

	@Override
	public String tableName() {
		return SettingsDB.LOCATION_TABLE;
	}

	@Override
	public String permissions() {
		return Manifest.permission.ACCESS_COARSE_LOCATION + "-"
				+ Manifest.permission.ACCESS_FINE_LOCATION;
	}

	private String[] getCustomConfigurationFromString(String configuration) {
		String[] arrayConfiguration = new String[columns.length];
		// Create a Pattern object
		String pattern = "\\[(.*?)\\]";
		String firstPart = configuration.substring(0,
				configuration.indexOf("["));
		String[] data = firstPart.split(",");
		for (int index = 0; index < data.length; index++)
			arrayConfiguration[index] = data[index];

		Pattern r = Pattern.compile(pattern);
		// Now create matcher object.
		Matcher m = r.matcher(configuration);
		int i = 2;
		while (m.find()) {
			arrayConfiguration[i] = m.group(0);
			i++;
		}
		return arrayConfiguration;
	}

	private String[] getConfigurationFromString(String configuration) { 	
		String formatted = configuration.substring(1,
				configuration.length() - 1);
		formatted = formatted.trim();
		if (formatted.indexOf("[") == -1)
			return Utilities.stringToArray(configuration);
		else {
			return getCustomConfigurationFromString(formatted);
		}
	}

	@Override
	public Object[] loadSettingsFromConfiguration(Object configuration) {
		String[] locationConfiguration = getConfigurationFromString((String) configuration);
		locationSelectedSate = new LocationInfoItem(locationConfiguration);
		return locationConfiguration;
	}

	@Override
	public ContentValues createDBEntry(String pkgName, String processes,
			Object[] selectedConfigurations) {
		ContentValues values = new ContentValues();

		values.put(SettingsDB.COL_PKG_NAME, pkgName);
		values.put(SettingsDB.COL_PROCESSES_NAMES, processes);

		for (int index = 0; index < columns.length; index++)
			values.put(columns[index], (String) selectedConfigurations[index]);

		return values;
	}

	@Override
	public Object[] getConfigurationByMode(SecurityMode mode, int isSystem) {
		String[] configuration = new String[columns.length];
		String option = null;

		switch (mode) {
		case PERMISSIVE:
			option = "Real";
			break;
		case SECURE:
			if (isSystem == 1)
				option = "Real";
			else
				option = "null";
			break;
		case PARANOID:
			option = "null";
			break;
		}

		for (int index = 0; index < columns.length; index++) 
			configuration[index] = option;
		
		if (configuration[0] == "null") {
			configuration[0] = String.valueOf(Utilities.fakeLatitude);
			configuration[1] = String.valueOf(Utilities.fakeLongitude);
		}

		return configuration;
	}

	@Override
	public Object configurationFromCursor(Cursor cursor) {
		String[] configuration = new String[columns.length];

		for (int index = 0; index < columns.length; index++) {
			String column = columns[index];
			int columnIndex = cursor.getColumnIndex(column);
			configuration[index] = cursor.getString(columnIndex);
		}

		return Arrays.toString(configuration);
	}

	@Override
	public Uri uri() {
		return Uri.parse("content://com.thesis.asa.settings/location_settings");
	}

	public void setLocationSelectedSate(String[] strings) {
		locationSelectedSate = new LocationInfoItem(strings);
	}

	public String[] getConfiguration() {
		return locationSelectedSate.getLocationInfo();
	}

	public LocationInfoItem getLocationState() {
		return locationSelectedSate;
	}

	public boolean isMapSelected() {
		return locationSelectedSate.getCellLocation().contains("null");
	}

	public boolean isCustomSelected() {
		return !(isMapSelected() || isRealSelected());
	}

	public boolean isRealSelected() {
		return (locationSelectedSate.getCellLocation()).contains("Real");
	}
}
