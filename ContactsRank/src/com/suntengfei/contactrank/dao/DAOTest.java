package com.suntengfei.contactrank.dao;

import java.util.ArrayList;
import java.util.List;

import com.sutnengfei.contactrank.model.Call_Record;
import com.sutnengfei.contactrank.model.Contact;
import com.sutnengfei.contactrank.model.Rank;
import com.sutnengfei.contactrank.model.Sms_Record;

import android.test.AndroidTestCase;
import android.util.Log;

public class DAOTest extends AndroidTestCase
{
	private final static String TAG = "DAOtest";
	
	public void testSmsAdd()
	{
		ArrayList<Sms_Record> smss = new ArrayList<Sms_Record>();
		SmsDAO sd = new SmsDAO(this.getContext());
		Sms_Record sr = new Sms_Record(1,"wer",2,20120626);
		smss.add(sr);
		sd.add(smss);
		List<Sms_Record> qq = sd.getData(1, 20120625);
		Log.i(TAG,String.valueOf(qq.size()));
	}
	
	public void testCallAdd()
	{
		ArrayList<Call_Record> calls = new ArrayList<Call_Record>();
		CallDAO cd = new CallDAO(this.getContext());
		calls.add(new Call_Record(2,"WP",1222,2,20120625));
		cd.add(calls);
		List<Call_Record> pp = cd.getData(2, 20120624);
		
		Log.i(TAG,pp.get(0).toString());
		
	}
	
	public void testTarget()
	{
		TargetDAO td = new TargetDAO(this.getContext());
		td.update(0, 0, 1);
		Log.i(TAG,String.valueOf(td.gettime()));
		Log.i(TAG,String.valueOf(td.getetime()));
	}
	
	public void testgetsms()
	{
		ArrayList<Contact> ctt = new ArrayList<Contact>();
		ctt.add(new Contact(1,"Www","1213"));
		SmsDAO sd = new SmsDAO(this.getContext());
		Log.i(TAG,String.valueOf(sd.getMDatacount()));
		Log.i(TAG,String.valueOf(sd.getMData(ctt).get(0).get_count()));
		
	}
	
	public void testgetrank()
	{
		ArrayList<Rank>  rank;
		ContactDAO ctd = new ContactDAO(this.getContext());
		rank = ctd.getARank();
		if(rank!=null)
			Log.i(TAG,rank.get(0).toString());
	}
	
	public void testcallgetadetaildata()
	{
		ArrayList<Call_Record>  call;
		CallDAO cd = new CallDAO(this.getContext());
		call = cd.getADetailData(1);
		for(int i =0;i<call.size();i++)
			Log.i("201277",call.get(i).toString());
	}
	
	public void testsmsgetadetaildata()
	{
		ArrayList<Sms_Record>  sms;
		SmsDAO cd = new SmsDAO(this.getContext());
		sms = cd.getADetailData(1);
		for(int i =0;i<sms.size();i++)
			Log.i("201277",sms.get(i).toString());
	}
}
