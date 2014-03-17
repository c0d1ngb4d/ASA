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
import java.util.Arrays;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.saurik.substrate.MS;
import com.thesis.asa.internet.InternetSettings;

public class InternetHook extends Hook {

	public static void hook(){
		int sdk = android.os.Build.VERSION.SDK_INT;
		
		final String websettings = sdk < android.os.Build.VERSION_CODES.JELLY_BEAN ?
				"android.webkit.WebSettings" :"android.webkit.WebSettingsClassic";
	
		MS.hookClassLoad(websettings, new MS.ClassLoadHook() {
			public void classLoaded(Class<?> _class) {

				Method method;
				Constructor constructor;
				try {
					constructor = _class.getDeclaredConstructor(Context.class,WebView.class);
					method = _class.getMethod("setJavaScriptEnabled",
							boolean.class);
				} catch (NoSuchMethodException e) {
					constructor =  null;
					method = null;
					Log.d("DEBUG", "No such method or constructor for" + websettings);

				}

				if(constructor != null){

						MS.hookMethod(_class, constructor,
								new MS.MethodAlteration<Object, Void>() {
									public Void invoked(Object hooked,
											Object... args) throws Throwable {
									context = (Context) args[0];
									return invoke(hooked, args);

									}
								});
								
				}
				
				if (method != null) {
					MS.hookMethod(_class, method,
							new MS.MethodAlteration<Object, Void>() {
								public Void invoked(Object hooked,
										Object... args) throws Throwable {
								
								Object[] properties = Hook
											.queryConfigurationFromASA(context,
													new InternetSettings(context));

								Log.d("DEBUG",Arrays.toString(properties));
								if( ((String)properties[0]).equals("Disable")){
									args[0] = false;
								}
								return invoke(hooked, args);

								}
							});
				}				

			}
		});		
		
	}
	
}
