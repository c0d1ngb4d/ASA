package com.thesis.asa.wifi;

import java.util.ArrayList;
import java.util.List;

import com.thesis.asa.provider.SettingsDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiConfigurationsModel {
	
	public static List<String> getConfiguredNetworks(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		List<WifiConfiguration> networks = wifiManager.getConfiguredNetworks();

		List<String> configuredSsids = new ArrayList<String>();
		for (WifiConfiguration configuration : networks) {
			configuredSsids.add(configuration.SSID);
		}

		return configuredSsids;
	}

	public static List<WifiInfoItem> getCustomWifiStoredConfigurations(Context context) {
		
		SettingsDB helper = new SettingsDB(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor saved = db.query(SettingsDB.WIFI_STATES_TABLE, null, null, null,
				null, null, null);
		List<WifiInfoItem> configuration = new ArrayList<WifiInfoItem>();

		if (saved != null && saved.moveToFirst() && saved.getCount() > 0) {
			WifiInfoItem value;
			Object[] parameters;
			int index;
			do {
				parameters = new Object[SettingsDB.WIFI_TABLE_COLUMNS.length];
				index = 0;
				for (String columnName : SettingsDB.WIFI_TABLE_COLUMNS) {
					int columnIndex = saved.getColumnIndex(columnName);
					parameters[index] = saved.getString(columnIndex);
					index++;
				}

				value = new WifiInfoItem(parameters);
				configuration.add(value);
			} while (saved.moveToNext());
		}

		if (saved != null)
			saved.close();
		db.close();

		return configuration;
	}
}
