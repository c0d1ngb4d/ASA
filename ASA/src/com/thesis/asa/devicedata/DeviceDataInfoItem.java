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
 
 
package com.thesis.asa.devicedata;

import java.util.Arrays;

import android.util.Log;

import com.thesis.asa.Utilities;

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
