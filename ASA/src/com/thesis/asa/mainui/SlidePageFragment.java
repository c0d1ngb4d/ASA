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

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.thesis.asa.Data;
import com.thesis.asa.R;
import com.thesis.asa.Utilities;
import com.thesis.asa.resourcemvc.ResourcePerApplicationActivity;

public class SlidePageFragment extends Fragment {
    public static final String PAGE_NUMBER = "page";

	public static final String PKG_NAME = "com.thesis.asa.packageName";
	public static final String DISPLAY_NAME = "com.thesis.asa.displayName";
	public static final String PERMISSION_NAME = "com.thesis.asa.permissionName";

	public static final String RESOURCE = "resource";
	public static boolean ADVANCED_DISPLAYED = false;
	
	private SeparatedListAdapter separatedListAdapter;
	private ApplicationInfoAdapter applicationsInfoAdapter;
	private ListView applicationList;
	
    private int pageNumber;
    private List<String> permissions;
    
    public static SlidePageFragment create(int pageNumber) {
        SlidePageFragment page = new SlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER, pageNumber);
        page.setArguments(args);
        return page;
    }

    public SlidePageFragment() {
    }
    
    @Override
    public void onActivityCreated(Bundle bundle) {
    	super.onActivityCreated(bundle);

    	if (separatedListAdapter == null) {
			separatedListAdapter = new SeparatedListAdapter(getActivity().getBaseContext());
			applicationList = (ListView) getView().findViewById(R.id.appList);
			
	    	applicationsInfoAdapter = new ApplicationInfoAdapter(getActivity(), permissions, Utilities.getThirdPartyApplicationsByPermission(getActivity(), permissions), getActivity().getPackageManager());
			
			separatedListAdapter.addSection("Third Party Applications", applicationsInfoAdapter);
			
			applicationList.setOnItemClickListener( new OnItemClickListener() {
	            @Override
	        	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
	            		            	
	           		Context context = parent.getContext();

	           		SeparatedListAdapter separatedAdapter = (SeparatedListAdapter) parent.getAdapter();
	           		ApplicationInfo appInfo = (ApplicationInfo) separatedAdapter.getItem(pos);
	           		Intent intent = new Intent(context, ResourcePerApplicationActivity.class);
	           		 
	           		Bundle extras = new Bundle();
	           		String displayName = context.getPackageManager().getApplicationLabel(appInfo).toString();
	           		extras.putString(DISPLAY_NAME, displayName);            		
	           		extras.putString(PKG_NAME, appInfo.packageName);
	           		extras.putString(RESOURCE, Data.getResourceFor(pageNumber));
	           		
	           		intent.putExtras(extras);
	           		Activity activity = (Activity) context;
	           		activity.startActivity(intent); 
	            }
	   		});
			
			final Button settingsButton = (Button) getView().findViewById(R.id.settingsButton);
			settingsButton.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(final View v) {
		        	Context context = v.getContext();
		        	Intent intent = new Intent(context, AdvancedSettingsActivity.class);
		        	intent.putExtra(PAGE_NUMBER, pageNumber);
		        	Activity activity = (Activity) context;
	           		activity.startActivity(intent);
		        }
		    });
			applicationList.setAdapter(separatedListAdapter);
		}
    }
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        pageNumber = getArguments().getInt(PAGE_NUMBER);
        permissions = Data.permissionsForPage(pageNumber);
    }
    
    private void updateAdvanced() {
		if(ADVANCED_DISPLAYED) {
			applicationsInfoAdapter = new ApplicationInfoAdapter(getActivity(), permissions, Utilities.getSystemApplicationsByPermission(getActivity(), permissions), getActivity().getPackageManager());			
			separatedListAdapter.addSection("System Applications", applicationsInfoAdapter);
		}
		else
			separatedListAdapter.sections.remove("System Applications");
		
		applicationList.setAdapter(separatedListAdapter);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	updateAdvanced();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	// Inflate the layout containing a title and body text.
    	ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.slide_page_fragment_layout, container, false);
    	
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return pageNumber;
    }
}
