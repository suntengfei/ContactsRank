package com.suntengfei.contactrank.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper
{

	private static final int VERSION = 1;
	private static final String DBNAME = "data.db";
	private static final String SMS = "sms";
	private static final String CALL = "call";
	private static final String RANK = "rank";
	private static final String TARGET = "target";
	
	
	public DBOpenHelper(Context context)
	{
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table "+RANK+"(id integer primary key," +
				"name varchar(20),cid integer," +
				"wpoint integer,mpoint integer,apoint integer,target integer)");
		db.execSQL("create table "+SMS+"(id integer primary key," +
				"name varchar(20),cid integer," +
				"count integer,time integer)");
		db.execSQL("create table "+CALL+"(id integer primary key," +
				"name varchar(20),cid integer," +
				"duration integer,count integer,time integer)");
		db.execSQL("create table "+TARGET+"(id integer primary key,time integer,etime integer,target integer)");
		String sql = "insert into target (id,time,etime,target) values(1,0,0,0)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}
