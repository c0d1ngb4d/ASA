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
