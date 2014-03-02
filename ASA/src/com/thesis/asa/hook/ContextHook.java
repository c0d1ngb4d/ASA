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

import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;
import com.saurik.substrate.MS;
import com.thesis.asa.Utilities;

public class ContextHook extends Hook {

	public static void hook() {
		MS.hookClassLoad("android.app.ContextImpl", new MS.ClassLoadHook() {
			public void classLoaded(Class<?> _clazz) {
				hookGetSystemContext(_clazz);
			}
		});
	}

	protected static void hookGetSystemContext(Class<?> _clazz) {
		Method method;
		try {
			Class<?>[] params = new Class[1];
			params[0] = String.class;

			method = _clazz.getMethod("getSystemService", params);
		} catch (NoSuchMethodException e) {
			method = null;
			Log.d(Utilities.ERROR, Log.getStackTraceString(e));
		}

		if (method != null) {

			MS.hookMethod(_clazz, method,
					new MS.MethodAlteration<Context, Object>() {

						public Object invoked(final Context hooked,
								final Object... args) throws Throwable {
							context = hooked;
							return invoke(hooked, args);
						}
					});
		}
	}
}
