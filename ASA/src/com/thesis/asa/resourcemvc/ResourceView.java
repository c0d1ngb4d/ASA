<<<<<<< HEAD
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
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
package com.thesis.asa.resourcemvc;

import android.view.View;


public abstract class ResourceView extends View {

	protected Resource model;
	protected ResourceActivity activity;
	
	public ResourceView(ResourceActivity a, Resource r) {
		super(a);
		model = r;
		activity = a;
	}

	public ResourceActivity getActivity(){
		return activity;
	}
	
	public abstract void showResourceViewIn();

	public abstract void displaySettingsFromConfiguration(Object[] settings);
		
	public abstract Object[] getSelectedConfiguration();

	public abstract int getLayoutID();

	public void applyClicked() {
		activity.finish();
	}
	
}
