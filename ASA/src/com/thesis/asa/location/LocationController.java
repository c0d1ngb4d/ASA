package com.thesis.asa.location;

import java.util.List;

import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;

public class LocationController implements OnMarkerClickListener,
		OnCheckedChangeListener, OnMapLongClickListener {

	private LocationSettingsView view;
	private LocationSettings model;
	private LocationConfigurationsModel locationConfigurationsModel;

	public LocationController(LocationSettingsView v, LocationSettings m) {
		view = v;
		model = m;
		locationConfigurationsModel = new LocationConfigurationsModel(
				v.getActivity());
		view.getMap().setOnMarkerClickListener(this);
		view.getMap().setOnMapLongClickListener(this);
		view.getStrategy().setOnCheckedChangeListener(this);
	}

	private void loadSavedConfigurations() {
		List<LocationInfoItem> configurations = locationConfigurationsModel
				.getCustomLocationStoredConfigurations();
		if (configurations.size() == 0) {
			Toast.makeText(
					view.getContext(),
					"No saved location information available. Add one using Settings menu",
					Toast.LENGTH_SHORT).show();
			onCheckedChanged(null, R.id.realRadioButton);
		} else {
			for (LocationInfoItem item : configurations)
				view.addMarketAt(item.getLatitude(), item.getLongitude());
			model.setLocationSelectedSate(configurations.get(0)
					.getLocationInfo());
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		String[] configuration = new String[5];

		if (model.isCustomSelected()) {
			LocationInfoItem location = locationConfigurationsModel
					.findLocationBy(marker.getPosition().latitude,
							marker.getPosition().longitude);
			if (location != null) {
				configuration = location.getLocationInfo();
			} else
				Log.d(Utilities.ERROR, "no location found for marker clicked");
		} else if (model.isMapSelected()) {
			configuration[0] = String.valueOf(marker.getPosition().latitude);
			configuration[1] = String.valueOf(marker.getPosition().longitude);
			configuration[2] = configuration[3] = "null";
		} else
			Log.d(Utilities.ERROR,
					"marker clicked with no custom or map configuration");

		view.notifyLocationSelected();
		model.setLocationSelectedSate(configuration);

		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.realRadioButton:
			String[] reals = { "Real", "Real", "Real", "Real", "Real" };
			model.setLocationSelectedSate(reals);
			view.disableMap();
			break;
		case R.id.customRadioButton:
			view.enableMap();
			view.getMap().clear();
			loadSavedConfigurations();
			break;
		case R.id.mapRadioButton:
			view.enableMap();
			view.getMap().clear();
			LatLng point;
			if (model.isMapSelected()) {
				LocationInfoItem state = model.getLocationState();
				point = new LatLng(state.getLatitude(), state.getLongitude());
			} else {
				point = new LatLng(Utilities.fakeLatitude,
						Utilities.fakeLongitude);
				LocationInfoItem item = new LocationInfoItem(point.latitude,
						point.longitude);
				model.setLocationSelectedSate(item.getLocationInfo());
			}
			view.setCurrentLocation(point);

			break;
		default:
			Log.d(Utilities.ERROR, "No id found");
		}
	}

	@Override
	public void onMapLongClick(LatLng point) {
		if (model.isMapSelected()) {
			LocationInfoItem item = new LocationInfoItem(point.latitude,
					point.longitude);
			view.setCurrentLocation(point);
			model.setLocationSelectedSate(item.getLocationInfo());
		}
	}
}
