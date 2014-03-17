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

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Looper;
import android.util.Log;

import com.saurik.substrate.MS;
import com.thesis.asa.Utilities;
import com.thesis.asa.location.LocationSettings;

public class LocationHook extends Hook {

	private static Class<?> locationManager;
	private static FakeLocationListener fakeListener;
	
	public static void hook() {

		MS.hookClassLoad("android.location.LocationManager",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> _clazz) {
						fakeListener = new FakeLocationListener();
						locationManager = _clazz;
						hookRequestUpdates();
						hookGetLastKnownLocation();
					}
				});

	}

	protected static Location getLocationIn(double latitude, double longitude,
			String provider) {
		Location location = new Location(provider);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		location.setAccuracy(0.2F);
		location.setTime(System.currentTimeMillis());
		return location;
	}

	protected static void hookGetLastKnownLocation() {
		Method method;
		try {
			Class<?>[] params = new Class[1];
			params[0] = String.class;

			method = locationManager.getMethod("getLastKnownLocation", params);
		} catch (NoSuchMethodException e) {
			method = null;
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
		}

		if (method != null) {
			MS.hookMethod(locationManager, method,
					new MS.MethodAlteration<LocationManager, Location>() {
						public Location invoked(final LocationManager hooked,
								final Object... args) throws Throwable {

							Object[] properties = getProperties(LocationSettings.class
									.getName());
							if (properties[0].equals("Real"))
								return invoke(hooked, args);

							return getLocationIn(
									Double.parseDouble((String) properties[0]),
									Double.parseDouble((String) properties[1]),
									(String) args[0]);
						}

					});
		}

	}

	private static void hookRequestUpdates() {
		Method[] methods = new Method[2];
		Class<?>[] params = null;
		for (int i = 0; i < methods.length; i++) {
			Method method = null;
			if (i == 0) {
				params = new Class[6];
				params[0] = String.class;
				params[1] = Criteria.class;
				params[2] = long.class;
				params[3] = float.class;
				params[4] = boolean.class;
				params[5] = PendingIntent.class;
			}

			if (i == 1) {
				params = new Class[7];
				params[0] = String.class;
				params[1] = Criteria.class;
				params[2] = long.class;
				params[3] = float.class;
				params[4] = boolean.class;
				params[5] = LocationListener.class;
				params[6] = Looper.class;
			}

			try {
				methods[i] = locationManager.getDeclaredMethod(
						"_requestLocationUpdates", params);
				method = methods[i];
			} catch (NoSuchMethodException e) {
				methods[i] = null;
				Log.d(Utilities.ERROR, "No such method i = " + i
						+ " _requestLocationUpdates");
			}

			if (method != null) {
				MS.hookMethod(locationManager, method,
						new MS.MethodAlteration<LocationManager, Void>() {
							public Void invoked(final LocationManager hooked,
									final Object... args) throws Throwable {

								String classname = LocationSettings.class.getName();
								Object[] properties = getProperties(classname);
								
								if (properties[0].equals("Real"))
									return invoke(hooked, args);

								Location location = getLocationIn(
										Double.parseDouble((String) properties[0]),
										Double.parseDouble((String) properties[1]),
										(String) args[0]);

								try {
									LocationListener listener = (LocationListener) args[5];
									fakeListener.setListener(listener);
									fakeListener.setLocation(location);
									args[5] = fakeListener;
								} catch (Exception e) {
									try {
										// TODO: we are not doing anything in
										// this case
										Log.d(Utilities.DEBUG,
												"_requestUpdates was called with PendingIntent, ASA");
										PendingIntent intent = (PendingIntent) args[5];
									} catch (Exception e2) {
										Log.d(Utilities.ERROR,
												"The argument should be LocationListener or PendingIntent");
									}
								}
								
								return invoke(hooked, args);
							}

						});
			}
		}
	}

}
