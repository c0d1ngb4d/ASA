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
/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thesis.asa.mainui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.thesis.asa.Data;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.wizard.Wizard;

public class MainSlideActivity extends FragmentActivity {

    private static final String IS_FIRST_RUN = "isFirstRun";	
	protected static final int NUM_PAGES = Data.resources().size();
	public static final int SECURITY_MODE_SELECTED = 0;
	private static final boolean RUN_WIZARD = true;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    //when your InstructionsActivity ends, do not forget to set the firstRun boolean
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == SECURITY_MODE_SELECTED && resultCode == 0) {
            SecurityMode mode = (SecurityMode) data.getSerializableExtra(Wizard.MODE);
            SharedPreferences settings = getSharedPreferences(Utilities.FIRST_RUN_PREFERENCE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(IS_FIRST_RUN, false);
            editor.putInt(Utilities.SELECTED_MODE, SecurityMode.toInteger(mode));
            editor.commit();
        }
    }
    
    protected void loadWizard(){
        /*
         * WIZARD ON FIRST RUN
         */
        SharedPreferences settings = getSharedPreferences(Utilities.FIRST_RUN_PREFERENCE, 0);
        boolean firstRun = settings.getBoolean(IS_FIRST_RUN, true);
        if (firstRun)
        {
        	Utilities.loadASAConfiguration(this);
        	Utilities.loadDefaultConfigurationForMode(this, SecurityMode.PERMISSIVE);
        	if (RUN_WIZARD) {
            	// here run your first-time instructions, for example :
                startActivityForResult(
                    new Intent(this, Wizard.class),
                    SECURITY_MODE_SELECTED);        		
        	}
        }
    }
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main_slide_layout);
        
        loadWizard();
        
        // Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        setTitle(Data.titleForPage(pager.getCurrentItem()));
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	setTitle(Data.titleForPage(position));
            	invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(pager.getCurrentItem() > 0);

        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (pager.getCurrentItem() == pagerAdapter.getCount()-1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, this.getClass()));
                return true;

            case R.id.action_previous:
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                pager.setCurrentItem(pager.getCurrentItem() + 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return SlidePageFragment.create(position);
          	}

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
