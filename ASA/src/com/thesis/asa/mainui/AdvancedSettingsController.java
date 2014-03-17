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
 
 
package com.thesis.asa.mainui;

import com.thesis.asa.Data;
import com.thesis.asa.resourcemvc.DefaultResourceActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

public class AdvancedSettingsController implements OnPreferenceChangeListener, OnPreferenceClickListener {

	private AdvancedSettingsActivity activity;

	public AdvancedSettingsController(AdvancedSettingsActivity a) {
		activity = a;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean checked = Boolean.valueOf(newValue.toString());
		SlidePageFragment.ADVANCED_DISPLAYED = checked;

		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("system_key"))
			openActivity(preference.getContext(), 1);
		else if (preference.getKey().equals("third_key")) {
			openActivity(preference.getContext(), 0);
		}
		return true;
	}

	private void openActivity(Context context, int isSystem) {

		Intent intent = new Intent(context,
				DefaultResourceActivity.class);

		Bundle extras = new Bundle();
		extras.putInt(AdvancedSettingsActivity.SYSTEM, isSystem);
		extras.putString(SlidePageFragment.RESOURCE,
				Data.getResourceFor(activity.getPageNumber()));
		intent.putExtras(extras);
		Activity activity = (Activity) context;
		activity.startActivity(intent);
	}

}
