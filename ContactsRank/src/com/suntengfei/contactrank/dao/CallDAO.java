package com.suntengfei.contactrank.dao;

import java.util.ArrayList;
import java.util.Date;

import com.sutnengfei.contactrank.model.Call_Record;
import com.sutnengfei.contactrank.model.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class CallDAO
{
	private DBOpenHelper helper;
	private SQLiteDatabase db; 
	
	public CallDAO(Context context)
	{
		helper = new DBOpenHelper(context);
	}
	
	/**
	 * 添加新的电话记录数据
	 * @param calls
	 * @param count
	 */
	public void add(ArrayList<Call_Record> calls)
	{
		db = helper.getWritableDatabase();
		Log.i("calls","sms count:"+String.valueOf(calls.size()));
		for(int i = 0;i<calls.size();i++)
		{

			ContentValues values = new ContentValues();
			values.put("name", calls.get(i).get_name());
			values.put("cid", calls.get(i).get_cid());
			values.put("duration", calls.get(i).get_duration());
			values.put("count", calls.get(i).get_count());
			values.put("time", calls.get(i).get_time());
			long m = db.insert("call", "cid", values);
			Log.i("calls","sms count:"+String.valueOf(m));
			values.clear();
		}
		db.close();
	}
	
	/**
	 * 获取cid用户的时间大于time的通话记录
	 * 详细信息
	 * @param cid
	 * @param time
	 * @return
	 */
	public ArrayList<Call_Record> getData(int cid,int time)
	{
		ArrayList<Call_Record> calls = new ArrayList<Call_Record>();
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("call", new String[]{"cid","name","sum(duration)"
				,"sum(count)","time"}, "cid=? and time>? ", new String[]
						{String.valueOf(cid),String.valueOf(time)}, "time", null, "time");
		while(cursor.moveToNext())
		{
			calls.add(new Call_Record(cursor.getInt(0),cursor.getString(1)
					,cursor.getLong(2),cursor.getInt(3),cursor.getLong(4)));
		}
		cursor.close();
		db.close();
		return calls;
	}
	
	/**
	 * 获取所有通讯录用户(ctt)的总通话记录信息
	 * @param ctt
	 * @return
	 */
	public ArrayList<Call_Record> getAData(ArrayList<Contact> ctt)
	{
		ArrayList<Call_Record> calls = new ArrayList<Call_Record>();
		db = helper.getWritableDatabase();
		for(int i = 0;i<ctt.size();i++)
		{
			Cursor cursor = db.query("call", new String[]{"cid","name","sum(duration)"
					,"sum(count)"}, "cid=?", new String[]
							{String.valueOf(ctt.get(i).get_cid())}, "cid", null, null);
			if(cursor.moveToNext())
			{
				calls.add(new Call_Record(cursor.getInt(0),cursor.getString(1)
						,cursor.getLong(2),cursor.getInt(3),0));
				Log.i("callall","姓名："+cursor.getString(1)+"count:"+String.valueOf(cursor.getInt(3)));

			}
			else
			{
				calls.add(new Call_Record(ctt.get(i).get_cid(),ctt.get(i).get_name(),0,0,0));
			}
			cursor.close();
		}
		db.close();
		return calls;
	}
	
	/**
	 * 
	 * @param cid
	 * @param target 0获取cid好友月通话记录，1获取cid好友总通话记录
	 * @return 单个好友(cid)月(总)通话记录
	 */
	public Call_Record getSData(int cid,int target)
	{
		long time=0;
		Call_Record ct = new Call_Record();
		db = helper.getWritableDatabase();
		Date date = new Date();
		if(target==0)
			time = (date.getYear()+1900)*10000+(date.getMonth()+1)*100;
		
		Cursor cursor = db.query("call", new String[]{"cid","name","sum(duration)"
				,"sum(count)"}, "cid=? and time>? ", new String[]
						{String.valueOf(cid),String.valueOf(time)}, "cid", null, null);
		if(cursor.moveToNext())
			ct = new Call_Record(cid,cursor.getString(1),cursor.getLong(2),cursor.getInt(3),time);
		cursor.close();
		db.close();
		return ct;
	}
	
	
	/**
	 * 获取所有通讯录用户(ctt)的月通话记录信息
	 * @param ctt
	 * @return
	 */
	public ArrayList<Call_Record> getMData(ArrayList<Contact> ctt)
	{
		Cursor cursor;
		Date date = new Date();
		long time = (date.getYear()+1900)*10000+(date.getMonth()+1)*100;
		ArrayList<Call_Record> calls = new ArrayList<Call_Record>();
		db = helper.getWritableDatabase();
		for(int i = 0;i<ctt.size();i++)
		{
			 cursor = db.query("call", new String[]{"cid","name","SUM(duration)"
					,"SUM(count)"}, "cid=? and time>? ", new String[]
							{String.valueOf(ctt.get(i).get_cid()),String.valueOf(time)}, "cid", null, null);
			if(cursor.moveToNext())
			{
				calls.add(new Call_Record(cursor.getInt(0),cursor.getString(1)
						,cursor.getLong(2),cursor.getInt(3),time));
			}
			else
			{
				calls.add(new Call_Record(ctt.get(i).get_cid(),ctt.get(i).get_name(),0,0,0));
			}
			cursor.close();
		}
		db.close();
		return calls;
	}
	
	
}
