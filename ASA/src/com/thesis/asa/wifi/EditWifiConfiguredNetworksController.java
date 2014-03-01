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
package com.thesis.asa.wifi;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;

public class EditWifiConfiguredNetworksController implements OnClickListener,
		OnDismissListener {

	private EditWifiConfiguredNetworksDialog dialog;
	private Activity activity;
	private String ip;
	private String bssid;
	private String mac;
	private String ssid;

	public EditWifiConfiguredNetworksController(Activity a,
			EditWifiConfiguredNetworksDialog editDialog) {
		activity = a;
		dialog = editDialog;
	}

	@Override
	public void onClick(View v) {
		if (hasValidConfiguration()) {
			saveConfiguration();
			dialog.close();
		} else {
			Toast.makeText(
					activity,
					"The configuration you're attempting to save is not valid.",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void saveConfiguration() {
		WifiInfoItem previous = dialog.getSelectedItem();
		SettingsDB helper = new SettingsDB(activity);
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = SettingsDB.WIFI_TABLE_COLUMNS;
		ContentValues values = new ContentValues();
		WifiInfoItem item = new WifiInfoItem(ssid, Utilities.formatMac(bssid), ip, Utilities.formatMac(mac),
				dialog.getConfiguredNetworks(), dialog.getConfiguredScanned());
		String[] data = item.getWifiInfo();
		for (int i = 0; i < data.length; i++)
			values.put(columns[i], data[i]);

		String whereClause = SettingsDB.COL_SSID + "=? AND "
				+ SettingsDB.COL_BSSID + "=? AND " + SettingsDB.COL_IPADDRESS
				+ "=? AND " + SettingsDB.COL_MACADDRESS + "=? AND "
				+ SettingsDB.COL_CONFIGURATIONS + "=? AND "
				+ SettingsDB.COL_SCANS + "=?";
		String[] whereArgs = { previous.getSsid(), previous.getBssid(),
				previous.getIP(), previous.getMac(),
				previous.getConfiguredNetworks(), previous.getScannedNetworks() };
		try {
			db.update(SettingsDB.WIFI_STATES_TABLE, values, whereClause,
					whereArgs);
		} catch (Exception e) {
			Toast.makeText(activity, "This configuration already exists",
					Toast.LENGTH_SHORT).show();
		}
		db.close();
	}

	private boolean hasValidConfiguration() {
		ip = dialog.getConfiguredIp().trim();

		if (ip.endsWith(".") || ip.startsWith(".") || ip.contains(".."))
			return false;

		bssid = dialog.getConfiguredBssid().trim();
		if (bssid.length() != 12)
			return false;

		mac = dialog.getConfiguredMac().trim();
		if (mac.length() != 12)
			return false;

		ssid = dialog.getConfiguredSsid().trim();
		if (mac.isEmpty())
			return false;

		return true;
	}

	@Override
	public void onDismiss(DialogInterface d) {
		dialog.close();
	}

}
