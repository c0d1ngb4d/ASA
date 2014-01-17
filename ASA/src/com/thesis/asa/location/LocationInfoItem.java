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
