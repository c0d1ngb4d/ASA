package com.thesis.asa.internet;

public class InternetItem {
	private String JavascriptEnabled; 
	
	public InternetItem(Object[] parameters) {
		JavascriptEnabled = (String) parameters[0];
	}

	public boolean getJavascriptEnabled(){
		return JavascriptEnabled.equals("Enable"); 
	}
	
}
