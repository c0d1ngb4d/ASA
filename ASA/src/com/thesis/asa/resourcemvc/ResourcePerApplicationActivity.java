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

import java.util.Arrays;

import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.mainui.SlidePageFragment;
import com.thesis.asa.provider.SettingsDB;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ResourcePerApplicationActivity extends ResourceActivity
	{
		private String pkgName;
		
		@Override
		public String getSelectedApplication(){
			return pkgName;
		}
		
		protected void loadSettings() {
			SettingsDB helper = new SettingsDB(this);
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor resourceCursor = db.query(resource.tableName(), null, SettingsDB.COL_PKG_NAME+"=?", new String[] {pkgName}, null, null, null);
			
			if (resourceCursor != null && resourceCursor.moveToFirst() && resourceCursor.getCount()>0)
			{
				Object configuration = resource.configurationFromCursor(resourceCursor);
				Object[] settings = resource.loadSettingsFromConfiguration(configuration);
				view.displaySettingsFromConfiguration(settings);
			}
			else{
				loadDefaultSettings();
			}
			
			if(resourceCursor != null)
				resourceCursor.close();

			db.close();
		}
		
		@Override
		public OnClickListener getController(){
			return new ResourceControllerPerApplication(view, resource);
		}
			
		@Override
		public void onCreate(Bundle bundle)
		{
			super.onCreate(bundle);
			
			LinearLayout layout = (LinearLayout)findViewById(R.id.resource_buttons);
		    Button button = new Button(this); 
		    button.setText(R.string.applyDefault); 
		    button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					SettingsDB helper = new SettingsDB(v.getContext());
					SQLiteDatabase db = helper.getWritableDatabase();
					db.delete(resource.tableName(), SettingsDB.COL_PKG_NAME + " =?", new String[] {pkgName});
					db.close();
					Context context = view.getContext();
					String[] processes = null;
					try {
						processes = Utilities.getProcesses(context, pkgName);
						Utilities.killApp(context, pkgName, processes);
					} catch (NameNotFoundException e) {
						Log.d(Utilities.ERROR, "problems killing app: "+pkgName+" with processes: "+Arrays.toString(processes));
					}
					
					finish();
				}
			});
		    
		    layout.addView(button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		
		protected void loadIntent(Bundle extras) {
			pkgName = extras.getString(SlidePageFragment.PKG_NAME);
			PackageManager manager = this.getPackageManager();
			try {
				isSystem = Utilities.isSystem(this, manager.getApplicationInfo(pkgName, 0).uid) ? 1 : 0;
			} catch (NameNotFoundException e) {
				Log.d(Utilities.ERROR, "Package name: "+pkgName +"not found while starting activity");
			}
			String displayName = extras.getString(SlidePageFragment.DISPLAY_NAME);
			setTitle(displayName);
		}
<<<<<<< HEAD
	}
=======
	}
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
