package com.thesis.asa.devicedata;

public class DeviceDataInfoItem {

	private String androidId;
	private String deviceId;
	private String subscriberId;
	private String simSerialNumber;
	private String line1Number;

	public DeviceDataInfoItem(Object[] parameters) {
		androidId = (String) parameters[0];
		deviceId = (String) parameters[1];
		subscriberId = (String) parameters[2];
		simSerialNumber = (String) parameters[3];
		line1Number = (String) parameters[4];
	}

	public DeviceDataInfoItem(String android, String id, String subscriber,
			String sim, String lineNumber) {
		androidId = android;
		deviceId = id;
		subscriberId = subscriber;
		simSerialNumber = sim;
		line1Number = lineNumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String[] getDeviceDataInfo() {
		String[] info = new String[4];
		info[0] = deviceId;
		info[1] = subscriberId;
		info[2] = simSerialNumber;
		info[3] = line1Number;
		return info;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public String getSimSerialNumber() {
		return simSerialNumber;
	}
	
	public String getLine1Number() {
		return line1Number;
	}

	public String getAndroidId() {
		return androidId;
	}
}
