package com.thesis.asa.devicedata;

import java.util.Arrays;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

public class DeviceDataSettings extends Resource {
	private DeviceDataInfoItem deviceDataSelectedSate;
	private boolean customSelected = false;

	public DeviceDataSettings(Context c) {
		super(c);
	}

	protected static String[] columns = SettingsDB.DEVICE_DATA_TABLE_COLUMNS;

	@Override
	public String tableName() {
		return SettingsDB.DEVICE_DATA_TABLE;
	}

	@Override
	public String permissions() {
		return Manifest.permission.READ_PHONE_STATE;
	}

	public Object[] loadSettingsFromConfiguration(Object configuration) {
		String[] deviceDataConfiguration = Utilities
				.stringToArray((String) configuration);
		setSelectedSate(deviceDataConfiguration);

		return deviceDataConfiguration;
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

	public void setSelectedSate(String[] strings) {
		deviceDataSelectedSate = new DeviceDataInfoItem(strings);

		customSelected = !(deviceDataSelectedSate.getDeviceId()
				.contains("Real")
				|| deviceDataSelectedSate.getDeviceId().contains("Fake") || deviceDataSelectedSate
				.getDeviceId().contains("Random"));
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
		return Uri
				.parse("content://com.thesis.asa.settings/device_data_settings");
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
				option = "Random";
			break;
		case PARANOID:
			option = "Random";
			break;
		}

		for (int index = 0; index < columns.length; index++) {
			configuration[index] = option;
		}

		return configuration;
	}

	public DeviceDataInfoItem getDeviceDataState() {
		if (!customSelected) {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			return new DeviceDataInfoItem(
					Secure.getString(context.getContentResolver(),
			                Secure.ANDROID_ID),
					telephonyManager.getDeviceId(),
					telephonyManager.getSubscriberId(),
					telephonyManager.getSimSerialNumber(),
					telephonyManager.getLine1Number());
		} else 
			return deviceDataSelectedSate;
	}

	public boolean isCustomSelected() {
		return customSelected;
	}
}
