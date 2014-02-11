package com.thesis.asa.devicedata;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thesis.asa.R;
import com.thesis.asa.resourcemvc.ResourceActivity;

public class EditDeviceDataDialog {

	private AlertDialog view;
	private Activity activity;
	private DeviceDataInfoItem selectedItem;
	private View layout;
	private final EditText editAndroidId;
	private final EditText editDeviceId;
	private final EditText editSubscriberId;
	private final EditText editSimSerialNumber;
	private final EditText editLine1Number;
	private final Button applyButton;
	private int androidIdLenght;
	private int deviceIdLenght;
	private int subscriberIdLenght;
	private int simSerialNumberLenght;
	private int line1NumberLenght;
	private DeviceDataSettings model;
	private boolean hasPlus = false;

	public EditDeviceDataDialog(ResourceActivity a, DeviceDataSettings m, DeviceDataInfoItem item) {
		selectedItem = item;
		activity = a;
		model = m;

		layout = LayoutInflater.from(activity).inflate(
				R.layout.edit_device_data_dialog_layout, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getText(R.string.edit_saved_device_data));
		builder.setView(layout);

		String realValue = item.getAndroidId().trim();
		androidIdLenght = realValue.length();
		editAndroidId = (EditText) layout.findViewById(R.id.edit_androidId);
		editAndroidId.setText(realValue);
		editAndroidId
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						androidIdLenght) });
		
		realValue = item.getDeviceId().trim();
		deviceIdLenght = realValue.length();
		editDeviceId = (EditText) layout.findViewById(R.id.edit_deviceId);
		editDeviceId.setText(realValue);
		editDeviceId
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						deviceIdLenght) });

		realValue = item.getSubscriberId().trim();
		subscriberIdLenght = realValue.length();
		
		editSubscriberId = (EditText) layout
				.findViewById(R.id.edit_subscriberId);
		editSubscriberId.setText(realValue);
		editSubscriberId
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						subscriberIdLenght) });

		realValue = item.getSimSerialNumber().trim();
		simSerialNumberLenght = realValue.length();
		
		editSimSerialNumber = (EditText) layout.findViewById(R.id.edit_sim);
		editSimSerialNumber.setText(realValue);
		editSimSerialNumber
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						simSerialNumberLenght) });

		line1NumberLenght = 15;
		editLine1Number = (EditText) layout.findViewById(R.id.edit_lineNumber);

		Log.d("DEBUG","----"+item.getLine1Number()+"*---");
		realValue = item.getLine1Number().trim();

		if(realValue != null && !realValue.equals("")){
			realValue = item.getLine1Number().trim();
			if (realValue.charAt(0) == '+') {
				hasPlus = true;
				realValue = realValue.substring(1);
			}
			
			line1NumberLenght = realValue.length();
			
			TextView editLine1NumberText = (TextView) layout.findViewById(R.id.edit_lineNumber_text);
			editLine1NumberText.setText(R.string.lineNumber_short_plus);
			
			editLine1Number.setText(realValue);
		}
		editLine1Number
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						line1NumberLenght) });

		applyButton = (Button) layout.findViewById(R.id.edit_save_button);
		view = builder.create();

		EditDeviceDataController controller = new EditDeviceDataController(
				activity, this);
		applyButton.setOnClickListener(controller);
		view.setOnDismissListener(controller);
	}

	public void show() {
		view.show();
	}

	public void close() {
		view.dismiss();
	}

	public DeviceDataInfoItem getSelectedItem() {
		return selectedItem;
	}

	public boolean hasValidDeviceId() {
		String deviceId = editDeviceId.getText().toString().trim();
		return deviceId.length() == deviceIdLenght;
	}

	public boolean hasValidSubscriberId() {
		String subscriberId = editSubscriberId.getText().toString().trim();
		return subscriberId.length() == subscriberIdLenght;
	}

	public boolean hasValidSimSerialNumber() {
		String simSerialNumber = editSimSerialNumber.getText().toString().trim();
		return simSerialNumber.length() == simSerialNumberLenght;
	}

	public boolean hasValidLine1Number() {
		String line1Number = editLine1Number.getText().toString().trim();
		return line1Number.length() == line1NumberLenght;
	}

	public void saveConfiguration() {
		String[] info = new String[5];
		info[0] = editAndroidId.getText().toString().trim();
		info[1] = editDeviceId.getText().toString().trim();
		info[2] = editSubscriberId.getText().toString().trim();
		info[3] = editSimSerialNumber.getText().toString().trim();
		if (hasPlus)
			info[4] = "+"+editLine1Number.getText().toString().trim();
		else
			info[4] = editLine1Number.getText().toString().trim();
		model.setSelectedSate(info);
	}
}
