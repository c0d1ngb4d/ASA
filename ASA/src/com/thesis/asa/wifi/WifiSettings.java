<<<<<<< HEAD
package com.thesis.asa.wifi;

import java.util.Arrays;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

public class WifiSettings extends Resource {

	protected static String[] columns = SettingsDB.WIFI_TABLE_COLUMNS;
	private boolean customSelected = false;

	private WifiInfoItem wifiSelectedSate;
	
	public WifiSettings(Context c) {
		super(c);
	}

	@Override
	public String tableName() {
		return SettingsDB.WIFI_TABLE;
	}

	@Override
	public String permissions() {
		return Manifest.permission.ACCESS_WIFI_STATE;
	}

	private String[] getCustomConfigurationFromString(String configuration) {
		String[] arrayConfiguration = new String[6];
		String firstPart = configuration.substring(0, configuration.indexOf("["));
		String[] data = firstPart.split(",");
		for (int index = 0; index<data.length; index++)
			arrayConfiguration[index] = data[index];
		
		int index = 4;
		int countBracket;
		int start = configuration.indexOf("["), ch = configuration.indexOf("[");
		
		while(ch < configuration.length()){
			ch++;		//avanzo el bracket
			countBracket = 1;
			while(countBracket > 0){
				if(ch == configuration.length()) break;
				char c = configuration.charAt(ch);
				if(c == ']') countBracket--;
				if(c == '[') countBracket++;
				ch++;
			}
			arrayConfiguration[index] = configuration.substring(start, ch);		
			index++;
			ch++;
			start = ch; //advance comma value
			
		}

		return arrayConfiguration;
	}
	
	private String[] getConfigurationFromString(String configuration) {
		String formatted = configuration.substring(1, configuration.length()-1);
		formatted = formatted.trim();
		if (formatted.indexOf("[") == -1) 
			return Utilities.stringToArray(configuration);
		else 
			return getCustomConfigurationFromString(formatted);
	}
	
	@Override
	public Object[] loadSettingsFromConfiguration(Object configuration) {
		String[] wifiConfiguration = getConfigurationFromString((String) configuration);
		setSelectedSate(wifiConfiguration);
		return wifiConfiguration;		
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
	        		option = "Fake";
	        	break;
	        case PARANOID:
	        	option = "Fake";
	            break;
    	}           
	     
    	for (int index = 0; index<columns.length; index++) {
	        configuration[index] = option;
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
		return Uri.parse("content://com.thesis.asa.settings/wifi_settings");
	}
	
	public void setSelectedSate(String[] strings) {
		wifiSelectedSate = new WifiInfoItem(strings);
		customSelected = !((wifiSelectedSate.getBssid()).contains("Real") || (wifiSelectedSate.getBssid()).contains("Fake"));
	}
	
	public WifiInfoItem getWifiState() {
		return wifiSelectedSate;
	}
	
	public boolean isCustomSelected() {
		return customSelected;
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

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

public class WifiSettings extends Resource {

	protected static String[] columns = SettingsDB.WIFI_TABLE_COLUMNS;
	private boolean customSelected = false;

	private WifiInfoItem wifiSelectedSate;
	
	public WifiSettings(Context c) {
		super(c);
	}

	@Override
	public String tableName() {
		return SettingsDB.WIFI_TABLE;
	}

	@Override
	public String permissions() {
		return Manifest.permission.ACCESS_WIFI_STATE;
	}

	private String[] getCustomConfigurationFromString(String configuration) {
		String[] arrayConfiguration = new String[6];
		String firstPart = configuration.substring(0, configuration.indexOf("["));
		String[] data = firstPart.split(",");
		for (int index = 0; index<data.length; index++)
			arrayConfiguration[index] = data[index];
		
		int index = 4;
		int countBracket;
		int start = configuration.indexOf("["), ch = configuration.indexOf("[");
		
		while(ch < configuration.length()){
			ch++;		//avanzo el bracket
			countBracket = 1;
			while(countBracket > 0){
				if(ch == configuration.length()) break;
				char c = configuration.charAt(ch);
				if(c == ']') countBracket--;
				if(c == '[') countBracket++;
				ch++;
			}
			arrayConfiguration[index] = configuration.substring(start, ch);		
			index++;
			ch++;
			start = ch; //advance comma value
			
		}

		return arrayConfiguration;
	}
	
	private String[] getConfigurationFromString(String configuration) {
		String formatted = configuration.substring(1, configuration.length()-1);
		formatted = formatted.trim();
		if (formatted.indexOf("[") == -1) 
			return Utilities.stringToArray(configuration);
		else 
			return getCustomConfigurationFromString(formatted);
	}
	
	@Override
	public Object[] loadSettingsFromConfiguration(Object configuration) {
		String[] wifiConfiguration = getConfigurationFromString((String) configuration);
		setSelectedSate(wifiConfiguration);
		return wifiConfiguration;		
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
	        		option = "Fake";
	        	break;
	        case PARANOID:
	        	option = "Fake";
	            break;
    	}           
	     
    	for (int index = 0; index<columns.length; index++) {
	        configuration[index] = option;
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
		return Uri.parse("content://com.thesis.asa.settings/wifi_settings");
	}
	
	public void setSelectedSate(String[] strings) {
		wifiSelectedSate = new WifiInfoItem(strings);
		customSelected = !((wifiSelectedSate.getBssid()).contains("Real") || (wifiSelectedSate.getBssid()).contains("Fake"));
	}
	
	public WifiInfoItem getWifiState() {
		return wifiSelectedSate;
	}
	
	public boolean isCustomSelected() {
		return customSelected;
	}
}
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
