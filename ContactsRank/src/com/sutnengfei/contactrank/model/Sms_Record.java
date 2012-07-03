package com.sutnengfei.contactrank.model;

public class Sms_Record
{
	private int cid;
	private int count;
	private String name;
	private long time;
	
	public Sms_Record()
	{
		this.cid = 0;
		this.count = 0;
		this.name = null;
		this.time = 0;
	}
	
	public Sms_Record(int cid,String name,int count,long time)
	{
		this.cid = cid;
		this.count = count;
		this.name = name;
		this.time = time;
	}
	
	public void set_cid(int cid)
	{
		this.cid = cid;
	}
	public int get_cid()
	{
		return this.cid;
	}
	
	public void set_count(int count)
	{
		this.count = count;
	}
	
	public int get_count()
	{
		return this.count;
	}
	
	public void set_name(String name)
	{
		this.name = name;
	}
	public String get_name()
	{
		return this.name;
	}
	
	public void set_time(Long time)
	{
		this.time = time;
	}
	
	public long get_time()
	{
		return this.time;
	}
	
	public String toString()
	{
		return this.name+"__id:"+String.valueOf(cid)+"__count:"+String.valueOf(this.count)+"__time:"+String.valueOf(this.time);
	}
}
