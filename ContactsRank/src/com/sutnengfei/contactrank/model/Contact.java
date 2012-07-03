package com.sutnengfei.contactrank.model;

public class Contact
{
	private int cid;
	private String name;
	
	public Contact(int cid,String name)
	{
		this.cid = cid;
		this.name = name;
	}
	
	public int get_cid()
	{
		return this.cid;
	}
	public String get_name()
	{
		return this.name;
	}
	
	public String toString()
	{
		return this.name+"__cid:"+String.valueOf(this.cid);
	}
}
