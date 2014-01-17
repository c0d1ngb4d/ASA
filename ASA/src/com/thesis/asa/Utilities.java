package com.thesis.asa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

public class Utilities {

	public static String ERROR = "ERROR";
	public static String DEBUG = "DEBUG";
	public static String RESULT = "RESULT";
	public static String PROVIDER = "asaLocationProvider";

	public static final Double fakeLatitude = -1.0;
	public static final Double fakeLongitude = -1.0;

	public static final String FIRST_RUN_PREFERENCE = "firstRunPreference";
	public static final String SELECTED_MODE = "selectedMode";

	public static void loadDefaultConfigurationForMode(Context context,
			final SecurityMode mode) {
		for (String name : Data.resources()) {
			try {
				Resource resource = (Resource) Class.forName(name)
						.getDeclaredConstructor(Context.class)
						.newInstance(context);
				resource.insertDefaultConfigurationForMode(mode);
			} catch (Exception e) {
				Log.d(Utilities.ERROR,
						"WIZARD ERRROR AT LOADING RESOURCE CLASS: " + name
								+ ", MODE: " + mode);
				Log.d(Utilities.ERROR, Log.getStackTraceString(e));
			}
		}
	}

	public static boolean areCloseEnough(double d1, double d2) {
		return Math.abs(d1 - d2) < 1e-6;
	}

	public static String[] getProcesses(Context context, String pkgName)
			throws NameNotFoundException {
		Set<String> processes = new HashSet<String>();
		PackageManager manager = context.getPackageManager();

		PackageInfo info = manager.getPackageInfo(pkgName,
				manager.GET_ACTIVITIES | manager.GET_SERVICES
						| manager.GET_PROVIDERS);
		String processName = info.applicationInfo.processName;
		processes.add(processName);
		ActivityInfo[] activities = info.activities;
		ServiceInfo[] services = info.services;
		ProviderInfo[] providers = info.providers;
		if (activities != null) {
			for (ActivityInfo activity : activities)
				processes.add(activity.processName);
		}
		if (services != null) {
			for (ServiceInfo service : services)
				processes.add(service.processName);
		}
		if (providers != null) {
			for (ProviderInfo provider : providers)
				processes.add(provider.processName);
		}

		return processes.toArray(new String[0]);
	}

	public static void killApp(Context context, String pkgName,
			String[] processes) {
		try {
			Runtime runtime = Runtime.getRuntime();
			for (String process : processes) {
				String[] line = new String[] { "su", "-c", "killall " + process };
				runtime.exec(line);
			}
		} catch (Throwable e) {
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
		}
	}

	public static String[] stringToArray(String list) {
		String[] array = new String[0];

		int size = list.length();
		if (size > 2) {
			list = list.substring(1, size - 1);
			list = list.trim();
			array = list.split(",");
		}

		return array;
	}

	protected static void killProcess(String processName) throws Throwable {
		String[] line = new String[] { "su", "-c", "killall " + processName };
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("su");
		runtime.exec(line);
	}

	public static void loadASAConfiguration(Context context) {
		String[] processes = new String[] { context.getApplicationInfo().processName };
		for (String name : Data.resources()) {
			try {
				Resource resource = (Resource) Class.forName(name)
						.getDeclaredConstructor(Context.class)
						.newInstance(context);
				resource.loadASAConfiguration(context.getPackageName(),
						processes);
			} catch (Exception e) {
				Log.d(Utilities.ERROR,
						"WIZARD ERRROR AT LOADING RESOURCE CLASS: " + name);
				Log.d(Utilities.ERROR, Log.getStackTraceString(e));
			}
		}
	}

	public static boolean hasDefaultConfiguration(List<String> permissions,
			Context context, ApplicationInfo app) {
		boolean hasDefault = false;
		SettingsDB helper = new SettingsDB(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		String table = SettingsDB.getTableNameForPermission(permissions.get(0));
		Cursor configuration = db.query(table, null, SettingsDB.COL_PKG_NAME
				+ "=?", new String[] { app.packageName }, null, null, null);

		hasDefault = configuration == null || configuration.getCount() == 0;

		if (configuration != null) {
			configuration.close();
		}

		db.close();

		return hasDefault;
	}

	public static int[] processLine(String[] strings) {
		int[] intarray = new int[strings.length];
		int i = 0;
		for (String str : strings) {
			intarray[i] = Integer.parseInt(str.trim());
			i++;
		}
		return intarray;
	}

	public static boolean isSystem(Context context, int uid) {
		boolean system;
		try {
			PackageManager manager = context.getPackageManager();
			system = false;
			String[] packages = manager.getPackagesForUid(uid);

			for (String pack : packages) {
				system = system
						|| (manager.getPackageInfo(pack, 0).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
			}

		} catch (Exception e) {
			system = false;
		}

		return system;
	}

	public static String getProcessNameByPid(int pid) {

		BufferedReader bufferReader;
		try {
			bufferReader = new BufferedReader(new FileReader(
					("/proc/" + pid + "/cmdline")));
			String processName = bufferReader.readLine();
			if (processName.length() > 0) {
				Character c = processName.charAt(processName.length() - 1);
				int end = processName.indexOf(c);
				processName = processName.substring(0, end);
			}

			bufferReader.close();
			return processName;

		} catch (Exception e) {
			Log.d(Utilities.ERROR, "There was an error getting the pid");
		}

		return null;

	}

	public static List<ApplicationInfo> getThirdPartyApplicationsByPermission(
			Context c, List<String> permissions) {
		List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
		PackageManager manager = c.getPackageManager();
		List<PackageInfo> packages = c.getPackageManager()
				.getInstalledPackages(0);

		for (PackageInfo pack : packages) {
			boolean granted = false;
			for (String permission : permissions) {
				granted = granted
						|| (!isSystem(c, pack.applicationInfo.uid)
								&& !pack.packageName.equals(c.getPackageName()) && PackageManager.PERMISSION_GRANTED == manager
								.checkPermission(permission, pack.packageName));
			}
			if (granted)
				applications.add(pack.applicationInfo);
		}

		return applications;
	}

	public static List<ApplicationInfo> getSystemApplicationsByPermission(
			Context c, List<String> permissions) {
		List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
		PackageManager manager = c.getPackageManager();
		List<PackageInfo> packages = c.getPackageManager()
				.getInstalledPackages(0);
		for (PackageInfo pack : packages) {
			boolean granted = false;
			for (String permission : permissions) {
				granted = granted
						|| (isSystem(c, pack.applicationInfo.uid)
								&& !pack.packageName.equals(c.getPackageName()) && PackageManager.PERMISSION_GRANTED == manager
								.checkPermission(permission, pack.packageName));
			}
			if (granted)
				applications.add(pack.applicationInfo);
		}

		return applications;
	}

	public static String getFormatedIpFromIp(Integer ipAddress) {
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
	}

	public static class InputFilterMinMax implements InputFilter {

		private int min, max;

		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (dest.length() > 0 && dest.charAt(0) == '0')
				return "";

			try {
				int input = Integer.parseInt(dest.toString()
						+ source.toString());
				if (isInRange(min, max, input))
					return null;
			} catch (NumberFormatException nfe) {
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	public static class HexFilter implements InputFilter {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				Character c = source.charAt(i);
				boolean isHex = ((c >= '0' && c <= '9')
						|| (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'));
				if (!isHex) {
					return "";
				}
			}

			return null;
		}
	}

	public static class SSIDFilter implements InputFilter {
		private static String validSymbols = "_-.,!�?�=)(/&%$#*+";

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			for (int i = start; i < end; i++) {
				Character c = source.charAt(i);
				boolean acceptable = Character.isWhitespace(c)
						|| Character.isLetterOrDigit(c)
						|| validSymbols.contains(c.toString());
				if (!acceptable) {
					return "";
				}
			}
			return null;
		}

	}

	public static List<String> getScannedWifisFrom(String configuration) {
		configuration = (String) configuration.subSequence(1, configuration.length()-1);
		List<String> scanned = new ArrayList<String>();		
		int start = 0, ch = 0;
		int countBracket = 0;
		
		while(ch < configuration.length()){
			while(ch != configuration.length() && configuration.charAt(ch) != '[') start = ch++;

			countBracket = 1; start = ch++;
			
			while(ch != configuration.length() && countBracket > 0){
				char c = configuration.charAt(ch);
				if(c == ']') countBracket--;
				if(c == '[') countBracket++;
				ch++;			
			}
			
			if(ch == configuration.length()) break;

			scanned.add(configuration.substring(start, ch));		
			while(ch != configuration.length() && configuration.charAt(ch) != ',') {
				ch++;
			}
			start = ch; //advance comma value			
		}

		return scanned;


	}
	
	public static List<String> getScannedWifis(WifiManager wifiManager) {
		List<ScanResult> scanned = wifiManager.getScanResults();
		List<String> scannedSsids = new ArrayList<String>();
		if (scanned != null) {
			for (ScanResult result : scanned)
				scannedSsids.add( "[ \"" + result.SSID + "\", " + result.BSSID
						+ ", " + result.capabilities+ ", "+ result.level  + ", " + result.frequency+ "]");
		}
		return scannedSsids;
	}

	public static String formatMac(String mac) {
		return mac.substring(0, 2) + ":" + mac.substring(2, 4) + ":"
				+ mac.substring(4, 6) + ":" + mac.substring(6, 8) + ":"
				+ mac.substring(8, 10) + ":" + mac.substring(10, 12);
	}

}
