package com.thesis.asa.devicedata;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
