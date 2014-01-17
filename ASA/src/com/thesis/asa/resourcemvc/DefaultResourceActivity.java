package com.thesis.asa.resourcemvc;


import com.thesis.asa.mainui.AdvancedSettingsActivity;

import android.os.Bundle;
import android.view.View.OnClickListener;

public class DefaultResourceActivity extends ResourceActivity
	{
		protected void loadSettings() {
			loadDefaultSettings();
		}
		
		@Override
		public String getSelectedApplication() throws Exception {
			throw new Exception("The activity has no reference to the package Name");
		}
				
		protected void loadIntent(Bundle extras) {
			isSystem = extras.getInt(AdvancedSettingsActivity.SYSTEM, -1);
		}

		@Override
		protected OnClickListener getController() {
			return new DefaultResourceController(view,resource);
		}
		
	}