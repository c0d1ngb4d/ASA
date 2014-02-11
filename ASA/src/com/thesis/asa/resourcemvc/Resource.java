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
package com.thesis.asa.resourcemvc;

import java.util.Arrays;

import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public abstract class Resource {

	protected Context context;

	public Resource(Context c) {
		context = c;
	}

	public abstract String tableName();

	public abstract String permissions();

	public abstract Object[] loadSettingsFromConfiguration(Object configuration);

	public abstract ContentValues createDBEntry(String pkgName,
			String processes, Object[] selectedConfigurations);

	public void insertDefaultConfigurationForMode(SecurityMode mode) {
		SettingsDB helper = new SettingsDB(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = createConfigurationFor(mode, 0);
		db.replace(SettingsDB.DEFAULT_TABLE, null, values);
		values = createConfigurationFor(mode, 1);
		db.replace(SettingsDB.DEFAULT_TABLE, null, values);
		db.close();
	}

	private ContentValues createConfigurationFor(SecurityMode mode, int isSystem) {
		ContentValues values = new ContentValues();
		values.put(SettingsDB.COL_PERMISSION, permissions());
		values.put(SettingsDB.COL_SYSTEM, isSystem);
		Object[] configuration = getConfigurationByMode(mode, isSystem);
		values.put(SettingsDB.COL_CONFIGURATION, Arrays.toString(configuration));
		return values;
	}

	public abstract Object[] getConfigurationByMode(SecurityMode mode,
			int isSystem);

	public void setSelectedSate(String[] strings) {}

	public Cursor getConfigurationForAppsAndProcess(int uid, int pid) {
		String[] packages = context.getPackageManager().getPackagesForUid(uid);
		String process = Utilities.getProcessNameByPid(pid);
		String pkgNames = Arrays.toString(packages).replace("[", "(\'")
				.replace("]", "\')").replace(",", "\',\'");

		String selection = SettingsDB.COL_PKG_NAME + " IN " + pkgNames
				+ " AND " + SettingsDB.COL_PROCESSES_NAMES + " LIKE \'%"
				+ process + "%\'";
		Cursor resourceCursor = context.getContentResolver().query(uri(), null,
				selection, null, null);
		return resourceCursor;
	}

	public abstract Object configurationFromCursor(Cursor cursor);

	public abstract Uri uri();

	public Object defaultConfiguration(int isSystem) {
		Uri uri = Uri
				.parse("content://com.thesis.asa.settings/default_settings");
		String[] projection = new String[] { SettingsDB.COL_CONFIGURATION };
		String selection = SettingsDB.COL_PERMISSION + "=? AND "
				+ SettingsDB.COL_SYSTEM + "=" + isSystem;
		String[] selectionArgs = new String[] { permissions() };
		Cursor defaultResource = context.getContentResolver().query(uri,
				projection, selection, selectionArgs, null);

		String defaultConfiguration = "";

		if (defaultResource != null && defaultResource.moveToFirst()
				&& defaultResource.getCount() > 0) {
			int confColumnIndex = defaultResource
					.getColumnIndex(SettingsDB.COL_CONFIGURATION);
			defaultConfiguration = defaultResource.getString(confColumnIndex);
		} else {
			Log.d(Utilities.ERROR,
					"NO DEFAULT CONFIGURATIOOOOON! BAD ASA, BAD! NO DOUNUT FOR YOU!");
		}

		if (defaultResource != null) {
			defaultResource.close();
		}

		return defaultConfiguration;
	}

	protected Object[] getASAConfiguration() {
		return getConfigurationByMode(SecurityMode.PERMISSIVE, 0);
	}

	public void loadASAConfiguration(String pkgName, String[] processes) {
		Object[] configuration = getASAConfiguration();
		updateSettingsDB(pkgName, processes, configuration);
	}

	public void updateSettingsDB(String pkgName, String[] processes,
			Object[] configuration) {
		SettingsDB helper = new SettingsDB(context);
		SQLiteDatabase db = helper.getWritableDatabase();

		String processesNames = Arrays.toString(processes).replace("[", "(\'")
				.replace("]", "\')").replace(",", "\',\'");

		ContentValues values = createDBEntry(pkgName, processesNames,
				configuration);

		db.replace(tableName(), null, values);
		db.close();
	}

	public static Object[] getConfiguration(SecurityMode mode, int isSystem,
			Resource resource) throws Exception {
		return resource.getConfigurationByMode(mode, isSystem);
	}

}
