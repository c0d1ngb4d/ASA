package com.thesis.asa.hook;


import android.content.Context;
import android.database.Cursor;
import android.os.Binder;
import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.resourcemvc.Resource;

public class Hook {
	protected static Context context;
	
	protected static Object[] queryConfigurationFromASA(Context context,
			Resource resource) throws Throwable {
		int uid = Binder.getCallingUid();
		int pid = Binder.getCallingPid();
		String configuration = "";

		Cursor resourceCursor = resource.getConfigurationForAppsAndProcess(uid,
				pid);

		if (resourceCursor != null && resourceCursor.moveToFirst()
				&& resourceCursor.getCount() > 0) {
			configuration = (String) resource
					.configurationFromCursor(resourceCursor);
		} else {
			int isSystem = Utilities.isSystem(context, uid) ? 1 : 0;
			configuration = (String) resource.defaultConfiguration(isSystem);
		}

		if (resourceCursor != null) {
			resourceCursor.close();
		}

		return resource.loadSettingsFromConfiguration(configuration);
	}

	protected static Object[] getProperties(String className) throws Throwable {
		Context context = Hook.context;
		Resource r = (Resource) Class.forName(className)
				.getDeclaredConstructor(Context.class).newInstance(context);

		if (context == null) {
			SecurityMode mode = SecurityMode.PARANOID;
			int isSystem = 0;
			return Resource.getConfiguration(mode, isSystem, r);
		} else {
			Object[] currentConfiguration = Hook.queryConfigurationFromASA(
					context, r);
			return currentConfiguration;
		}
	}
}
