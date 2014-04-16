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

package com.thesis.asa.hook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.saurik.substrate.MS;
import com.thesis.asa.Utilities;
import com.thesis.asa.internet.InternetSettings;

public class InternetHook extends Hook {

	public static void hook() {
		final String websettings = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN ? "android.webkit.WebSettings"
				: "android.webkit.WebSettingsClassic";

		MS.hookClassLoad(websettings, new MS.ClassLoadHook() {
			public void classLoaded(Class<?> webSettings) {

				Method method;
				Constructor constructor;
				try {
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
						constructor = webSettings.getConstructor(Context.class,
								WebView.class);
					else {
						Class webViewClassic = Class.forName("android.webkit.WebViewClassic"); 
						constructor = webSettings.getDeclaredConstructor(Context.class,
								webViewClassic);
					}

					method = webSettings.getMethod("setJavaScriptEnabled",	boolean.class);
				} catch (Exception e) {
					constructor = null;
					method = null;
					Log.d(Utilities.ERROR, "No such method or constructor for "
							+ websettings);
					Log.d(Utilities.ERROR, Log.getStackTraceString(e));
				}

				if (constructor != null) {

					MS.hookMethod(webSettings, constructor,
							new MS.MethodAlteration<Object, Void>() {
								public Void invoked(Object hooked,
										Object... args) throws Throwable {
									context = (Context) args[0];
									return invoke(hooked, args);

								}
							});

				}

				if (method != null) {
					MS.hookMethod(webSettings, method,
							new MS.MethodAlteration<Object, Void>() {
								public Void invoked(Object hooked,
										Object... args) throws Throwable {

									Object[] properties = Hook
											.queryConfigurationFromASA(context,
													new InternetSettings(
															context));

									if (((String) properties[0])
											.equals("Disable"))
										args[0] = false;

									return invoke(hooked, args);
								}
							});
				}

			}
		});

	}

}
