package com.suntengfei.contactrank.dao;

import java.util.ArrayList;

import com.sutnengfei.contactrank.model.Rank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactDAO
{
	private DBOpenHelper helper;
	private SQLiteDatabase db;
	public ContactDAO(Context context)
	{
		helper = new DBOpenHelper(context);
	}
	
	/**
	 * 更新cid好友的三项分数
	 * @param cid
	 * @param wpoint
	 * @param mpoint
	 * @param apoint
	 */
	public void update(int cid,int wpoint,int mpoint,int apoint)
	{
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("wpoint", wpoint);
		values.put("mpoint", mpoint);
		values.put("apoint", apoint);
		db.update("rank", values, "cid=?", new String[]{String.valueOf(cid)});
		db.close();
	}
	
	public void updateApoint(int cid,int apoint)
	{
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("apoint", apoint);
		db.update("rank", values, "cid=?", new String[]{String.valueOf(cid)});
		db.close();
	}
	
	/**
	 * 添加新好友到rank表中
	 * @param cid
	 * @param name
	 */
	
	public void updateM(ArrayList<Rank> rk)
	{
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for(int i = 0;i<rk.size();i++)
		{
			values.put("name", rk.get(i).get_name());
			values.put("mpoint", rk.get(i).get_mpoint());
			db.update("rank", values, "cid=?", new String[]{String.valueOf(rk.get(i).get_cid())});
			values.clear();
		}
		db.close();
	}
	
	
	/**
	 * 更新mpoint和apoint
	 * @param rk
	 */
	public void updateA(ArrayList<Rank> rk)
	{
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for(int i = 0;i<rk.size();i++)
		{
			values.put("name", rk.get(i).get_name());
			values.put("apoint", rk.get(i).get_apoint());
			values.put("mpoint", rk.get(i).get_mpoint());
			db.update("rank", values, "cid=?", new String[]{String.valueOf(rk.get(i).get_cid())});
			values.clear();
		}
		db.close();
	}
	
	public Rank getSRank(int cid)
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("rank", new String[]{"cid","name","apoint","mpoint"
		},"cid=?",new String[]{String.valueOf(cid)}, null, null, null);
		if(cursor==null||cursor.getCount()==0)
			return null;
		if(cursor.moveToNext())
			return new Rank(cursor.getInt(0),cursor.getString(1),0,0,cursor.getInt(2),0);
		return null;
	}
	
	public ArrayList<Rank> getARank()
	{
		db = helper.getWritableDatabase();
		ArrayList<Rank> rk = new ArrayList<Rank>();
		Cursor cursor = db.query("rank", new String[]{"cid","name","SUM(apoint)","SUM(mpoint)"
				},null, null, "name", null, "SUM(apoint)+SUM(mpoint) desc");
		while(cursor.moveToNext())
		{
			if(cursor.getInt(2)==0&&cursor.getInt(3)==0)
				break;
			rk.add(new Rank(cursor.getInt(0),cursor.getString(1),0,0,cursor.getInt(2)+cursor.getInt(3),0));
		}
		cursor.close();
		db.close();
		return rk;
	}
	
	public ArrayList<Rank> getMRank()
	{
		db = helper.getWritableDatabase();
		ArrayList<Rank> rk = new ArrayList<Rank>();
		Cursor cursor = db.query("rank", new String[]{"cid","name","SUM(mpoint)"
				},null, null, "name", null, "mpoint desc");
		while(cursor.moveToNext())
		{
			if(cursor.getInt(2)==0)
				break;
			rk.add(new Rank(cursor.getInt(0),cursor.getString(1),0,cursor.getInt(2),0,0));
		}
		cursor.close();
		db.close();
		return rk;
	}
	
	public void add(int cid,String name)
	{
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("cid", cid);
		values.put("name", name);
		values.put("wpoint", 0);
		values.put("mpoint", 0);
		values.put("apoint", 0);
		values.put("target", 0);
		db.insert("rank", "cid", values);
		db.close();
	}
	/**
	 * 查找cid好友是否存在于rank表中
	 * @param cid
	 * @return 0不存在  1存在
	 */
	public int search(int cid)
	{
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("rank", new String[]{"name"}, "cid=?", new String[]
						{String.valueOf(cid)}, null, null, null);
		if(cursor==null||cursor.getCount()==0)
		{
			cursor.close();
			db.close();
			return 0;
		}
		cursor.close();
		db.close();
		return 1;
	}
}
