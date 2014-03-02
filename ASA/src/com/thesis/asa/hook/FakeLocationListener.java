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
