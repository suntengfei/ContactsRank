package com.sutnengfei.contactrank.model;

public class Rank
{
	private int cid;
	private String name;
	private int wpoint;
	private int mpoint;
	private int apoint;
	private int target;
	
	public Rank(int cid,String name,int wpoint,int mpoint,int apoint,int target)
	{
		this.cid = cid;
		this.name = name;
		this.wpoint = wpoint;
		this.mpoint = mpoint;
		this.apoint = apoint;
		this.target = target;
	}
	
	public int get_cid()
	{
		return this.cid;
	}
	
	public String get_name()
	{
		return this.name;
	}
	
	public int get_target()
	{
		return this.target;
	}
	
	public int get_wpoint()
	{
		return this.wpoint;
	}
	
	public void set_wpoint(int point)
	{
		this.wpoint = point;
	}
	
	public int get_mpoint()
	{
		return this.mpoint;
	}
	
	public void set_mpoint(int mpoint)
	{
		this.mpoint = mpoint;
	}
	public int get_apoint()
	{
		return this.apoint;
	}
	
	public void set_apoint(int point)
	{
		this.apoint = point;
	}
	
	public String toString()
	{
		if(apoint==0)
			return "姓名："+this.name+"\n"+"月得分："+String.valueOf(this.mpoint);
		else
			return "姓名："+this.name+"\n"+"总得分："+String.valueOf(this.apoint);
	}
	
}