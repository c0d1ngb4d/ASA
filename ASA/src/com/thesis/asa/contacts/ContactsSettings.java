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
