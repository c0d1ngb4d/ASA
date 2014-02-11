package com.thesis.asa.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.provider.Settings.System;

public class ASASettings extends ContentProvider {
  static final String TAG = "SettingsProvider";
  static final String AUTHORITY = "com.thesis.asa.settings";
  static final String DEFAULT_PATH = "default_settings";
  static final String CONTACTS_PATH = "contacts_settings"; 
  static final String WIFI_PATH = "wifi_settings";
  static final String LOCATION_PATH = "location_settings"; 
  static final String DEVICE_DATA_PATH = "device_data_settings";
  public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);
  static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.thesis.asa.status";
  static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.com.thesis.asa.status";
  private static final UriMatcher URI_MATCHER;
  private static final int DEFAULT_CODE = 0;
  private static final int CONTACTS_CODE = 1;
  private static final int LOCATION_CODE = 2; 
  private static final int DEVICE_DATA_CODE = 3;
  private static final int WIFI_CODE = 4;

	//prepare the UriMatcher
	static {
	  URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	  URI_MATCHER.addURI(AUTHORITY, DEFAULT_PATH, DEFAULT_CODE);
	  URI_MATCHER.addURI(AUTHORITY, CONTACTS_PATH, CONTACTS_CODE);
	  URI_MATCHER.addURI(AUTHORITY, LOCATION_PATH, LOCATION_CODE);
	  URI_MATCHER.addURI(AUTHORITY, DEVICE_DATA_PATH, DEVICE_DATA_CODE);
	  URI_MATCHER.addURI(AUTHORITY, WIFI_PATH, WIFI_CODE);
	}

  private SettingsDB contactsDB;

  @Override
  public boolean onCreate() {
    Log.d(TAG, "onCreate: "+CONTENT_URI.toString());
    contactsDB = new SettingsDB(getContext());
    return true;
  }

  @Override
  public String getType(Uri uri) {
    String ret = getContext().getContentResolver().getType(System.CONTENT_URI);
    Log.d(TAG, "getType returning: " + ret);
    return ret;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
	  Log.e(TAG, "Insert not supported for this content provider");
	  return null;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {
	  Log.e(TAG, "Update not supported for this content provider");
	  return 0;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    Log.e(TAG, "Delete not supported for this content provider");
    return 0;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
          String[] selectionArgs, String sortOrder) {
      SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
      int uriType = URI_MATCHER.match(uri);
      switch (uriType) {
      case DEFAULT_CODE:
    	  queryBuilder.setTables(SettingsDB.DEFAULT_TABLE);
    	  break;
      case CONTACTS_CODE:
    	  queryBuilder.setTables(SettingsDB.CONTACTS_TABLE);
          break;
      case LOCATION_CODE:
    	  queryBuilder.setTables(SettingsDB.LOCATION_TABLE);
          break;
      case DEVICE_DATA_CODE:
    	  queryBuilder.setTables(SettingsDB.DEVICE_DATA_TABLE);
          break;
      case WIFI_CODE:
    	  queryBuilder.setTables(SettingsDB.WIFI_TABLE);
          break;    
      default:
          throw new IllegalArgumentException("Unknown URI");
      }
      Cursor cursor = queryBuilder.query(contactsDB.getReadableDatabase(),
              projection, selection, selectionArgs, null, null, sortOrder);
      cursor.setNotificationUri(getContext().getContentResolver(), uri);
      return cursor;
  }

}