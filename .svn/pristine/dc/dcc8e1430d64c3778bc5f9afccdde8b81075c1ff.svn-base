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