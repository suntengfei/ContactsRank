package com.suntengfei.contactrank;

import java.util.ArrayList;

import com.sutnengfei.contactrank.model.Rank;;

public class List_Result
{
	private  ArrayList<Rank> mrank;
	private  ArrayList<Rank> arank;
	public List_Result(ArrayList<Rank> mrank,ArrayList<Rank> arank)
	{
		this.mrank = mrank;
		this.arank = arank;
	}
	
	public ArrayList<Rank> getMrank()
	{
		return mrank;
	}
	public ArrayList<Rank> getArank()
	{
		return arank;
	}
}
