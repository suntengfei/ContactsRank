package com.sutnengfei.contactrank.model;

public class Contact
{
	private int cid;
	private String name;
	private String phoneNumber;
	
	public Contact(int cid,String name,String phoneNumber)
	{
		this.cid = cid;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	public int get_cid()
	{
		return this.cid;
	}
	public String get_name()
	{
		return this.name;
	}
	
	public String get_number()
	{
		return this.phoneNumber;
	}
	
	public String toString()
	{
		return this.name+"__cid:"+String.valueOf(this.cid)+"__number:"+this.phoneNumber;
	}
}
