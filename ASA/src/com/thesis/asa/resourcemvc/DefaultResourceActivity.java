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
package com.thesis.asa.resourcemvc;


import com.thesis.asa.mainui.AdvancedSettingsActivity;

import android.os.Bundle;
import android.view.View.OnClickListener;

public class DefaultResourceActivity extends ResourceActivity
	{
		protected void loadSettings() {
			loadDefaultSettings();
		}
		
		@Override
		public String getSelectedApplication() throws Exception {
			throw new Exception("The activity has no reference to the package Name");
		}
				
		protected void loadIntent(Bundle extras) {
			isSystem = extras.getInt(AdvancedSettingsActivity.SYSTEM, -1);
		}

		@Override
		protected OnClickListener getController() {
			return new DefaultResourceController(view,resource);
		}
		
	}
