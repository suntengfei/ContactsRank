package com.sutnengfei.contactrank.model;

public class Call_Record
{
	private long time;
	private long duration;
	private int count;
	private String name;
	private int cid;
	
	public Call_Record()
	{
		this.time = 0;
		this.duration = 0;
		this.name = null;
		this.cid = 0;
		this.count = 0;
	}
	
	public Call_Record(int cid,String name,long duration,int count,long time)
	{
		this.time = time;
		this.duration = duration;
		this.name = name;
		this.cid = cid;
		this.count = count;
	}
	
	public void set_cid(int cid)
	{
		this.cid = cid;
	}
	
	public int get_cid()
	{
		return this.cid;
	}
	
	public void set_time(Long time)
	{
		this.time = time;
	}
	public long get_time()
	{
		return this.time;
	}
	public void set_duration(Long duration)
	{
		this.duration = duration;
	}
	public long get_duration()
	{
		return this.duration;
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
	
	public String toString()
	{
		return name+"__cid:"+String.valueOf(cid)+"__time:"+String.valueOf(time)
				+"__duration:"+String.valueOf(duration)+"__count:"+String.valueOf(count);
	}
}
