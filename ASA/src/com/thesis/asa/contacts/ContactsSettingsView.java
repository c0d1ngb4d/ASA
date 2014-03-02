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
package com.thesis.asa.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.widget.ListView;
import com.thesis.asa.R;
import com.thesis.asa.mainui.SeparatedListAdapter;
import com.thesis.asa.resourcemvc.Resource;
import com.thesis.asa.resourcemvc.ResourceActivity;
import com.thesis.asa.resourcemvc.ResourceView;

public class ContactsSettingsView extends ResourceView {
	
	private HashMap<String, List<ContactsGroupItem>> groupsByAccount;
	private SeparatedListAdapter adapter;
	private ListView view;
	
	public Object[] getSelectedConfiguration() {
		int count = view.getCount();
		
		SparseBooleanArray positions = view.getCheckedItemPositions();
		List<Object> checked = new ArrayList<Object>();
					
		for (int i = 0; i < count; i++)
		{
		    if (positions.get(i)) 
		    {
		    	ContactsGroupItem group = (ContactsGroupItem) adapter.getItem(i);
		    	checked.add(group.getGroupId());
		    }
		}
		return checked.toArray(new Object[0]);
	}
	
	public ContactsSettingsView(ResourceActivity a, Resource r) {
		super(a, r);
		activity.setContentView(R.layout.resource_settings_layout);
		// Create the ListView Adapter
		adapter = new SeparatedListAdapter(activity);
		// Get a reference to the ListView holder
		view = (ListView) activity.findViewById(R.id.list_journal);
		
		Cursor groups = activity.getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
				new String[] {
				ContactsContract.Groups._ID,
				ContactsContract.Groups.TITLE,
				ContactsContract.Groups.ACCOUNT_NAME}
				, null, null, null); 
		
		groupsByAccount = new HashMap<String, List<ContactsGroupItem>>();
				
		if (groups != null && groups.moveToFirst()) {
			
			int idColumnIndex = groups.getColumnIndex(ContactsContract.Groups._ID);
			int groupColumnIndex = groups.getColumnIndex(ContactsContract.Groups.TITLE);
			int accountIndex = groups.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME);

			do{
					String group = groups.getString(groupColumnIndex);
					int id = groups.getInt(idColumnIndex);
					String account = groups.getString(accountIndex);
					ContactsGroupItem groupItem = new ContactsGroupItem(group, account , id);
					if(groupsByAccount.containsKey(account)){ //si estaba ya la cuenta lo agrego sino lo inicializo
						List<ContactsGroupItem> groupsInAccount = groupsByAccount.get(account);
						groupsInAccount.add(groupItem);
						groupsByAccount.put(account, groupsInAccount);
					}
					else{
						List<ContactsGroupItem> groupsInAccount = new ArrayList<ContactsGroupItem>();
						groupsInAccount.add(groupItem);
						groupsByAccount.put(account,groupsInAccount);
					}
			}
			while(groups.moveToNext());
		}
		
		if(groups != null){
			groups.close();
		}
	}
	
	@Override
	public void showResourceViewIn() {
		view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// Add Sections	x
		for (String account : groupsByAccount.keySet())
		{
			ContactsGroupAdapter choiceadapter = new ContactsGroupAdapter(activity,
				android.R.layout.simple_list_item_multiple_choice, groupsByAccount.get(account));
			adapter.addSection(account, choiceadapter);
		}
		
		// Set the adapter on the ListView holder
		view.setAdapter(adapter);
	}

	@Override
	public void displaySettingsFromConfiguration(Object[] settings) {
		
		for (Object id : settings) {
			int position = adapter.getLabelIndexFor((Integer)id);
			view.setItemChecked(position, true);
		}
	}
	
	@Override
	public int getLayoutID() {
		return R.id.list_header_title;
	}
	
}
