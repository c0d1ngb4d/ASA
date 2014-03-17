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
