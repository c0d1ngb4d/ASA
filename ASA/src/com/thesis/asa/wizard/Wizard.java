<<<<<<< HEAD
package com.thesis.asa.wizard;

import com.thesis.asa.Data;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.mainui.MainSlideActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

public class Wizard extends PreferenceActivity {

	public static final String MODE = "SecurityMode";
	
	 @Override
	 protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addPreferencesFromResource(R.layout.wizard_layout);
		Preference permissive = (Preference) findPreference("permissive_key");
		permissive.setOnPreferenceClickListener(securityModeListener(Data.SecurityMode.PERMISSIVE));
	       
		Preference secure = (Preference) findPreference("secure_key");
		secure.setOnPreferenceClickListener(securityModeListener(Data.SecurityMode.SECURE));
			   
		Preference paranoid = (Preference) findPreference("paranoid_key");
		paranoid.setOnPreferenceClickListener(securityModeListener(Data.SecurityMode.PARANOID));
	 }    
	
	 public OnPreferenceClickListener securityModeListener(final Data.SecurityMode mode){
		  
		   return new OnPreferenceClickListener() {
		   		
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Utilities.loadDefaultConfigurationForMode(preference.getContext(), mode);

					Intent data = new Intent();
					data.putExtra(MODE,  mode);
					setResult(MainSlideActivity.SECURITY_MODE_SELECTED, data);
					finish();
					return true;
				}
		   };
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
package com.thesis.asa.wizard;

import com.thesis.asa.Data;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.mainui.MainSlideActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

public class Wizard extends PreferenceActivity {

	public static final String MODE = "SecurityMode";
	
	 @Override
	 protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addPreferencesFromResource(R.layout.wizard_layout);
		Preference permissive = (Preference) findPreference("permissive_key");
		permissive.setOnPreferenceClickListener(securityModeListener(Data.SecurityMode.PERMISSIVE));
	       
		Preference secure = (Preference) findPreference("secure_key");
		secure.setOnPreferenceClickListener(securityModeListener(Data.SecurityMode.SECURE));
			   
		Preference paranoid = (Preference) findPreference("paranoid_key");
		paranoid.setOnPreferenceClickListener(securityModeListener(Data.SecurityMode.PARANOID));
	 }    
	
	 public OnPreferenceClickListener securityModeListener(final Data.SecurityMode mode){
		  
		   return new OnPreferenceClickListener() {
		   		
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Utilities.loadDefaultConfigurationForMode(preference.getContext(), mode);

					Intent data = new Intent();
					data.putExtra(MODE,  mode);
					setResult(MainSlideActivity.SECURITY_MODE_SELECTED, data);
					finish();
					return true;
				}
		   };
	 }
	  
}
>>>>>>> 46da12c22a5800376d8a52d1bb5ba4e85192a2b6
