<<<<<<< HEAD
package com.thesis.asa.hook;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;

public class FakePhoneStateListener extends PhoneStateListener {

	private PhoneStateListener phoneStateListener;
	private CellLocation cellLocation;

	public void setListener(PhoneStateListener p){
		phoneStateListener = p;
	}
	
	public void setcellLocation(CellLocation c){
		cellLocation = c;
	}
	
	public void onCellLocationChanged(CellLocation location) {
		phoneStateListener.onCellLocationChanged(cellLocation);
	}

	
}
=======
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

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;

public class FakePhoneStateListener extends PhoneStateListener {

	private PhoneStateListener phoneStateListener;
	private CellLocation cellLocation;

	public void setListener(PhoneStateListener p){
		phoneStateListener = p;
	}
	
	public void setcellLocation(CellLocation c){
		cellLocation = c;
	}
	
	public void onCellLocationChanged(CellLocation location) {
		phoneStateListener.onCellLocationChanged(cellLocation);
	}

	
}
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
