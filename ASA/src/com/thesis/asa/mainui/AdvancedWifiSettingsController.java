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
package com.thesis.asa.mainui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.wifi.WifiInfoAdapter;
import com.thesis.asa.wifi.WifiInfoItem;

public class AdvancedWifiSettingsController implements
		OnPreferenceClickListener {

	private AdvancedSettingsActivity activity;
	private WifiInfoAdapter wifiInfoAdapter;

	public AdvancedWifiSettingsController(AdvancedSettingsActivity a) {
		activity = a;
	}

	private boolean hasWifiConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return wifi != null && wifi.isConnected();
	}

	private void showSavedWifiStates() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Manage saved Wifi state");

		SettingsDB helper = new SettingsDB(activity);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor saved = db.query(SettingsDB.WIFI_STATES_TABLE, null, null, null,
				null, null, null);
		wifiInfoAdapter = new WifiInfoAdapter(activity,
				android.R.layout.select_dialog_item);

		if (saved != null && saved.moveToFirst() && saved.getCount() > 0) {
			WifiInfoItem value;
			Object[] parameters;
			int index;
			do {
				parameters = new Object[6];
				index = 0;
				for (String columnName : saved.getColumnNames()) {
					int columnIndex = saved.getColumnIndex(columnName);
					parameters[index] = saved.getString(columnIndex);
					index++;
				}
				value = new WifiInfoItem(parameters);
				wifiInfoAdapter.add(value);
			} while (saved.moveToNext());
		}

		if (saved != null)
			saved.close();
		db.close();

		builder.setAdapter(wifiInfoAdapter, null);

		final AlertDialog alertDialog = builder.create();

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
				"Add current Wifi state",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int position) {

					}
				});

		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int position) {

					}
				});

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Apply",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int position) {

						SettingsDB helper = new SettingsDB(wifiInfoAdapter
								.getContext());
						SQLiteDatabase db = helper.getWritableDatabase();
						String[] columns = SettingsDB.WIFI_TABLE_COLUMNS;
						ContentValues values = new ContentValues();
						WifiInfoItem item;
						for (int index = 0; index < wifiInfoAdapter.getCount(); index++) {
							item = wifiInfoAdapter.getItem(index);
							String[] data = item.getWifiInfo();
							for (int i = 0; i < data.length; i++)
								values.put(columns[i], data[i]);

							db.replace(SettingsDB.WIFI_STATES_TABLE, null,
									values);
						}

						db.close();
					}
				});

		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {

						WifiManager wifiManager = (WifiManager) activity
								.getSystemService(Context.WIFI_SERVICE);
						WifiInfo currentWifi = wifiManager.getConnectionInfo();

						if (!hasWifiConnectivity()) {
							Toast.makeText(view.getContext(),
									"No wifi connectivity available",
									Toast.LENGTH_SHORT).show();
							return;
						}

						int ipAddress = currentWifi.getIpAddress();
						String formated = Utilities
								.getFormatedIpFromIp(ipAddress);
						List<String> scannedSsids = Utilities.getScannedWifis(wifiManager);
												
						List<WifiConfiguration> configured = wifiManager
								.getConfiguredNetworks();

						List<String> configuredNetworks = new ArrayList<String>();
						if (configured != null) {
							for (WifiConfiguration network : configured)
								configuredNetworks.add(network.SSID);
						}

						WifiInfoItem info = new WifiInfoItem(currentWifi
								.getSSID(), currentWifi.getBSSID(), formated,
								currentWifi.getMacAddress(),
								configuredNetworks, scannedSsids);
						wifiInfoAdapter.add(info);
					}
				});
			}
		});
		alertDialog.show();

		Button button = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
		button.setEnabled(hasWifiConnectivity());

	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		showSavedWifiStates();
		return true;
	}
}
