package com.thesis.asa.resourcemvc; 

import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.devicedata.DeviceDataSettings;
import com.thesis.asa.wifi.WifiSettings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditableResourceView extends ResourceView {
	protected int[] ids;
	protected String[] properties;
	
	protected CheckBox customize;
	protected Button editCustom;
	protected OnClickListener controller;
	
	public EditableResourceView(ResourceActivity a, Resource r) {
		super(a, r);
	}

	protected void initializeEditable() {
		customize = (CheckBox) activity.findViewById(R.id.customCheckbox);
		editCustom = (Button) activity.findViewById(R.id.customButton);	
		initializeCustomizeController();
	}

	protected void initializeCustomizeController() {
		customize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if (isChecked) {
					disableFixedConfiguration();
					editCustom.setEnabled(true);
					customize.setChecked(true);
				} else {
					String[] configuration = (String[]) model.getConfigurationByMode(loadSelectedSecurityMode(), activity.isSystem());
					model.setSelectedSate(configuration);
					enableFixedConfiguration(configuration);
					editCustom.setEnabled(false);
					customize.setChecked(false);
				}
			}
		});
		
	}
	
	public void disableFixedConfiguration() {
		for (int i = 0; i < ids.length; i++) {
			RadioGroup group = (RadioGroup) activity.findViewById(ids[i]);
			for (int j = 0; j < group.getChildCount(); j++){
				((RadioButton) group.getChildAt(j)).setEnabled(false);
			}
		}
	}
	
	public void enableFixedConfiguration(String[] configuration) {
		displaySettingsFromConfiguration(configuration);
		
		for (int i = 0; i < ids.length; i++) {
			RadioGroup group = (RadioGroup) activity.findViewById(ids[i]);
			for (int j = 0; j < group.getChildCount(); j++){
				((RadioButton) group.getChildAt(j)).setEnabled(true);
			}
		}
	}

	protected SecurityMode loadSelectedSecurityMode() {
		 SharedPreferences settings = activity.getSharedPreferences(Utilities.FIRST_RUN_PREFERENCE, 0);
		 int mode = settings.getInt(Utilities.SELECTED_MODE, -1);
		 
		 return SecurityMode.fromInteger(mode);
	}

	@Override
	public void showResourceViewIn() {

	}

	@SuppressLint("DefaultLocale") @Override
	public void displaySettingsFromConfiguration(Object[] settings) {
		boolean customized = false;
		if (model instanceof WifiSettings) 
			customized = ((WifiSettings) model).isCustomSelected();
		else
			customized = ((DeviceDataSettings) model).isCustomSelected();
		
		editCustom.setEnabled(customized);
		editCustom.setOnClickListener(controller);
		customize.setChecked(customized);

		if (customized) {
			disableFixedConfiguration();
		} else {
			String selection;
			RadioButton button;
			int id;
			for (int i = 0; i < settings.length; i++) {
				selection = settings[i].toString().toLowerCase();
				id = activity.getResources().getIdentifier(
						(selection + properties[i]).trim(), "id",
						activity.getPackageName());
				button = (RadioButton) activity.findViewById(id);
				button.setChecked(true);
			}
		}
	}

	@Override
	public Object[] getSelectedConfiguration() {
		return null;
	}

	@Override
	public int getLayoutID() {
		return 0;
	}

}
