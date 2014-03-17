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
 
package com.thesis.asa;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.util.Log;
import android.util.SparseArray;

public class Data {
	private static SparseArray<List<String>> permissions;
	
	public enum SecurityMode {
		PERMISSIVE, SECURE, PARANOID;

		public static int toInteger(SecurityMode mode) {
			switch(mode) {
	        case PERMISSIVE:
	            return 0;
	        case SECURE:
	            return 1;
	        case PARANOID:
	            return 2;
	        }
			Log.d(Utilities.ERROR, "No int found for security mode: "+mode);
	        return -1;
		}
		
		public static SecurityMode fromInteger(int mode) {
			switch(mode) {
	        case 0:
	            return PERMISSIVE;
	        case 1:
	            return SECURE;
	        case 2:
	            return PARANOID;
	        }
			
			Log.d(Utilities.ERROR, "No security mode found for int: "+mode);
	        return PARANOID;
		}

	}
	
    static
    {
    	permissions = new SparseArray< List<String> >();
    	permissions.put(0, Arrays.asList(Manifest.permission.READ_CONTACTS));
    	permissions.put(1, Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION));
    	permissions.put(2, Arrays.asList(Manifest.permission.READ_PHONE_STATE));
    	permissions.put(3, Arrays.asList(Manifest.permission.ACCESS_WIFI_STATE));
    	permissions.put(4, Arrays.asList(Manifest.permission.INTERNET));
    }
    
    public static List<String> permissionsForPage(Integer page) {
    	return permissions.get(page);
    }
	
    private static SparseArray<String> titles;
	static
    {
		titles = new SparseArray<String>();
		titles.put(0, "Contacts");
		titles.put(1, "Location");
		titles.put(2, "Device data");
		titles.put(3, "Wifi info");
		titles.put(4, "Internet access");
    }
	
	public static String titleForPage(Integer page) {
		return titles.get(page);
	}
	
	private static Map<Integer, String> resourceClasses;
	
	static
    {
		resourceClasses = new HashMap<Integer, String>();
		resourceClasses.put(0, "com.thesis.asa.contacts.ContactsSettings");
		resourceClasses.put(1, "com.thesis.asa.location.LocationSettings");
		resourceClasses.put(2, "com.thesis.asa.devicedata.DeviceDataSettings");
		resourceClasses.put(3, "com.thesis.asa.wifi.WifiSettings"); 
		resourceClasses.put(4, "com.thesis.asa.internet.InternetSettings");
    }
	
	public static String getResourceFor(Integer page) {
		return resourceClasses.get(page);
	}
	
	public static Collection<String> resources() {
		return resourceClasses.values();
	}
}


