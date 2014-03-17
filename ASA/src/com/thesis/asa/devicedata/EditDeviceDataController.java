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

import com.thesis.asa.Utilities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EditDeviceDataController implements OnClickListener, OnDismissListener {

	private EditDeviceDataDialog dialog;
	private Activity activity;
	
	public EditDeviceDataController(Activity a,
			EditDeviceDataDialog editDialog) {
		activity = a;
		dialog = editDialog;
	}

	private boolean hasValidConfiguration() {
		return dialog.hasValidDeviceId() && dialog.hasValidSubscriberId() && dialog.hasValidSimSerialNumber() && dialog.hasValidLine1Number();
	}

	@Override
	public void onDismiss(DialogInterface d) {
		dialog.close();
	}

	private void saveConfiguration() {
    	dialog.saveConfiguration();
	}
	
	@Override
	public void onClick(View v) {
		if (hasValidConfiguration()) {
			saveConfiguration();
			dialog.close();
		} else {
			Toast.makeText(
					activity,
					"The configuration you're attempting to save is not valid.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
