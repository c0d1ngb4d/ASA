/*******************************************************************************
 * Copyright (c) 2014 CodingBad.
 * All rights reserved.  This file is part of ASA.
 * 
 * ASA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASA.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * Ayelén Chavez - ashy.on.line@gmail.com
 * Joaquín Rinaudo - jmrinaudo@gmail.com
 ******************************************************************************/
 
 
package com.thesis.asa.resourcemvc;

import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.mainui.SlidePageFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;

public abstract class ResourceActivity extends android.support.v4.app.FragmentActivity
	{
		protected int isSystem;	
		protected Resource resource;
		protected ResourceView view;
		
		public int isSystem(){
			return isSystem;	
		}
		
		public abstract String getSelectedApplication() throws Exception;
		
		public void loadDefaultSettings() {
			Object defaultConfiguration = resource.defaultConfiguration(isSystem);
			Object[] settings = resource.loadSettingsFromConfiguration(defaultConfiguration);
			view.displaySettingsFromConfiguration(settings);
		}
						
		protected abstract void loadIntent(Bundle extras);
		
		protected  abstract OnClickListener getController();
		
		@Override
		public void onCreate(Bundle bundle)
		{
			super.onCreate(bundle);
			
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			String name = extras.getString(SlidePageFragment.RESOURCE);
			try {
				
				resource = (Resource) Class.forName(name).getDeclaredConstructor(Context.class).newInstance(this);
				view = (ResourceView) Class.forName(name+"View").getDeclaredConstructor(ResourceActivity.class, Resource.class).newInstance(this, resource);
			}
			catch (Exception e) {
				Log.d(Utilities.ERROR,"ERROR AT LOADING CLASS OR CLASS OBJECT INITIALIZATION"+name+"View");
				Log.d(Utilities.ERROR,Log.getStackTraceString(e));
			}
		
			loadIntent(extras);
			
			// Sets the View Layer
			view.showResourceViewIn();
			
			loadSettings();
						
			Button apply = (Button) findViewById(R.id.applyButton);
			OnClickListener controller = getController();
			apply.setOnClickListener(controller);

			Button cancel = (Button) findViewById(R.id.cancelButton);
			cancel.setOnClickListener(controller);					
		}
		
		protected abstract void loadSettings();
	}
