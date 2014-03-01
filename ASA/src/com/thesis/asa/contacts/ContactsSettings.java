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
package com.thesis.asa.contacts;



import java.util.Arrays;
import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;



public class ContactsSettings extends Resource {

	public ContactsSettings(Context c) {
		super(c);
	}

	public String tableName() {
		return SettingsDB.CONTACTS_TABLE;
	}

	public ContentValues createDBEntry(String pkgName, String processes, Object[] selectedConfigurations) {
		ContentValues values = new ContentValues();
		
		values.put(SettingsDB.COL_PKG_NAME, pkgName);
		values.put(SettingsDB.COL_PROCESSES_NAMES, processes);
		Integer[] selectedConfigurationInt = new Integer[selectedConfigurations.length];
		
		for(int i = 0; i < selectedConfigurations.length; i++){
			selectedConfigurationInt[i] = Integer.parseInt(""+selectedConfigurations[i]);
		}
		
		values.put(SettingsDB.COL_GROUPS, Arrays.toString((Integer[]) selectedConfigurationInt));

		return values;
	}

	public Uri uri() {
		return Uri.parse("content://com.thesis.asa.settings/contacts_settings");
	}
	
	public Object configurationFromCursor(Cursor groups) { 
		int groupColumnIndex = groups.getColumnIndex(SettingsDB.COL_GROUPS);
		String filteredGroups = groups.getString(groupColumnIndex);		
	
		return filteredGroups;
	}
	
	public Object[] loadSettingsFromConfiguration(Object configuration) {
		String filteredGroups = (String) configuration;
		int[] groupIds = new int[0];
		
		int size = filteredGroups.length();
		if (size > 2) {
			filteredGroups = filteredGroups.substring(1, size-1);
			groupIds = Utilities.processLine(filteredGroups.split(","));
		}
		
		Object[] ids = new Object[groupIds.length];
		for (int index = 0; index < groupIds.length; index ++) {
			ids[index] = groupIds[index];
		}
		
		return ids;
	}

	public String permissions() {
		return Manifest.permission.READ_CONTACTS;				
	}

	protected String[] getGroupIds(Context context) {
		Cursor groups = context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
				new String[] { ContactsContract.Groups._ID }
				, null, null, null); 
		String[] ids = new String[groups.getCount()];
		if (groups != null && groups.moveToFirst()) {
			
			int idColumnIndex = groups.getColumnIndex(ContactsContract.Groups._ID);
			int index = 0;
			do{
					int id = groups.getInt(idColumnIndex);
					ids[index] = ""+id;
					index++;
			}
			while(groups.moveToNext());
		}
		
		if(groups != null && !groups.isClosed()){
			groups.close();
		}
		
		return ids;
	}
	
	@Override
	public Object[] getConfigurationByMode(SecurityMode mode, int isSystem) {	
    	String[] configuration = null; 
    	switch (mode) {
	        case PERMISSIVE:
	        	configuration = getGroupIds(context);
	        	break;
	        case SECURE:
	        	if (isSystem == 1)
	        		configuration= getGroupIds(context);
	        	else
	        		configuration = new String[0];
	        	break;
	        case PARANOID:
	        	configuration = new String[0];
	            break;
    	}           
	     
    	return configuration;
	}
}
