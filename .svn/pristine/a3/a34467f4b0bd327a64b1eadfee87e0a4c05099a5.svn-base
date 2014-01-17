package com.thesis.asa.hook;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;

public class FakePhoneStateListener extends PhoneStateListener {

	private PhoneStateListener phoneStateListener;
	private CellLocation cellLocation;

	public void setListener(PhoneStateListener p){
		phoneStateListener = p;
	}
	
	public void setcellLocation(CellLocation c){
		cellLocation = c;
	}
	
	public void onCellLocationChanged(CellLocation location) {
		phoneStateListener.onCellLocationChanged(cellLocation);
	}

	
}
