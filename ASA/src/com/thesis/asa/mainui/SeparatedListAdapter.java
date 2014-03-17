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
 
 
package com.thesis.asa.mainui;

import java.util.LinkedHashMap;
import java.util.Map;

import com.thesis.asa.R;
import com.thesis.asa.contacts.ContactsGroupItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SeparatedListAdapter extends BaseAdapter
{
	public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
	public final ArrayAdapter<String> headers;
	public final static int TYPE_SECTION_HEADER = 0;

	public SeparatedListAdapter(Context context)
	{
		headers = new ArrayAdapter<String>(context, R.layout.separated_list_layout);
	}

	public void addSection(String section, Adapter adapter)
	{
		headers.add(section);
		sections.put(section, adapter);
	}
	
	public int getLabelIndexFor(int id) {
		int relativePosition = 0;
		for (Object section : this.sections.keySet()) {			
			Adapter ad = this.sections.get(section);					
			for(int i = 0 ; i < ad.getCount(); i++) {
				ContactsGroupItem group = (ContactsGroupItem) ad.getItem(i);
				
				if(group.getGroupId() == id) {
					return relativePosition+i+1;
				}
			}
			relativePosition += this.sections.get(section).getCount()+1;			
		}
		
		return -1;
	}
	
	public String getNameForPosition(int i) {
		int relativePosition = 0;
		for(Object section : this.sections.keySet()){
			int inc = this.sections.get(section).getCount()+1; 
			if(i < relativePosition+inc){
				return (String) this.getItem(relativePosition);
			}
			else{
				relativePosition += inc;
			}						
		}
		return "";
	}

	public Object getItem(int position)
	{
		for (Object section : this.sections.keySet())
		{
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			if (position == 0) return section;
			if (position < size) return adapter.getItem(position - 1);

			position -= size;
		}
		return null;
	}

	public int getCount()
	{
		int total = 0;
		for (Adapter adapter : sections.values())
			total += adapter.getCount() + 1;
		return total;
	}

	@Override
	public int getViewTypeCount()
	{
		int total = 1;
		for (Adapter adapter : sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}

	@Override
	public int getItemViewType(int position)
	{
		int type = 1;
		for (Object section : this.sections.keySet())
		{
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			if (position == 0) return TYPE_SECTION_HEADER;
			if (position < size) return type + adapter.getItemViewType(position - 1);

			position -= size;
			type += adapter.getViewTypeCount();
		}
		
		return -1;
	}

	public boolean areAllItemsSelectable()
	{
		return false;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		int sectionnum = 0;
		for (Object section : this.sections.keySet())
		{
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			if (position == 0) return headers.getView(sectionnum, convertView, parent);
			if (position < size) return adapter.getView(position - 1, convertView, parent);

			position -= size;
			sectionnum++;
		}
		
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}
