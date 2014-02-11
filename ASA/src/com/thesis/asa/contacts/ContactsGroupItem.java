package com.thesis.asa.contacts;

public class ContactsGroupItem {	
	private String label;
	private String account;
	private int groupId;

	public ContactsGroupItem(String l, String a, int id){
		label = l;
		account = a;
		groupId = id;
	}

	public String getAccount() {
		return account;
	}

	public String getLabel() {
		return label;
	}

	public int getGroupId() {
		return groupId;
	}
}
