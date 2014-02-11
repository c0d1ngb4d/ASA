package com.thesis.asa.wifi;

import java.util.List;


import android.view.View;
import android.widget.Toast;

public class WifiController implements android.view.View.OnClickListener {

	private WifiSettingsView view;
	private WifiSettings model;
	
	public WifiController(WifiSettingsView v, WifiSettings m) {
		view = v;	
		model = m;
	}
	
	private void showCustomWifiStoredConfigurations() {
		new SavedWifiStatesDialog(view.getActivity(), model).show();
	}

	@Override
	public void onClick(View v) {
		List<WifiInfoItem> configurations = WifiConfigurationsModel.getCustomWifiStoredConfigurations(view.getContext());
		if (configurations.size() == 0) {
			Toast.makeText(view.getContext(), "No saved WiFi information available. Add one using Settings menu", Toast.LENGTH_SHORT).show();
		} else {
			showCustomWifiStoredConfigurations();
		}
	}
}
