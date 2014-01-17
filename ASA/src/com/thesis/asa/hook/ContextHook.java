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
