package com.thesis.asa.hook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.net.wifi.ScanResult;
import com.saurik.substrate.MS;
import com.thesis.asa.Utilities;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.wifi.WifiSettings;

public class WifiHook extends Hook {
	protected static final String[] columns = SettingsDB.WIFI_TABLE_COLUMNS;

	// protected static Context currentContext = null;

	protected static void hookUtilsGetWifiIpAddresses() {
		MS.hookClassLoad("com.android.settings.Utils", new MS.ClassLoadHook() {
			public void classLoaded(Class<?> utils) {
				Method method;
				try {
					Class<?>[] params = new Class[1];
					params[0] = Context.class;
					method = utils.getMethod("getWifiIpAddresses", params);
				} catch (NoSuchMethodException e) {
					Log.d(Utilities.ERROR, Log.getStackTraceString(e));
					method = null;
				}

				if (method != null) {
					MS.hookMethod(utils, method,
							new MS.MethodAlteration<Object, String>() {
								public String invoked(final Object hooked,
										final Object... args) throws Throwable {
									Integer ip = 0;
									Context context = (Context) args[0];
									String result = invoke(hooked, args);

									if (result == null)
										return null;

									final Object[] properties = Hook
											.queryConfigurationFromASA(context,
													new WifiSettings(context));
									String value = ((String) properties[2])
											.trim();
									ip = (Integer) getHookedResultForIP(value,
											getRealIPFrom(result));

									return Utilities.getFormatedIpFromIp(ip);
								}
							});

				}
			}
		});
	}

	protected static void hookUtilsGetDefaultWifiIpAddresses() {
		MS.hookClassLoad("com.android.settings.Utils", new MS.ClassLoadHook() {
			public void classLoaded(Class<?> utils) {
				Method method;
				try {
					Class<?>[] params = new Class[1];
					params[0] = Context.class;
					method = utils.getMethod("getDefaultIpAddresses", params);
				} catch (NoSuchMethodException e) {
					Log.d(Utilities.ERROR, Log.getStackTraceString(e));
					method = null;
				}

				if (method != null) {
					MS.hookMethod(utils, method,
							new MS.MethodAlteration<Object, String>() {
								public String invoked(final Object hooked,
										final Object... args) throws Throwable {
									Integer ip = 0;
									Context context = (Context) args[0];
									String result = invoke(hooked, args);

									if (result == null)
										return null;

									final Object[] properties = Hook
											.queryConfigurationFromASA(context,
													new WifiSettings(context));
									String value = ((String) properties[2])
											.trim();
									ip = (Integer) getHookedResultForIP(value,
											getRealIPFrom(result));

									return Utilities.getFormatedIpFromIp(ip);
								}
							});

				}
			}
		});
	}

	public static void hook() {
		hookUtilsGetWifiIpAddresses();
		hookUtilsGetDefaultWifiIpAddresses();

		MS.hookClassLoad("android.net.wifi.WifiManager",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> wifiManager) {
						hookWifiConfiguredNetworksMethod(wifiManager);
						hookWifiScanResultsMethod(wifiManager);
					}
				});

		MS.hookClassLoad("android.net.wifi.WifiInfo", new MS.ClassLoadHook() {
			public void classLoaded(Class<?> wifiInfo) {
				hookWifiMethod(wifiInfo, "getBSSID");
				hookWifiMethod(wifiInfo, "getIpAddress");
				hookWifiMethod(wifiInfo, "getMacAddress");
				hookWifiMethod(wifiInfo, "getSSID");
			}
		});
	}

	protected static int fakeIP() {
		return getRealIPFrom("192.168.0.2");
	}

	protected static String fakeDataFor(String methodName) {
		if (methodName.equals("getBSSID")) {
			return "aa:aa:aa:aa:aa:aa";
		} else {
			if (methodName.equals("getMacAddress")) {
				return "bb:bb:bb:bb:bb:bb";
			} else {
				if (methodName.equals("getSSID")) {
					return "Wifi";
				} else {
					Log.d(Utilities.ERROR, "No matched column for method "
							+ methodName + "in Wifi resource");
				}
			}
		}

		return "";
	}

	protected static Object getGeneralHookedResult(String methodName,
			String value, String result) {
		if (value.equals("Fake")) {
			return fakeDataFor(methodName);
		} else {
			if (value.equals("Real"))
				return result;
			else {
				return value.replace("\"", "");
			}
		}
	}

	protected static Object getHookedResultForIP(String value, int result) {
		if (value.equals("Fake"))
			return fakeIP();
		else {
			if (value.equals("Real"))
				return result;
			else {
				return getRealIPFrom(value);
			}
		}
	}

	private static int getRealIPFrom(String ipAddress) {
		String[] octets = ipAddress.split("\\.");
		return (Integer.parseInt(octets[3]) << 24)
				+ (Integer.parseInt(octets[2]) << 16)
				+ (Integer.parseInt(octets[1]) << 8)
				+ Integer.parseInt(octets[0]);
	}

	private static void hookWifiConfiguredNetworksMethod(Class<?> clazz) {
		final String methodName = "getConfiguredNetworks";
		Method method;
		try {
			Class<?>[] params = new Class[0];
			method = clazz.getMethod(methodName, params);
		} catch (NoSuchMethodException e) {
			method = null;
			Log.d(Utilities.ERROR, "No such method: getConfiguredNetworks");
		}

		if (method != null) {
			MS.hookMethod(clazz, method,
					new MS.MethodAlteration<WifiManager, List<Object>>() {
						public List<Object> invoked(final WifiManager hooked,
								final Object... args) throws Throwable {
							List<Object> result = invoke(hooked, args);

							if (result == null)
								return null;

							Object[] properties = getProperties(WifiSettings.class
									.getName());

							int index = 0;
							for (Object property : properties) {
								String column = columns[index];
								index++;
								if (column.equals(methodName)) {
									String value = ((String) property).trim();
									if (value.equals("Real"))
										return result;
									else if (value.equals("Fake"))
										return new ArrayList<Object>();
									else {
										// Custom case
										String[] propertyIds = Utilities
												.stringToArray(value);
										List<String> ssids = new ArrayList<String>();
										List<Object> filteredSSIDS = new ArrayList<Object>();

										List<String> originalSSIDS = new ArrayList<String>();

										for (int i = 0; i < propertyIds.length; i++) {
											ssids.add(propertyIds[i].trim());
										}
										for (Object res : result) {
											if (ssids
													.contains(((WifiConfiguration) res).SSID))
												filteredSSIDS.add(res);
											originalSSIDS
													.add(((WifiConfiguration) res).SSID);
										}

										result = filteredSSIDS;

									}
									break;
								}

							}

							return result;
						}
					});
		}
	}

	private static void hookWifiScanResultsMethod(Class<?> clazz) {
		final String methodName =  "getScanResults";
		Method method;
		try {
			Class<?>[] params = new Class[0];
			method = clazz.getMethod(methodName, params);
		} catch (NoSuchMethodException e) {
			method = null;
			Log.d(Utilities.ERROR, "No such method: " + methodName);
		}

		if (method != null) {
			MS.hookMethod(clazz, method,
					new MS.MethodAlteration<WifiManager, List<Object>>() {
						public List<Object> invoked(final WifiManager hooked,
								final Object... args) throws Throwable {
							List<Object> result = invoke(hooked, args);
							
							if (result == null)
								return null;

							Constructor<ScanResult> constructor = null;
							try {
								constructor = ScanResult.class.getConstructor(String.class,String.class,String.class,Integer.TYPE,Integer.TYPE);
							} catch (NoSuchMethodException e1) {
								Log.d(Utilities.ERROR,"Problem getting ScanResult constructor through Java Reflection");
							}

							Object[] properties = getProperties(WifiSettings.class
									.getName());

							int index = 0;
							for (Object property : properties) {
								String column = columns[index];
								index++;
								if (column.equals(methodName)) {
									String value = ((String) property).trim();
									if (value.equals("Real"))
										return result;
									else if (value.equals("Fake"))
										return new ArrayList<Object>();
									else {
										// Custom case
										List<String> propertyIds = Utilities.
												getScannedWifisFrom(value);
										
										List<Object> scannedNetworks = new ArrayList<Object>();

											for (int i = 0; i < propertyIds.size(); i++) {
												 Object[] constructor_args = new Object[5];
												 String[] scan = Utilities
															.stringToArray(propertyIds.get(i));
												 constructor_args[0] = ((String) scan[0]).replace("\"","").trim();
												 constructor_args[1] = scan[1].trim();
												 constructor_args[2] = scan[2].trim();
												 constructor_args[3] = Integer.parseInt(((String) scan[3]).trim()); 
												 constructor_args[4] = Integer.parseInt(((String) scan[4]).trim()); 
										
												 scannedNetworks.add(constructor.newInstance(constructor_args));
											}
											
										result = scannedNetworks;
									
									}
									break;
								}
							}

							return result;
						}
					});
		}
	}

	private static void hookWifiMethod(Class<?> clazz, final String methodName) {

		Method method;
		try {
			Class<?>[] params = new Class[0];
			method = clazz.getMethod(methodName, params);
		} catch (NoSuchMethodException e) {
			method = null;
		}

		if (method != null) {
			MS.hookMethod(clazz, method,
					new MS.MethodAlteration<WifiInfo, Object>() {
						public Object invoked(final WifiInfo hooked,
								final Object... args) throws Throwable {
							Object result = invoke(hooked, args);

							if (result == null)
								return result;

							Object[] properties = getProperties(WifiSettings.class
									.getName());

							int index = 0;
							for (Object property : properties) {
								String column = columns[index];
								index++;
								if (column.equals(methodName)) {
									String value = ((String) property).trim();
									if (methodName.equals("getIpAddress")) {
										result = getHookedResultForIP(value,
												(Integer) result);
									} else {
										result = getGeneralHookedResult(
												methodName, value,
												(String) result);
									}
									break;
								}
							}

							if (result == null || result == "null")
								result = "";
							return result;
						}
					});
		}
	}

}
