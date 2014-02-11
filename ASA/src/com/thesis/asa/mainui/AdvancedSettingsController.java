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
