/*******************************************************************************
 * Copyright (c) 2014 CodingBad.
 *  All rights reserved.  This file is part of ASA.
 *  
 *  ASA is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  ASA is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *    
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *     Ayelén Chavez - ashy.on.line@gmail.com
 *     Joaquín Rinaudo - jmrinaudo@gmail.com
 ******************************************************************************/
package com.thesis.asa.wifi;


import java.util.List;

import com.thesis.asa.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;

public class SavedWifiStatesDialog {

	private WifiInfoAdapter wifiInfoAdapter;
	private AlertDialog view;
	private Activity activity;
	private int selectedPosition;
	private ListView listView;
	
	public SavedWifiStatesDialog(Activity a, WifiSettings m) {
		activity = a;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.saved_wifi));
		List<WifiInfoItem> customSavedWifi = WifiConfigurationsModel.getCustomWifiStoredConfigurations(activity);
		wifiInfoAdapter = new WifiInfoAdapter(activity, android.R.layout.select_dialog_singlechoice,
				customSavedWifi);

	    selectedPosition = customSavedWifi.indexOf(m.getWifiState());
	    if(selectedPosition == -1){
	    	selectedPosition = 0;
	    }
	    
		builder.setSingleChoiceItems(wifiInfoAdapter, selectedPosition, new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int item){
	            /* User clicked on a radio button do some stuff */
	        }
	    });

	    view = builder.create();
	    view.setCancelable(false);
	    listView = view.getListView();
	    
		SavedWifiStatesController controller = new SavedWifiStatesController(this, m);
		view.setButton(AlertDialog.BUTTON_NEUTRAL, activity.getString(R.string.edit_configuration), controller);
		view.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getString(R.string.cancel), controller);
		view.setButton(AlertDialog.BUTTON_POSITIVE, activity.getString(R.string.apply), controller);
	}

	public Activity getActivity() {
		return activity;
	}
		
	public void show() {
		wifiInfoAdapter = new WifiInfoAdapter(activity, android.R.layout.select_dialog_singlechoice, WifiConfigurationsModel.getCustomWifiStoredConfigurations(activity));
		listView.setAdapter(wifiInfoAdapter);
		listView.setItemChecked(selectedPosition, true);
		view.show();
	}

	public WifiInfoItem getWifiInfo() {
		selectedPosition = listView.getCheckedItemPosition();
    	WifiInfoItem item = wifiInfoAdapter.getItem(selectedPosition);
    	return item;
	}
}
