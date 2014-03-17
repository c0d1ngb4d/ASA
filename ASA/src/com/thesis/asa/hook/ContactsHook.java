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
 
package com.thesis.asa.hook;

import java.lang.reflect.Method;
import java.util.Arrays;

import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.saurik.substrate.MS;
import com.thesis.asa.contacts.ContactsSettings;

public class ContactsHook extends Hook { 	
    
	private static void printQueryArgs(String tag, Object... args) {
		// final Cursor query( Uri uri,
		// String[] projection,
		// String selection,
		// String[] selectionArgs, 
		// String sortOrder )
		
		Log.d(tag,("URI: " + (Uri) args[0]).toString());
		
		if (args[1] != null) {
			Log.d(tag, "PROJECTION: ");
			String[] projection = (String[]) args[1];
			for (int p = 0; p<projection.length; p++)
				Log.d(tag, projection[p]);
		}
		if (args[2] != null) 
			Log.d(tag, "SELECTION: "+ (String) args[2]);
		if (args[3] != null) {
			Log.d(tag, "SELECTION ARGS: ");
			String [] selectionArgs = (String[]) args[3];
			for (int p = 0; p<selectionArgs.length; p++)
				Log.d(tag, selectionArgs[p]);
		}

	}
	
	private static void printResult(String tag, Cursor cursor) {
		Log.d(tag, DatabaseUtils.dumpCursorToString(cursor));
	}
	
	
	public static void hook() {
		MS.hookClassLoad("com.android.providers.contacts.ContactsProvider2",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> contentProvider) {
						Method query;
						try {
							Class[] params = new Class[5];
							params[0] = Uri.class;
							params[1] = String[].class;
							params[2] = String.class;
							params[3] = String[].class;
							params[4] = String.class;

							query = contentProvider.getMethod("query", params);
						} catch (NoSuchMethodException e) {
							query = null;
						}

						if (query != null) {
							MS.hookMethod(
									contentProvider,
									query,
									new MS.MethodAlteration<ContentProvider, Cursor>() {
										
										public Cursor invoked(
												ContentProvider contentProvider,
												Object... args)
												throws Throwable {
										
										Context context = contentProvider.getContext();
										Boolean hooked = true;
										
										Object[] filteredGroups = Hook.queryConfigurationFromASA(context, new ContactsSettings(context));
										Integer[] groups = Arrays.copyOf(filteredGroups, filteredGroups.length, Integer[].class);
										String contactsInGroups = ContactsHook.getContactsByGroups(this, contentProvider, groups);
										String selection = (String) args[2];
										Uri id = (Uri) args[0];
										if (selection == null || selection.replaceAll("\\s","").length() == 0){
											selection = "";
										}
										else
											selection = " AND "
													+ selection;
										
										if(id.toString().contains(ContactsContract.Data.CONTENT_URI.toString())){
													
													selection = ContactsContract.Data.CONTACT_ID
													+ " IN "
													+ contactsInGroups
													+ selection;																}
										else{
											if(id.toString().contains(ContactsContract.RawContacts.CONTENT_URI.toString())){
												selection = ContactsContract.RawContacts.CONTACT_ID
												+ " IN "
												+ contactsInGroups
												+ selection;			
											}
											else{
												if(id.toString().contains(ContactsContract.Contacts.CONTENT_URI.toString())){
													selection = ContactsContract.Contacts._ID
															+ " IN "
															+ contactsInGroups
															+ selection;			
												}
												else{
													//APARENTEMENTE GRUPOS
													
													selection = (String) args[2];
													hooked = false;
												}
											}
										}

										args[2] = selection;

										Cursor result = invoke(contentProvider, args);
										
										return result;
										}
									});
						}
					}
		});
	}
	
	protected static String uriPath() {
		return "content://com.thesis.asa.settings/contacts_settings";
	}
	
	private static String getContactsByGroups(MS.MethodAlteration<ContentProvider, Cursor> hookedMethod, ContentProvider contentProvider, Integer[] filteredGroups) throws Throwable {		
		Object[] groupsArgs = new Object[5];
		groupsArgs[0] = ContactsContract.Data.CONTENT_URI;
		groupsArgs[1] = new String[] {
				ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.DATA1 };
		groupsArgs[2] = ContactsContract.Data.MIMETYPE + "=?";
		groupsArgs[3] = new String[] { ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE };
	
		Cursor contactsByGroup =  hookedMethod.invoke(
				contentProvider,
				groupsArgs);
									
		String contactsInGroups = new String();
		contactsInGroups = contactsInGroups
				+ "(";
	
		if (contactsByGroup != null
			&& contactsByGroup
					.moveToFirst()) {

		int contactColumnIndex = contactsByGroup
				.getColumnIndex(ContactsContract.Data.CONTACT_ID);

		int groupColumnIndex = contactsByGroup
				.getColumnIndex(ContactsContract.Data.DATA1);
		do {
			int group = contactsByGroup
					.getInt(groupColumnIndex);
			
			for (int i = 0; i < filteredGroups.length; i++) {
				if (filteredGroups[i] == group) {
					contactsInGroups = contactsInGroups
							+ "'"
							+ contactsByGroup
									.getInt(contactColumnIndex)
							+ "',";
					break;
				}
			}
		} while (contactsByGroup
				.moveToNext());

		if (contactsInGroups
				.length() > 1)
			contactsInGroups = contactsInGroups
					.substring(
							0,
							contactsInGroups
									.length() - 1);

		contactsInGroups = contactsInGroups
				+ ")"; }
		if (contactsByGroup != null)
			contactsByGroup.close();

		return contactsInGroups;
	}
}
