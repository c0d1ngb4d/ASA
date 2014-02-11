package com.thesis.asa.hook;

public class Main {

	static void initialize() {
		ContextHook.hook();
		ContactsHook.hook();			
		DeviceDataHook.hook();
		WifiHook.hook();
		LocationHook.hook();
	}	
}
