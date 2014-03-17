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
 

package com.thesis.asa.location;

public class LocationInfoItem {

	private String latitude;
	private String longitude;
	private String cellLocation;
	private String neighboringCellInfo;
	
	public LocationInfoItem(double lat, double lon) {
		latitude = String.valueOf(lat);
		longitude = String.valueOf(lon);
		cellLocation = "null";
		neighboringCellInfo = "null";
	}
	
	public LocationInfoItem(Object[] parameters) {
		latitude = (String) parameters[0];
		longitude = (String) parameters[1];
		cellLocation = (String) parameters[2];
		neighboringCellInfo = (String) parameters[3];
	}
	
	public String[] getLocationInfo() {
		String[] info = new String[4];
		info[0] = latitude; 
		info[1] = longitude;
		info[2] = cellLocation;
		info[3] = neighboringCellInfo;
		return info;

	}

	public String getCellLocation() {
		return cellLocation;
	}
	
	public String getLabel() {
		return "* Latitude: "+latitude+", Longitude: "+longitude;
	}

	public Double getLatitude() {
		return Double.parseDouble(latitude);
	}

	public Double getLongitude() {
		return Double.parseDouble(longitude);
	}
}
