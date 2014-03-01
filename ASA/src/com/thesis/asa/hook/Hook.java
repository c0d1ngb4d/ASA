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
