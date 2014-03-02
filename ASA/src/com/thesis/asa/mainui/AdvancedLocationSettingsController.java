<<<<<<< HEAD
package com.thesis.asa.mainui;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;

public class AdvancedLocationSettingsController implements OnConnectionFailedListener,
		ConnectionCallbacks, OnPreferenceClickListener {

	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final long FASTEST_INTERVAL = 0;
	private static final long UPDATE_INTERVAL = 0;
	private AdvancedSettingsActivity activity;
	private LocationRequest mLocationRequest;

	private final class ASALocationListener implements LocationListener {
	     @Override
	     public void onLocationChanged(Location location) {
	         // Report to the UI that the location was updated
	 		String[] data = new String[5];
			data[0] = String.valueOf(location.getLatitude());
			data[1] = String.valueOf(location.getLongitude());
			TelephonyManager telephonyManager = (TelephonyManager) activity
					.getSystemService(Context.TELEPHONY_SERVICE);
			CellLocation cellLocation = telephonyManager.getCellLocation();
			String cellLocationData = "[]";
			switch (telephonyManager.getPhoneType()) {
			case TelephonyManager.PHONE_TYPE_NONE:
				cellLocationData = "[" + TelephonyManager.PHONE_TYPE_NONE + "]";
				break;
			case TelephonyManager.PHONE_TYPE_CDMA:
				CdmaCellLocation cdmaLocation = (CdmaCellLocation) cellLocation;
				cellLocationData = "[" + TelephonyManager.PHONE_TYPE_CDMA
						+ cdmaLocation.getBaseStationId() + ", "
						+ cdmaLocation.getBaseStationLatitude() + ", "
						+ cdmaLocation.getBaseStationLongitude() + ", " 
						+ cdmaLocation.getSystemId() + ", "
						+ cdmaLocation.getNetworkId() + "]";
				break;
			case TelephonyManager.PHONE_TYPE_GSM:
				GsmCellLocation gsmLocation = (GsmCellLocation) cellLocation;
				cellLocationData = "[" + TelephonyManager.PHONE_TYPE_GSM + ", "
						+ gsmLocation.getLac() + ", " + gsmLocation.getCid() + ", "
						+ gsmLocation.getPsc() + "]";
				break;
			}
			;

			data[2] = cellLocationData;
			data[3] = "[]";
			data[4] = "[]";

			SettingsDB helper = new SettingsDB(activity.getBaseContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			String[] columns = SettingsDB.LOCATION_TABLE_COLUMNS;
			ContentValues values = new ContentValues();

			for (int i = 0; i < columns.length; i++)
				values.put(columns[i], data[i]);

			db.replace(SettingsDB.LOCATION_STATES_TABLE, null, values);
			Toast.makeText(activity, "Current location has been added",
					Toast.LENGTH_SHORT).show();
			db.close();
			
			activity.getLocationClient().removeLocationUpdates(this);
	      }

	}
	
	public AdvancedLocationSettingsController(AdvancedSettingsActivity a) {
		activity = a;
	}

	@Override
	public void onConnected(Bundle arg0) {

		   mLocationRequest = LocationRequest.create();
		   mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);    // Use high accuracy
		    mLocationRequest.setInterval(UPDATE_INTERVAL);  // Setting the update interval to  5mins
		    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);  // Set the fastest update interval to 1 min
		    LocationListener locationListener = new ASALocationListener();
		    activity.getLocationClient().requestLocationUpdates(mLocationRequest,locationListener);
	}

	@Override
	public void onDisconnected() {
		Log.d(Utilities.ERROR, "CONECTION DISCONNECTED");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(Utilities.ERROR, "CONECTION FAILED");
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(activity,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(activity,
					"No location found (it may be a connectivity problem)",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		activity.getLocationClient().connect();		
		return true;
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
package com.thesis.asa.mainui;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;

public class AdvancedLocationSettingsController implements OnConnectionFailedListener,
		ConnectionCallbacks, OnPreferenceClickListener {

	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final long FASTEST_INTERVAL = 0;
	private static final long UPDATE_INTERVAL = 0;
	private AdvancedSettingsActivity activity;
	private LocationRequest mLocationRequest;

	private final class ASALocationListener implements LocationListener {
	     @Override
	     public void onLocationChanged(Location location) {
	         // Report to the UI that the location was updated
	 		String[] data = new String[5];
			data[0] = String.valueOf(location.getLatitude());
			data[1] = String.valueOf(location.getLongitude());
			TelephonyManager telephonyManager = (TelephonyManager) activity
					.getSystemService(Context.TELEPHONY_SERVICE);
			CellLocation cellLocation = telephonyManager.getCellLocation();
			String cellLocationData = "[]";
			switch (telephonyManager.getPhoneType()) {
			case TelephonyManager.PHONE_TYPE_NONE:
				cellLocationData = "[" + TelephonyManager.PHONE_TYPE_NONE + "]";
				break;
			case TelephonyManager.PHONE_TYPE_CDMA:
				CdmaCellLocation cdmaLocation = (CdmaCellLocation) cellLocation;
				cellLocationData = "[" + TelephonyManager.PHONE_TYPE_CDMA
						+ cdmaLocation.getBaseStationId() + ", "
						+ cdmaLocation.getBaseStationLatitude() + ", "
						+ cdmaLocation.getBaseStationLongitude() + ", " 
						+ cdmaLocation.getSystemId() + ", "
						+ cdmaLocation.getNetworkId() + "]";
				break;
			case TelephonyManager.PHONE_TYPE_GSM:
				GsmCellLocation gsmLocation = (GsmCellLocation) cellLocation;
				cellLocationData = "[" + TelephonyManager.PHONE_TYPE_GSM + ", "
						+ gsmLocation.getLac() + ", " + gsmLocation.getCid() + ", "
						+ gsmLocation.getPsc() + "]";
				break;
			}
			;

			data[2] = cellLocationData;
			data[3] = "[]";
			data[4] = "[]";

			SettingsDB helper = new SettingsDB(activity.getBaseContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			String[] columns = SettingsDB.LOCATION_TABLE_COLUMNS;
			ContentValues values = new ContentValues();

			for (int i = 0; i < columns.length; i++)
				values.put(columns[i], data[i]);

			db.replace(SettingsDB.LOCATION_STATES_TABLE, null, values);
			Toast.makeText(activity, "Current location has been added",
					Toast.LENGTH_SHORT).show();
			db.close();
			
			activity.getLocationClient().removeLocationUpdates(this);
	      }

	}
	
	public AdvancedLocationSettingsController(AdvancedSettingsActivity a) {
		activity = a;
	}

	@Override
	public void onConnected(Bundle arg0) {

		   mLocationRequest = LocationRequest.create();
		   mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);    // Use high accuracy
		    mLocationRequest.setInterval(UPDATE_INTERVAL);  // Setting the update interval to  5mins
		    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);  // Set the fastest update interval to 1 min
		    LocationListener locationListener = new ASALocationListener();
		    activity.getLocationClient().requestLocationUpdates(mLocationRequest,locationListener);
	}

	@Override
	public void onDisconnected() {
		Log.d(Utilities.ERROR, "CONECTION DISCONNECTED");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(Utilities.ERROR, "CONECTION FAILED");
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(activity,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(activity,
					"No location found (it may be a connectivity problem)",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		activity.getLocationClient().connect();		
		return true;
	}
}
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
