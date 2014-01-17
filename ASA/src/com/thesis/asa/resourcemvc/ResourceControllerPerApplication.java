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
