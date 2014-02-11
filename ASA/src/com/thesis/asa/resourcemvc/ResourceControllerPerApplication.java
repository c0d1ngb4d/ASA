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
package com.thesis.asa.resourcemvc;

import java.util.Arrays;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ResourceControllerPerApplication implements OnClickListener{

	private ResourceView view;
	private Resource resource;
	
	public ResourceControllerPerApplication(ResourceView v, Resource r){
		view = v;
		resource = r;
	}
	
	@Override
	public void onClick(View v) {
		if(R.id.applyButton == v.getId() ){
			
				String pkgName = "";
				try {
					pkgName = view.getActivity().getSelectedApplication();
				}catch (Exception e1) {
					Log.d(Utilities.ERROR, "No pkgName found for controller or problem gettings processes");
				}
				
				Context context = view.getActivity();
				String[] processes = null;
				try {
					processes = Utilities.getProcesses(context, pkgName);
					Utilities.killApp(context, pkgName, processes);
				} catch (NameNotFoundException e) {
					Log.d(Utilities.ERROR, "problems killing app: "+pkgName+" with processes: "+Arrays.toString(processes));
				}
				
				Object[] configuration = view.getSelectedConfiguration();
				resource.updateSettingsDB(pkgName, processes, configuration);
				view.applyClicked();
		}
		if (R.id.cancelButton == v.getId()){
            	view.getActivity().finish();
		}	
	}
}
