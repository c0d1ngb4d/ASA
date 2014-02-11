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
