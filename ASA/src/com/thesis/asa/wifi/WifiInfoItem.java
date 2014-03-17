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
 
 
package com.thesis.asa.wifi;

import java.util.Arrays;
import java.util.List;

import com.thesis.asa.Utilities;

public class WifiInfoItem {

	private String SSID;
	private String BSSID;
	private String ipAddress;
	private String macAddress;
	private String configuredSsids;
	private String scannedSsids;

	public WifiInfoItem(String s, String b, String ip, String mac,
			List<String> configured, List<String> scanned) {
		SSID = "\"" + s + "\"";
		BSSID = b;
		ipAddress = ip;
		macAddress = mac;
		configuredSsids = Arrays.toString(configured.toArray(new String[0]));
		scannedSsids = Arrays.toString(scanned.toArray(new String[0]));
	}

	public WifiInfoItem(String s, String b, String ip, String mac,
			String configured, String scanned) {
		SSID = "\"" + s + "\"";
		BSSID = b;
		ipAddress = ip;
		macAddress = mac;
		configuredSsids = configured;
		scannedSsids = scanned;
	}

	public void setConfiguredNetworks(List<String> configured) {
		configuredSsids = Arrays.toString(configured.toArray(new String[0]));
	}

	public WifiInfoItem(Object[] parameters) {
		SSID = (String) parameters[0];
		BSSID = (String) parameters[1];
		ipAddress = (String) parameters[2];
		macAddress = (String) parameters[3];
		configuredSsids = (String) parameters[4];
		scannedSsids = (String) parameters[5];
	}

	public String[] getWifiInfo() {
		String[] info = new String[6];
		info[0] = SSID;
		info[1] = BSSID;
		info[2] = ipAddress;
		info[3] = macAddress;
		info[4] = configuredSsids;
		info[5] = scannedSsids;
		return info;
	}

	public String getBssid() {
		return BSSID;
	}

	public List<String> listConfiguredNetworks() {
		String[] configuration = Utilities.stringToArray(configuredSsids);
		for (int i = 0; i < configuration.length; i++)
			configuration[i] = configuration[i].trim();
		return Arrays.asList(configuration);
	}

	public String getConfiguredNetworks() {
		return configuredSsids;
	}

	public String getLabel() {
		return "* SSID: " + SSID + ", BSSID: " + BSSID + "\nIP: " + ipAddress
				+ ", mac: " + macAddress;
	}

	public String getMac() {
		return macAddress;
	}

	public String getSsid() {
		return SSID;
	}

	public String getIP() {
		return ipAddress;
	}

	public String print() {
		return "SSID: " + SSID + ", BSSID: " + BSSID + "\nIP: " + ipAddress
				+ ", mac: " + macAddress + " configured: " + configuredSsids
				+ ", scanned: " + scannedSsids;
	}

	public String getScannedNetworks() {
		return scannedSsids;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof WifiInfoItem))
			return false;
		else {
			WifiInfoItem other = (WifiInfoItem) obj;
			boolean ssid = other.getSsid().trim().equals(getSsid().trim());
			boolean bssid =	 other.getBssid().trim().equals(getBssid().trim());
			boolean ip = other.getIP().trim().equals(getIP().trim());
			boolean mac = other.getMac().trim().equals(getMac().trim());
			boolean scanned = other.getScannedNetworks().trim().equals(getScannedNetworks().trim());
			boolean configured = other.getConfiguredNetworks().trim().equals(
							getConfiguredNetworks().trim());
			return ssid && bssid && ip && mac && scanned && configured;
		
		}
	}
}
