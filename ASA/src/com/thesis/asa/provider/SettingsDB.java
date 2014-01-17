package com.thesis.asa.provider;

import java.util.HashMap;
import java.util.Map;
import android.Manifest;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SettingsDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "settings.db";
    public static final int DB_VERSION = 21;
    public static final String TABLE = "status";
    
    // Contacts Settings DB
    public static final String CONTACTS_TABLE = "contacts_settings";
    public static final String COL_PKG_NAME = "pkgName";
    public static final String COL_PROCESSES_NAMES = "processes";
    public static final String COL_GROUPS = "availableGroups";
    private static final String CREATE_CONTACTS_TABLE = "create table "+ CONTACTS_TABLE+" ("+ COL_PKG_NAME
    + " text primary key, " + COL_PROCESSES_NAMES + " text not null, " + COL_GROUPS + " text not null);";
    
    // Device data DB
    public static final String DEVICE_DATA_TABLE = "device_data_settings";
    public static final String COL_ANDROID_ID = "getString";
    public static final String COL_TELEPHONY_ID = "getDeviceId";
    public static final String COL_SUBSCRIBER_ID = "getSubscriberId";
    public static final String COL_SIM_NUMBER = "getSimSerialNumber";
    public static final String COL_LINE_NUMBER = "getLine1Number";
    private static final String CREATE_DEVICE_DATA_TABLE = "create table "+ DEVICE_DATA_TABLE+" ("+ COL_PKG_NAME
    	    + " text primary key, " + COL_PROCESSES_NAMES + " text not null, " + COL_ANDROID_ID + " text, " + COL_TELEPHONY_ID + " text, "+ 
    	    COL_SUBSCRIBER_ID + " text, "+ COL_SIM_NUMBER + " text, "+ COL_LINE_NUMBER + " text);";
    public static final String[] DEVICE_DATA_TABLE_COLUMNS = {COL_ANDROID_ID, COL_TELEPHONY_ID, COL_SUBSCRIBER_ID, COL_SIM_NUMBER, COL_LINE_NUMBER};
    
    // Wifi Info Settings DB
    public static final String WIFI_TABLE = "wifi_settings";
    public static final String COL_BSSID = "getBSSID";
    public static final String COL_IPADDRESS = "getIpAddress";
    public static final String COL_MACADDRESS = "getMacAddress";
    public static final String COL_SSID = "getSSID";
    public static final String COL_CONFIGURATIONS = "getConfiguredNetworks";
    public static final String COL_SCANS = "getScanResults";
    private static final String CREATE_WIFI_TABLE = "create table "+ WIFI_TABLE+" ("+ COL_PKG_NAME
    + " text primary key, " + COL_PROCESSES_NAMES + " text not null, " + COL_SSID + " text, "+ 
    COL_BSSID + " text, "+ COL_IPADDRESS + " text, "+ COL_MACADDRESS + " text, "+ COL_CONFIGURATIONS + " text, "+ COL_SCANS +" text )";
   
    // Wifi Saved State DB
    public static final String WIFI_STATES_TABLE = "saved_wifi_settings";
    private static final String CREATE_WIFI_STATES_TABLE = "create table "+ WIFI_STATES_TABLE +" ("+ COL_SSID + " text, "+ 
    		   COL_BSSID + " text, "+ COL_IPADDRESS + " text, "+ COL_MACADDRESS + " text, "+ COL_CONFIGURATIONS + " text, " + COL_SCANS +" text, "+ 
    	  "PRIMARY KEY ("+COL_SSID+", "+COL_BSSID+", "+COL_IPADDRESS+", "+COL_MACADDRESS+", "+COL_CONFIGURATIONS+", "+COL_SCANS+"));";

    public static final String[] WIFI_TABLE_COLUMNS = {COL_SSID, COL_BSSID, COL_IPADDRESS, COL_MACADDRESS, COL_CONFIGURATIONS, COL_SCANS};
    public static final String[] WIFI_STATES_TABLE_COLUMNS = {COL_SSID, COL_BSSID, COL_IPADDRESS, COL_MACADDRESS, COL_SCANS};
    
    public static final String LOCATION_TABLE = "location_settings";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_CELL_LOCATION = "getCellLocation";
    public static final String COL_NEIGHBORING_CELL_INFO  = "getNeighboringCellInfo";   
    private static final String CREATE_LOCATION_TABLE = "create table "+ LOCATION_TABLE+" ("+ COL_PKG_NAME
    	    + " text primary key, " + COL_PROCESSES_NAMES + " text not null, " + COL_LATITUDE + " text, "+ 
    	    COL_LONGITUDE + " text, "+ COL_CELL_LOCATION + " text, "+ COL_NEIGHBORING_CELL_INFO + " text not null )";
    	   
    public static final String[] LOCATION_TABLE_COLUMNS = {COL_LATITUDE, COL_LONGITUDE, COL_CELL_LOCATION, COL_NEIGHBORING_CELL_INFO };
    
    public static final String LOCATION_STATES_TABLE = "saved_location_settings";
    private static final String CREATE_LOCATION_STATES_TABLE = "create table "+ LOCATION_STATES_TABLE +" (" + COL_LATITUDE + " text, "+ 
    	    COL_LONGITUDE + " text, "+ COL_CELL_LOCATION + " text, "+ COL_NEIGHBORING_CELL_INFO + " text not null )";
	       
    
       
    // Default Settings DB
    public static final String DEFAULT_TABLE = "default_settings";
    public static final String COL_PERMISSION = "permission";
    public static final String COL_SYSTEM = "isSystem";
    public static final String COL_CONFIGURATION = "configuration";
    private static final String CREATE_DEFAULT_TABLE = "create table "+ DEFAULT_TABLE+" ("+ COL_PERMISSION
    	    + " text not null, " + COL_SYSTEM + " integer not null, " + COL_CONFIGURATION + " text not null, " +
    	    		"PRIMARY KEY ("+COL_PERMISSION+", "+COL_SYSTEM+"));";
    	    
	private static final String DEBUG_TAG = "SettingsProvider";
    
    public SettingsDB(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(CREATE_CONTACTS_TABLE);
    	db.execSQL(CREATE_DEVICE_DATA_TABLE);
    	db.execSQL(CREATE_WIFI_TABLE);
    	db.execSQL(CREATE_DEFAULT_TABLE);
    	db.execSQL(CREATE_WIFI_STATES_TABLE);
    	db.execSQL(CREATE_LOCATION_TABLE);
    	db.execSQL(CREATE_LOCATION_STATES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WIFI_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DEFAULT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WIFI_STATES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
    	db.execSQL("DROP TABLE IF EXISTS " + LOCATION_STATES_TABLE);
        onCreate(db);
    }
    
	private static Map<String, String> tableByPermission;
	
	static {
		tableByPermission = new HashMap<String, String>();
		tableByPermission.put(Manifest.permission.READ_CONTACTS, CONTACTS_TABLE);
		tableByPermission.put(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_TABLE); 
		tableByPermission.put(Manifest.permission.ACCESS_WIFI_STATE, WIFI_TABLE);
		tableByPermission.put(Manifest.permission.READ_PHONE_STATE, DEVICE_DATA_TABLE);
		tableByPermission.put(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_TABLE);
		tableByPermission.put(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_TABLE);
    }
	
	public static String getTableNameForPermission(String permission) {
		return tableByPermission.get(permission);
	}
}