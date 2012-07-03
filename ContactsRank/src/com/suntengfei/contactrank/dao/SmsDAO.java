package com.suntengfei.contactrank.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sutnengfei.contactrank.model.Contact;
import com.sutnengfei.contactrank.model.Sms_Record;

public class SmsDAO
{
	private DBOpenHelper helper;
	private SQLiteDatabase db; 
	
	public SmsDAO(Context context)
	{
		helper = new DBOpenHelper(context);
	}
	/**
	 * 添加新的短信记录数据
	 * @param smss
	 * @param count
	 */
	public void add(ArrayList<Sms_Record> smss)
	{
		db = helper.getWritableDatabase();
		Log.i("smss","sms count:"+String.valueOf(smss.size()));
		for(int i = 0;i<smss.size();i++)
		{
			//Log.i("DAOtest","1");
			//Log.i("DAOtest","2");
			ContentValues values = new ContentValues();
			values.put("name", smss.get(i).get_name());
			values.put("cid", smss.get(i).get_cid());
			values.put("count", smss.get(i).get_count());
			values.put("time", smss.get(i).get_time());
			//Log.i("DAOtest","3");
			long m = db.insert("sms", "cid", values);
			Log.i("smss","新插入的SMS id:"+String.valueOf(m));
			values.clear();
		}
		db.close();
	}
	
	/**
	 * 获取cid用户的时间大于time的短信记录
	 * 详细信息
	 * @param cid
	 * @param time
	 * @return
	 */
	public ArrayList<Sms_Record> getData(int cid,int time)
	{
		ArrayList<Sms_Record> smss = new ArrayList<Sms_Record>();
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("sms", new String[]{"cid","name"
				,"SUM(count)","time"}, "cid=? and time>?", new String[]
						{String.valueOf(cid),String.valueOf(time)}, "time", null, "time");
		while(cursor.moveToNext())
		{
			smss.add(new Sms_Record(cursor.getInt(0),cursor.getString(1)
					,cursor.getInt(2),cursor.getLong(3)));
		}
		cursor.close();
		db.close();
		return smss;
	}
	
	public ArrayList<Sms_Record> getAData(ArrayList<Contact> ctt)
	{
		ArrayList<Sms_Record> smss = new ArrayList<Sms_Record>();
		db = helper.getWritableDatabase();
		for(int i = 0;i<ctt.size();i++)
		{
			//我把query 中的sms写成call了。。。。55555真不能随便粗心的copy
			Cursor cursor = db.query("sms", new String[]{"cid","name"
					,"sum(count)"}, "cid=?", new String[]
							{String.valueOf(ctt.get(i).get_cid())}, "cid", null, null);
			if(cursor.moveToNext())
			{
				smss.add(new Sms_Record(cursor.getInt(0),cursor.getString(1)
						,cursor.getInt(2),0));
				Log.i("smsall","姓名："+cursor.getString(1)+"count:"+String.valueOf(cursor.getInt(2)));
			}
			else
			{
				smss.add(new Sms_Record(ctt.get(i).get_cid(),ctt.get(i).get_name(),0,0));
			}
		}
		db.close();
		return smss;
	}
	
	public ArrayList<Sms_Record> getMData(ArrayList<Contact> ctt)
	{
		Date date = new Date();
		long time = (date.getYear()+1900)*10000+(date.getMonth()+1)*100;
		ArrayList<Sms_Record> smss = new ArrayList<Sms_Record>();
		db = helper.getWritableDatabase();
		for(int i = 0;i<ctt.size();i++)
		{
			Cursor cursor = db.query("sms", new String[]{"cid","name"
					,"SUM(count)"}, "cid=? and time>? ", new String[]
							{String.valueOf(ctt.get(i).get_cid()),String.valueOf(time)}, "cid", null, null);
			if(cursor.moveToNext())
			{
				smss.add(new Sms_Record(cursor.getInt(0),cursor.getString(1)
						,cursor.getInt(2),time));
			}
			else
			{
				smss.add(new Sms_Record(ctt.get(i).get_cid(),ctt.get(i).get_name(),0,0));
			}
			cursor.close();
		}
		db.close();
		return smss;
	}
	
	/**
	 * 
	 * @param cid
	 * @param target 0获取cid好友月短信记录，1获取cid好友总短信记录
	 * @return 单个好友(cid)月(总)短信记录
	 */
	public Sms_Record getSData(int cid,int target)
	{
		long time=0;
		db = helper.getWritableDatabase();
		Sms_Record sr = new Sms_Record();
		Date date = new Date();
		if(target==0)
			time = (date.getYear()+1900)*10000+(date.getMonth()+1)*100;
		Cursor cursor = db.query("sms", new String[]{"cid","name"
				,"SUM(count)"}, "cid=? and time>? ", new String[]
						{String.valueOf(cid),String.valueOf(time)}, "cid", null, null);
		if(cursor.moveToNext())
			sr = new Sms_Record(cid,cursor.getString(1),cursor.getInt(2),time);
		cursor.close();
		db.close();
		return sr;
	}
	
	public int getMDatacount()
	{
		int j = 0;
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("sms", new String[]{"cid","name"
				,"count","time"}, "cid=1 ", null, null, null, null);
		if(cursor.moveToNext())
		{
			j = cursor.getCount();
		}
		db.close();
		return j;
	}
}
