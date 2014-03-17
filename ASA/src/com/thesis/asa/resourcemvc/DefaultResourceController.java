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

import java.util.Arrays;
import com.thesis.asa.R;
import com.thesis.asa.provider.SettingsDB;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;

public class DefaultResourceController implements OnClickListener{

	private ResourceView view;
	private Resource resource;
	
	public DefaultResourceController(ResourceView v, Resource r){
		view = v;
		resource = r;
	}
	
	@Override
	public void onClick(View v) {
		
		if(R.id.applyButton == v.getId()){
			
				SettingsDB helper = new SettingsDB(view.getContext());
				SQLiteDatabase db = helper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(SettingsDB.COL_PERMISSION, resource.permissions());
				values.put(SettingsDB.COL_SYSTEM, view.getActivity().isSystem());
				Object[] configuration = view.getSelectedConfiguration(); 
				values.put(SettingsDB.COL_CONFIGURATION, Arrays.toString(configuration));
				db.replace(SettingsDB.DEFAULT_TABLE, null, values);
				db.close();
	
				view.applyClicked();
		}
		if(R.id.cancelButton == v.getId()){
            	view.getActivity().finish();
		}	
	}
	

	
}
