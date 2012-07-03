package com.suntengfei.contactrank;

import java.util.ArrayList;
import java.util.Date;

import com.suntengfei.contactrank.dao.TargetDAO;
import com.sutnengfei.contactrank.model.Sms_Record;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class Sms_Records
{
	//private Sms_Record[] smsrecords;
	private ArrayList<Sms_Record> smsrecords;
	private Date date;
	private TargetDAO td;
	private Context mContext;
	private class data
	{
		String name;
		int cid;
		/**
		 * 存储以电话号码查询出来的联系人姓名和ID
		 * @param name
		 * @param cid
		 */
		public data(String name,int cid)
		{
			this.name = name;
			this.cid = cid;
		}
	};
	
	private final String SMS_URI_ALL = "content://sms/";
	
	public Sms_Records(Context mContext)
	{
		this.smsrecords = new ArrayList<Sms_Record>();
		this.mContext = mContext;
		this.td = new TargetDAO(mContext);
	}
	
	public ArrayList<Sms_Record> refresh_Sms_Records()
	{
		int target = -1;
		int j=0;
		Uri uri = Uri.parse(SMS_URI_ALL);
		long etime = td.getetime();
		
		ContentResolver cr = mContext.getContentResolver();
		String[] projection = new String[] { "_id", "address", "date", "type" };
		Cursor cursor = cr.query(uri, projection, "date>?"
				,new String[]{String.valueOf(etime)},"date");
		
		Log.i("smss",String.valueOf(cursor.getCount())+"__etime:"+String.valueOf(etime)
				+"__curtime"+String.valueOf(new Date().getTime()));
		
		if(cursor==null)
			return smsrecords;
		
		for(int i = 0;i<cursor.getCount();i++)
		{
			cursor.moveToPosition(i);
			data data = check_If_Exist(mContext,cursor.getString(1));
			if(data==null)
			{
				Log.i("bbbbbb","wo le ge");
				continue;
			}
			date = new Date(Long.parseLong(cursor.getString(2)));
			long time = makeDate(date);
			
			if(target<0)
			{
				smsrecords.add(new Sms_Record(data.cid,data.name,1,time));
				target = 0;
				j++;
				Log.i("bbbbbb", "init");
			}
			else
			 {
				 //循环检测当天短信记录中是否已有此联系人
				 if(time==smsrecords.get(j-1).get_time())
				 {
					 Log.i("bbbbbb","time");
					 int m;
					 for( m = target;m<=j-1;m++)
					 {
						 Log.i("bbbbbb",smsrecords.get(m).get_name()+"   m="+String.valueOf(m));
						 if(data.name.equals(smsrecords.get(m).get_name()))
						 {
							 smsrecords.get(m).set_count(smsrecords.get(m).get_count()+1);
							 break;
						 }
					 }
					 if(m>j-1)
					 {
						 smsrecords.add(new Sms_Record(data.cid,data.name,1,time));
						 j++;
					 }
				 }
				 else
				 {
					 smsrecords.add(new Sms_Record(data.cid,data.name,1,time));
					 target = j;
					 j++;
					 
				 }
			 }			
		}
		
		for(int s = 0;s<j;s++)
			Log.i("smss",smsrecords.get(s).toString());
		
		return smsrecords;
	}
	
	public long makeDate(Date date)
	{
		return (long)((date.getYear()+1900)*10000+(date.getMonth()+1)*100+date.getDate());
	}
	
	public data check_If_Exist(Context mContext,String number)
	{
		String[] projection = { Phone.DISPLAY_NAME, Phone.CONTACT_ID };
		Cursor cursor = mContext.getContentResolver().query(
				Phone.CONTENT_URI,
                projection,    // Which columns to return.
                Phone.NUMBER + " = '" + number + "'", // WHERE clause.
                null,          // WHERE clause value substitution
                null);
		if(cursor==null)
			return null;
		else 
		{
			if(cursor.getCount()==0)
				return null;
			cursor.moveToPosition(0);
			return new data(cursor.getString(0),cursor.getInt(1));
		}
	}
	
}
