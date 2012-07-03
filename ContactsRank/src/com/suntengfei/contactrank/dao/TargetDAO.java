package com.suntengfei.contactrank.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TargetDAO
{
	private DBOpenHelper helper;
	private SQLiteDatabase db;
	
	public TargetDAO(Context context)
	{
		helper = new DBOpenHelper(context);
	}
	
	public int update(long time,long etime,int init)
	{
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("time", time);
		values.put("etime", etime);
		values.put("target", init);
		db.update("target", values, "id=?", new String[]{String.valueOf(1)});
		db.close();
		return 1;
	}
	
	public long getetime()
	{
		long etime = 0;
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("target", new String[]{"time","etime"}, "id=1 ", null, null, null, null);
		if(cursor.moveToPosition(0))
			etime = cursor.getLong(1);
		cursor.close();
		db.close();
		return etime;
	}
	
	public long gettime()
	{
		long time = 0;
		db = helper.getWritableDatabase();
		Cursor cursor = db.query("target", new String[]{"time","etime"}, "id=? ", new String[]
						{String.valueOf(1)}, null, null, null);
		if(cursor.moveToNext())
			time = cursor.getLong(0);
		cursor.close();
		db.close();
		return time;
			
	}
}
