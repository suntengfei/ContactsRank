package com.suntengfei.contactrank;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;

import com.suntengfei.contactrank.dao.TargetDAO;
import com.sutnengfei.contactrank.model.Call_Record;

public class Call_Records
{
	private ArrayList<Call_Record> crds;
	//private Call_Record[] callrecords;
	private TargetDAO td;	
	Date date;
	private Context mContext;
	
	public Call_Records(Context mContext)
	{
		this.mContext = mContext;
		this.td = new TargetDAO(mContext);
		crds = new ArrayList<Call_Record>();
	}
	
	/**
	 * 
	 * @param mContext
	 * @return
	 * 1、检测duration是否为0
	 * 2、检测name是否为空
	 * 3、检测已录入数据中最后一条数据日期和将插入的数据日期是否相等
	 * 4、若相等，则遍历此日期记录中是否有此联系人，若有，则将duration加到这个联系人的duration中
	 *    若无，则添加一条数据
	 */
	public ArrayList<Call_Record> refresh_Call_Records()
	{
		crds.clear();
		int target = -1;
		int j=0;
		long etime = td.getetime();
		
		ContentResolver cr = mContext.getContentResolver();	
		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{
				CallLog.Calls.NUMBER,CallLog.Calls.CACHED_NAME,CallLog.Calls.DURATION
				, CallLog.Calls.DATE}, CallLog.Calls.DATE+">?"
				,new String[]{String.valueOf(etime)},CallLog.Calls.DATE);
		
		if(cursor==null)
			return crds;
		
		//callrecords = new Call_Record[cursor.getCount()];
		
		 for (int i = 0; i < cursor.getCount(); i++) {
			 cursor.moveToPosition(i);
			 if(!TextUtils.isEmpty(cursor.getString(1)))
			 	Log.i("aaaaaa", cursor.getString(1)+"1");
			 if(TextUtils.isEmpty(cursor.getString(1))||cursor.getLong(2)==0)
				 continue;
			 Log.i("aaaaaa",cursor.getString(3));
			 date = new Date(Long.parseLong(cursor.getString(3)));  
			 long time = makeDate(date);
			 
			 if(target<0)
			 {
				 int cid = getID(mContext,cursor.getString(1));
				 if(cid ==0)
					 continue;
				 crds.add(new Call_Record(cid,cursor.getString(1),cursor.getLong(2),1,time));
/*				 callrecords[j]= new Call_Record();
				 callrecords[j].set_duration(cursor.getLong(2));
				 callrecords[j].set_count(1);
				 callrecords[j].set_name(cursor.getString(1));
				 callrecords[j].set_time(time);*/
				 target = 0;
				 j++;
				 Log.i("aaaaaa", cursor.getString(1)+"2");
			 }
			 else
			 {
				 //循环检测当天通话记录中是否已有此联系人
				 if(time==crds.get(j-1).get_time())
				 {
					 Log.i("cccccc", "call_time="+String.valueOf(time));
					 int m;
					 for( m = target;m<=j-1;m++)
					 {
						 if(crds.get(m).get_name().equals(cursor.getString(1)))
						 {
							 crds.get(m).set_duration(crds.get(m).get_duration()+cursor.getLong(2));
							 crds.get(m).set_count(crds.get(m).get_count()+1);
							 break;
						 }
					 }
					 if(m>j-1)
					 {
						 int cid = getID(mContext,cursor.getString(1));
						 if(cid ==0)
							 continue;
						 crds.add(new Call_Record(cid,cursor.getString(1),cursor.getLong(2),1,time));
/*						 callrecords[j] = new Call_Record();
						 callrecords[j].set_duration(cursor.getLong(2));
						 callrecords[j].set_count(1);
						 callrecords[j].set_name(cursor.getString(1));
						 callrecords[j].set_time(time);*/
						 j++;
					 }
				 }
				 else
				 {
					 int cid = getID(mContext,cursor.getString(1));
					 if(cid ==0)
						 continue;
					 crds.add(new Call_Record(cid,cursor.getString(1),cursor.getLong(2),1,time));
/*					 callrecords[j] = new Call_Record();
					 callrecords[j].set_duration(cursor.getLong(2));
					 callrecords[j].set_count(1);
					 callrecords[j].set_name(cursor.getString(1));
					 callrecords[j].set_time(time);*/
					 target = j;
					 j++;
				 }
			 }
			 
		 }
		 
/*		 for(int l=0;l<j;l++)
		 {
			 callrecords[l].set_cid(getID(mContext,callrecords[l].get_name()));
		 }
		 */
		 for(int s=0;s<crds.size();s++)
			 Log.i("searchcall",crds.get(s).toString());
		 
		return crds;
	}
	
	public long makeDate(Date date)
	{
		return (long)((date.getYear()+1900)*10000+(date.getMonth()+1)*100+date.getDate());
	}
	
	public int getID(Context mContext,String name)
	{
		String[] projection = {Phone.CONTACT_ID };
		Cursor cursor = mContext.getContentResolver().query(
				Phone.CONTENT_URI,
                projection,    // Which columns to return.
                Phone.DISPLAY_NAME + " = '" + name + "'", // WHERE clause.
                null,          // WHERE clause value substitution
                null);
		if(cursor==null)
			return 0;
		else 
		{
			if(cursor.getCount()==0)
				return 0;
			cursor.moveToPosition(0);
			return cursor.getInt(0);
		}
	}
	
/*	public Call_Record[] getCallRecords()
	{
		return callrecords;
	}
	*/
}
