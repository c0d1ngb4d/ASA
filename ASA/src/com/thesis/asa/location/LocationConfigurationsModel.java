<<<<<<< HEAD
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
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
package com.thesis.asa.location;

import java.util.ArrayList;
import java.util.List;

import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationConfigurationsModel {
	private Activity activity;
	private List<LocationInfoItem> customLocationStoredConfigurations;

	public LocationConfigurationsModel(Activity a) {
		activity = a;
		initializeCustomLocationStoreConfigurations();
	}
	
	public List<LocationInfoItem> getCustomLocationStoredConfigurations() {
		return customLocationStoredConfigurations;
	}
	
	public LocationInfoItem findLocationBy(Double lat, Double lon) {
		for (LocationInfoItem item : customLocationStoredConfigurations) {
			if (Utilities.areCloseEnough(item.getLatitude(), lat) && Utilities.areCloseEnough(item.getLongitude(), lon))
				return item;
		}
		return null;
	}

	private void initializeCustomLocationStoreConfigurations() {
		SettingsDB helper = new SettingsDB(activity);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor saved = db.query(SettingsDB.LOCATION_STATES_TABLE, null, null, null, null, null, null);
		List<LocationInfoItem> configuration = new ArrayList<LocationInfoItem>();
		
		if (saved != null && saved.moveToFirst() && saved.getCount()>0) {
			LocationInfoItem value;
			Object[] parameters;
			int index;
			do {
				parameters = new Object[5];
				index = 0;
				for (String columnName : SettingsDB.LOCATION_TABLE_COLUMNS) {
					if (columnName == SettingsDB.COL_CONFIGURATIONS) 
						parameters[index] = "[]" ;
					else {
						int columnIndex = saved.getColumnIndex(columnName);
						parameters[index] = saved.getString(columnIndex);
					}
					
					index++;
				}

				value = new LocationInfoItem(parameters);
				configuration.add(value);
			} while (saved.moveToNext());
		}
		
		if (saved != null) 
			saved.close();
		db.close();
		
		customLocationStoredConfigurations = configuration;
	}
}
