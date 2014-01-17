package com.thesis.asa.devicedata;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.thesis.asa.R;
import com.thesis.asa.resourcemvc.EditableResourceView;
import com.thesis.asa.resourcemvc.Resource;
import com.thesis.asa.resourcemvc.ResourceActivity;

public class DeviceDataSettingsView extends EditableResourceView {
	
	public DeviceDataSettingsView(ResourceActivity a, Resource r) {
		super(a, r);
		dataInitialization();		
		
		activity.setContentView(R.layout.device_data_layout);
		initializeEditable();
		controller = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DeviceDataInfoItem item = ((DeviceDataSettings) model).getDeviceDataState();
				new EditDeviceDataDialog(activity, (DeviceDataSettings) model, item).show(); 
			}
		};
	}

	private void dataInitialization() {
		properties = new String[5];
		properties[0] = "AndroidId";
		properties[1] = "DeviceId";
		properties[2] = "SubscriberId";
		properties[3] = "SIM";
		properties[4] = "LineNumber";

		ids = new int[5];
		ids[0] = R.id.radioGroupAndroidId;
		ids[1] = R.id.radioGroupDeviceId;
		ids[2] = R.id.radioGroupSubscriberId;
		ids[3] = R.id.radioGroupSIM;
		ids[4] = R.id.radioGroupLineNumber;
	}

	@Override
	public void showResourceViewIn() {
	}

	@Override
	public Object[] getSelectedConfiguration() {
		if (((DeviceDataSettings) model).isCustomSelected())
			return ((DeviceDataSettings) model).getDeviceDataState().getDeviceDataInfo();

		String[] configuration = new String[ids.length];
		RadioGroup group;
		RadioButton button;

		for (int i = 0; i < ids.length; i++) {
			group = (RadioGroup) activity.findViewById(ids[i]);
			int id = group.getCheckedRadioButtonId();
			button = (RadioButton) activity.findViewById(id);
			configuration[i] = button.getText().toString();
		}

		return configuration;

	}

	@Override
	public int getLayoutID() {
		return R.id.device_linear_layout;
	}
}
