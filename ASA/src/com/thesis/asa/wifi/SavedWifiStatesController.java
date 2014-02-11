package com.thesis.asa.wifi;

import android.app.AlertDialog;
import android.content.DialogInterface;


public class SavedWifiStatesController implements DialogInterface.OnClickListener {
	
	private SavedWifiStatesDialog dialog;
	private WifiSettings model;
	private WifiInfoItem item;
	
	public SavedWifiStatesController(SavedWifiStatesDialog list, WifiSettings m) {
		dialog = list;
		model = m;
	}
	
	@Override
	public void onClick(DialogInterface d, int which) {
		switch (which) {
			case AlertDialog.BUTTON_POSITIVE: 
		    	item = dialog.getWifiInfo();
		    	model.setSelectedSate(item.getWifiInfo());
		    	break;
		    	
			case AlertDialog.BUTTON_NEUTRAL: 
				item = dialog.getWifiInfo();
	        	new EditWifiConfiguredNetworksDialog(dialog.getActivity(), item, dialog).show(); 
	        	break;
	    	
			case AlertDialog.BUTTON_NEGATIVE: 
				break;
		};
	}

	public void showDialog() {
		dialog.show();
	}
}
