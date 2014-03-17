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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.webkit.WebStorage.Origin;

import com.saurik.substrate.MS;
import com.thesis.asa.Utilities;
import com.thesis.asa.devicedata.DeviceDataSettings;
import com.thesis.asa.location.LocationSettings;
import com.thesis.asa.provider.SettingsDB;

public class DeviceDataHook extends Hook {
	protected static final String[] columns = SettingsDB.DEVICE_DATA_TABLE_COLUMNS;

	private static FakePhoneStateListener fakeListener;

	private static final String[] cdmaKeys = { "baseStationId",
			"baseStationLatitude", "baseStationLongitude", "systemId",
			"networkId" };
	private static final String[] gsmKeys = { "lac", "cid", "psc" };

	public static void hookAndroidId() {

		MS.hookClassLoad("com.android.providers.settings.SettingsProvider",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> _clazz) {
						Method method;
						try {
							Class[] args = { String.class, String.class,
									Bundle.class };
							method = _clazz.getMethod("call", args);
						} catch (NoSuchMethodException e) {
							Log.d(Utilities.ERROR, Log.getStackTraceString(e));
							method = null;
						}

						if (method != null) {
							MS.hookMethod(_clazz, method,
									new MS.MethodAlteration<Object, Bundle>() {

										@Override
										public Bundle invoked(Object hooked,
												Object... args)
												throws Throwable {

											Context context = ((ContentProvider) hooked)
													.getContext();

											Bundle result = invoke(hooked, args);

											if (result != null
													&& args[1]
															.toString()
															.equals(Secure.ANDROID_ID)) {

												String original = result.getString("value");
												try {
														result.putString("value", getResultByASAConfiguration(
																			"getString",
																			result.getString("value"),
																			hooked,
																			context));
												} catch (Exception e) {
														Log.d("ERROR","There was an error with getting Android ID from ASA, falling back to random");
														result.putString("value", randomNumberWithLength(original.length()));
												}
											}

											return result;
										}
									});
						}
					}
				});
	}

	private static String fakeNumberFrom(String seed) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(seed.getBytes("UTF-8"));
		} catch (Exception e) {
			Log.d(Utilities.ERROR, "An error occurred generating a fake number");
		}
		byte[] hash = digest.digest();

		char[] fakeChars = new char[seed.length()];
		for (int i = 0; i < fakeChars.length; i++) {
			int n = Math.abs(hash[i]);
			fakeChars[i] = Character.forDigit(n % 10, 10);
		}

		return new String(fakeChars);
	}

	private static String randomNumberWithLength(int n) {
		Random rand = new Random();
		Long r = Math.abs(rand.nextLong());
		String result = String.format("%0" + n + "d", r);

		return fakeNumberFrom(result.substring(0, n));
	}

	public static void hook() {
		hookAndroidId();

		MS.hookClassLoad("android.telephony.TelephonyManager",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> telephonyManager) {
						try {
							new PullTasksThread().start();
						} catch (Exception e) {
							Log.d(Utilities.ERROR, Log.getStackTraceString(e));
						}

						hookCellInfoMethod("getNeighboringCellInfo",
								telephonyManager);
						hookGetCellLocation(telephonyManager);
						hookListen(telephonyManager);
					}
				});

		MS.hookClassLoad("com.android.internal.telephony.gsm.GSMPhone",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> phone) {
						hookMethod("getDeviceId", phone);
						hookMethod("getSubscriberId", phone);
						hookMethod("getImei", phone);
						hookMethod("getLine1Number", phone);
					}
				});

		MS.hookClassLoad("com.android.internal.telephony.cdma.CDMAPhone",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> phone) {
						hookMethod("getDeviceId", phone);
						hookMethod("getMeid", phone);
						hookMethod("getSubscriberId", phone);
						hookMethod("getLine1Number", phone);
					}
				});
		MS.hookClassLoad("com.android.internal.telephony.PhoneBase",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> phone) {
						hookMethod("getIccSerialNumber", phone);
					}
				});
	}

	protected static void hookListen(Class<?> telephonyManager) {
		Method method;
		try {
			Class<?>[] params = new Class[2];
			params[0] = PhoneStateListener.class;
			params[1] = Integer.TYPE;

			method = telephonyManager.getMethod("listen", params);
		} catch (NoSuchMethodException e) {
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
			method = null;
		}

		if (method != null) {
			MS.hookMethod(telephonyManager, method,
					new MS.MethodAlteration<TelephonyManager, Void>() {

						public Void invoked(TelephonyManager hooked,
								Object... args) throws Throwable {

							int events = (Integer) args[1];
							if ((events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
								args[1] = events
										& ~PhoneStateListener.LISTEN_CELL_LOCATION;
								invoke(hooked, args);

								CellLocation location = hooked
										.getCellLocation();

								FakePhoneStateListener listener = PullTasksThread
										.getListener();

								if (listener != null) {
									listener.setListener((PhoneStateListener) args[0]);
									listener.setcellLocation(location);
									args[1] = PhoneStateListener.LISTEN_CELL_LOCATION;
									args[0] = listener;
								}
							}

							return invoke(hooked, args);
						}
					});
		}

	}

	protected static void hookGetCellLocation(Class<?> _class) {
		Method method;
		try {
			Class<?>[] params = new Class[0];
			method = _class.getMethod("getCellLocation", params);
		} catch (NoSuchMethodException e) {
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
			method = null;
		}

		if (method != null) {
			MS.hookMethod(_class, method,
					new MS.MethodAlteration<Object, CellLocation>() {

						public CellLocation invoked(Object hooked,
								Object... args) throws Throwable {

							CellLocation result = invoke(hooked, args);

							Context context = getContext(hooked);

							Object[] properties = Hook
									.queryConfigurationFromASA(context,
											new LocationSettings(context));

							if (((String) properties[0]).trim().equals("Real")) {
								return result;
							}

							if (((String) properties[2]).trim().equals("null")) {
								return null;
							}

							Bundle bundle;
							String[] data = Utilities
									.stringToArray((String) properties[2]);
							String type = ((String) data[0]).trim();
							if (type.equals(String
									.valueOf(TelephonyManager.PHONE_TYPE_NONE))) {
								return null;
							} else if (type.equals(String
									.valueOf(TelephonyManager.PHONE_TYPE_CDMA))) {
								bundle = new Bundle();
								data = Utilities
										.stringToArray((String) properties[2]);
								for (int i = 1; i < data.length; i++) {
									bundle.putInt(cdmaKeys[i - 1],
											Integer.parseInt(data[i].trim()));
								}

								return new CdmaCellLocation(bundle);
							} else if (type.equals(String
									.valueOf(TelephonyManager.PHONE_TYPE_GSM))) {
								bundle = new Bundle();
								data = Utilities
										.stringToArray((String) properties[2]);

								for (int i = 1; i < data.length; i++) {
									bundle.putInt(gsmKeys[i - 1],
											Integer.parseInt(data[i].trim()));
								}

								return new GsmCellLocation(bundle);
							}

							Log.d(Utilities.ERROR, "No phone type!");
							return null;
						}
					});
		}

	}

	protected static void hookCellInfoMethod(final String methodName,
			Class<?> telephonyManager) {
		Method method;
		try {
			Class<?>[] params = new Class[0];
			method = telephonyManager.getMethod(methodName, params);
		} catch (NoSuchMethodException e) {
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
			method = null;
		}

		if (method != null) {
			MS.hookMethod(telephonyManager, method,
					new MS.MethodAlteration<TelephonyManager, List<Object>>() {

						public List<Object> invoked(TelephonyManager hooked,
								Object... args) throws Throwable {
							List<Object> result = invoke(hooked, args);

							Context context = getContext(hooked);

							Object[] properties = Hook
									.queryConfigurationFromASA(context,
											new LocationSettings(context));

							if (properties[0].equals("Real")) {
								return result;
							}

							return new ArrayList<Object>();
						}
					});
		}
	}

	private static String getResultByASAConfiguration(final String methodName,
			String result, Object hooked, Context context) throws Throwable {

		if (result == null)
			return result;
		String name = translateIntoColumnName(methodName);

		Object[] properties = Hook.queryConfigurationFromASA(context,
				new DeviceDataSettings(context));

		int index = 0;
		for (Object property : properties) {
			String column = columns[index];
			index++;
			if (column.equals(name)) {
				String value = ((String) property).trim();
				if (value.equals("Random"))
					result = randomNumberWithLength(result.length());
				else {
					if (value.endsWith("Fake")) {
						result = fakeNumberFrom(result);
					} else {
						if (value.equals("Real")) {
							// Nothing...
						} else {
							result = value;
						}
					}
				}
			}
		}

		return result;

	}

	protected static void hookMethod(final String methodName, Class<?> _class) {

		Method method;
		try {
			Class<?>[] params = new Class[0];
			method = _class.getMethod(methodName, params);
		} catch (NoSuchMethodException e) {
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
			method = null;
		}

		if (method != null) {
			MS.hookMethod(_class, method,
					new MS.MethodAlteration<Object, String>() {

						public String invoked(Object hooked, Object... args)
								throws Throwable {

							String result = invoke(hooked, args);

							Context context = getContext(hooked);

							return getResultByASAConfiguration(methodName,
									result, hooked, context);

						}
					});
		}
	}

	private static String translateIntoColumnName(String methodName) {
		String name = methodName;
		if (methodName == "getIccSerialNumber")
			name = "getSimSerialNumber";
		if (methodName == "getImei")
			name = "getDeviceId";
		if (methodName == "getMeid")
			name = "getDeviceId";

		return name;
	}

	protected static Context getContext(Object owner) {
		/* WATCH OUT, IT USES A PRIVATE VARIABLE */
		Method method = null;
		Context context = null;
		try {
			if (owner.getClass().getName().contains("TelephonyManager"))
				return Hook.context;
			else if (owner.getClass().getName().contains("PhoneBase"))
				method = owner.getClass().getMethod("getContext");
			else
				method = owner.getClass().getSuperclass()
						.getMethod("getContext");

			return (Context) method.invoke(owner);
		} catch (Exception e) {
			String error = Log.getStackTraceString(e);
			Log.d(Utilities.ERROR, "Error: " + error);
		}
		return null;
	}

	public static class PullTasksThread extends Thread {
		public void run() {
			fakeListener = new FakePhoneStateListener();
		}

		public static FakePhoneStateListener getListener() {
			if (fakeListener == null)
				fakeListener = new FakePhoneStateListener();
			return fakeListener;
		}
	}
}
