package com.thesis.asa.hook;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class FakeLocationListener implements LocationListener {

	private Location fakeLocation;
	private LocationListener appListener;

	public void setListener(LocationListener p) {
		appListener = p;
	}

	public void setLocation(Location l) {
		fakeLocation = l;
	}

	@Override
	public void onLocationChanged(Location location) {
		appListener.onLocationChanged(fakeLocation);
	}

	@Override
	public void onProviderDisabled(String provider) {
		appListener.onProviderDisabled(provider);

	}

	@Override
	public void onProviderEnabled(String provider) {
		appListener.onProviderEnabled(provider);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		appListener.onStatusChanged(provider, status, extras);

	}

}
