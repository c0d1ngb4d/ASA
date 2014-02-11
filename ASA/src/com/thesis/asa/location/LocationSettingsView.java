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
package com.thesis.asa.location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.resourcemvc.Resource;
import com.thesis.asa.resourcemvc.ResourceActivity;
import com.thesis.asa.resourcemvc.ResourceView;

public class LocationSettingsView extends ResourceView {

	private LocationController controller;
	private Fragment fragment;
	private GoogleMap map;
	private RadioGroup strategy;
	private Marker marker;
	
	public LocationSettingsView(ResourceActivity a, Resource r) {
		super(a, r);
		activity.setContentView(R.layout.location_layout);
		FragmentManager manager = activity.getSupportFragmentManager();
		fragment = (Fragment) manager.findFragmentById(R.id.map); 
		SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
		map = supportmapfragment.getMap();
		strategy = (RadioGroup) activity.findViewById(R.id.strategyradioGroup);
		controller = new LocationController(this, (LocationSettings) model);
	}

	public void applyClicked() {
		if (!((LocationSettings)model).isRealSelected())
			Toast.makeText(activity, "Please, set Wifi scanned networks to fake or turn off Google services", Toast.LENGTH_SHORT).show();
		super.applyClicked();
	}
	
	public void enableMap() {
		fragment.getView().setVisibility(View.VISIBLE);
	}

	public void disableMap() {
		fragment.getView().setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void showResourceViewIn() {
	}

	public void displaySettingsFromConfiguration(Object[] settings) {
		if (((LocationSettings) model).isCustomSelected())
			strategy.check(R.id.customRadioButton);
		else if (((LocationSettings) model).isRealSelected())
			strategy.check(R.id.realRadioButton);
		else if (((LocationSettings) model).isMapSelected())
			strategy.check(R.id.mapRadioButton);
		else
			Log.d(Utilities.ERROR, "Invalid configuration");
	}

	@Override
	public Object[] getSelectedConfiguration() {
		return ((LocationSettings) model).getConfiguration();
	}

	@Override
	public int getLayoutID() {
		return R.id.location_linear_layout;
	}

	public void addMarketAt(Double lat, Double lon) {
		LatLng point = new LatLng(lat, lon);
		map.addMarker(new MarkerOptions().position(point).title(point.toString()));
	}

	public GoogleMap getMap() {
		return map;
	}

	public RadioGroup getStrategy() {
		return strategy;
	}

	public void setCurrentLocation(LatLng point) {
		if (marker != null)
				marker.remove();
		
		marker = map.addMarker(new MarkerOptions().position(point).title(point.toString()));
	}

	public void notifyLocationSelected() {
		Toast.makeText(activity, "Location saved", Toast.LENGTH_SHORT).show();
	}
}
