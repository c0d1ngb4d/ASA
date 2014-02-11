package com.thesis.asa.wifi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thesis.asa.R;
import com.thesis.asa.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.InputFilter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditWifiConfiguredNetworksDialog {

	private Activity activity;
	private AlertDialog view;
	private final EditText edit_bssid;
	private final EditText edit_mac;
	private final EditText edit_ssid;
	private final ListView listview;
	private final int[] editIPTextsIds = { R.id.edit_ip_1, R.id.edit_ip_2,
			R.id.edit_ip_3, R.id.edit_ip_4 };
	private final EditText[] editIPTexts;
	private final Button applyButton;
	private final RadioButton scannedRadioButton;
	private final View layout;
	private SavedWifiStatesDialog parent;
	private WifiInfoItem selectedItem;

	public EditWifiConfiguredNetworksDialog(Activity a, WifiInfoItem info, SavedWifiStatesDialog p) {
		selectedItem = info;
		activity = a;
		parent = p;
		layout = LayoutInflater.from(activity).inflate(
				R.layout.edit_wifi_dialog_layout, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getText(R.string.edit_saved_wifi));
		builder.setView(layout);
		edit_mac = (EditText) layout.findViewById(R.id.edit_mac);
		edit_mac.setText(info.getMac().replace(":", ""));
		edit_mac.setFilters(new InputFilter[] { new Utilities.HexFilter(),
				new InputFilter.LengthFilter(12) });

		edit_bssid = (EditText) layout.findViewById(R.id.edit_bssid);
		edit_bssid.setText(info.getBssid().replace(":", ""));
		edit_bssid.setFilters(new InputFilter[] { new Utilities.HexFilter(),
				new InputFilter.LengthFilter(12) });

		edit_ssid = (EditText) layout.findViewById(R.id.edit_ssid);
		edit_ssid.setText(info.getSsid().replace("\"", ""));
		edit_ssid.setFilters(new InputFilter[] { new Utilities.SSIDFilter() });

		listview = (ListView) layout
				.findViewById(R.id.edit_configured_network_list);
		
		String ip = info.getIP();
		String[] parts = ip.split("\\.");
		editIPTexts = new EditText[4];
		for (int i = 0; i < editIPTextsIds.length; i++) {
			editIPTexts[i] = (EditText) layout.findViewById(editIPTextsIds[i]);
			editIPTexts[i].setText(parts[i]);
			editIPTexts[i]
					.setFilters(new InputFilter[] { new Utilities.InputFilterMinMax(
							0, 255) });
		}

		editIPTexts[editIPTextsIds.length - 1]
				.setFilters(new InputFilter[] { new Utilities.InputFilterMinMax(
						1, 255) });

		applyButton = (Button) layout.findViewById(R.id.edit_save_button);

		if (info.getScannedNetworks().equals("[]"))
			scannedRadioButton = (RadioButton) layout
					.findViewById(R.id.edit_fake_scanned);
		else
			scannedRadioButton = (RadioButton) layout
					.findViewById(R.id.edit_real_scanned);

		scannedRadioButton.setChecked(true);
		List<String> items = WifiConfigurationsModel
				.getConfiguredNetworks(activity);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_multiple_choice, items);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		listview.setAdapter(arrayAdapter);
		for (String network : info.listConfiguredNetworks()) 
			listview.setItemChecked(items.indexOf(network), true);

		view = builder.create();

		EditWifiConfiguredNetworksController controller = new EditWifiConfiguredNetworksController(
				activity, this);

		applyButton.setOnClickListener(controller);
		
		view.setOnDismissListener(controller);
	}
	
	public WifiInfoItem getSelectedItem() {
		return selectedItem;
	}
	
	public void show() {
		view.show();
	}
	
	public void close() {
		parent.show();
		view.dismiss();
	}

	public String getConfiguredScanned() {
		RadioGroup group = (RadioGroup) layout
				.findViewById(R.id.radioGroupEditScanNetworks);
		if (group.getCheckedRadioButtonId() == R.id.edit_fake_scanned)
			return "[]";
		else if (group.getCheckedRadioButtonId() == R.id.edit_real_scanned) {
			WifiManager wifiManager = (WifiManager) activity
					.getSystemService(Context.WIFI_SERVICE);
			return Arrays.toString(Utilities.getScannedWifis(wifiManager)
					.toArray(new String[0]));
		} else {
			Log.d(Utilities.ERROR,
					"No networks configuration radio button found");
			return "";
		}
	}

	public String getConfiguredSsid() {
		return edit_ssid.getText().toString();
	}

	public String getConfiguredBssid() {
		return edit_bssid.getText().toString();
	}

	public String getConfiguredMac() {
		return edit_mac.getText().toString();
	}

	public String getConfiguredIp() {
		String ip = editIPTexts[0].getText().toString();
		for (int i = 1; i < editIPTexts.length; i++)
			ip = ip + "." + editIPTexts[i].getText().toString();
		return ip;
	}

	public String getConfiguredNetworks() {
		SparseBooleanArray positions = listview.getCheckedItemPositions();
		int count = listview.getCount();
		List<String> checked = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			if (positions.get(i)) {
				checked.add((String) listview.getAdapter().getItem(i));
			}
		}

		return Arrays.toString(checked.toArray(new String[0]));
	}
}
