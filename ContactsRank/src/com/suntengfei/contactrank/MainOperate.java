package com.suntengfei.contactrank;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.suntengfei.contactrank.dao.CallDAO;
import com.suntengfei.contactrank.dao.ContactDAO;
import com.suntengfei.contactrank.dao.SmsDAO;
import com.suntengfei.contactrank.dao.TargetDAO;
import com.sutnengfei.contactrank.model.Call_Record;
import com.sutnengfei.contactrank.model.Contact;
import com.sutnengfei.contactrank.model.Rank;
import com.sutnengfei.contactrank.model.Sms_Record;

public class MainOperate
{
	private Context mContext;
    private CallDAO calldao;
    private SmsDAO smsdao;
    private ContactDAO contactdao;
    private TargetDAO targetdao;

	
	public MainOperate(Context mContext)
	{
        calldao = new CallDAO(mContext);
        smsdao = new SmsDAO(mContext);
        contactdao = new ContactDAO(mContext);
        targetdao = new TargetDAO(mContext);
	}
	
    /**
     * 同步通讯录
     */
	public void cuContacts()
	{
		Contacts cts = new Contacts(mContext);
		ArrayList<Contact> cc = cts.getPhoneContacts();
		for(int i = 0;i<cc.size();i++)
		{
			if(contactdao.search(cc.get(i).get_cid())==0)
			{
				contactdao.add(cc.get(i).get_cid(), cc.get(i).get_name());
			}
		}
	}
	
	public int addAllMsg(ArrayList<Call_Record> crd,ArrayList<Sms_Record> srd)
	{
		calldao.add(crd);
		smsdao.add(srd);
		return 0;
	}
	
	/**
	 * 为用户一个月评分
	 * @param ctt
	 * @return
	 */
	public ArrayList<Rank> makeMRank(ArrayList<Contact> ctt)
	{
		int point = 0;
		ArrayList<Rank> ranks = new ArrayList<Rank>();
		ArrayList<Call_Record> crd;
		ArrayList<Sms_Record> srd;
		srd = smsdao.getMData(ctt);
		crd = calldao.getMData(ctt);
		
		Log.i("calltest",crd.get(0).toString());
		Log.i("smstest",srd.get(0).toString());
		
		Log.i("newsms","一个月的短信数："+String.valueOf(srd.size()));
		for(int i = 0;i<crd.size();i++)
		{
			point = (int) (crd.get(i).get_count()*2+(crd.get(i).get_duration()/60)+1);
			Log.i("newcall","count:"+String.valueOf(crd.get(i).get_count())+"*2  duration:"+String.valueOf(crd.get(i).get_duration()));
			Log.i("newcall",String.valueOf(point));
			ranks.add(new Rank(crd.get(i).get_cid(),crd.get(i).get_name(),0,point,point,1));
		}
		
		for(int j=0;j<srd.size();j++)
		{
			int k;
			for( k=0;k<ranks.size();k++)
			{
				if(srd.get(j).get_cid()==ranks.get(k).get_cid())
				{
					Log.i("newsms","加分前"+String.valueOf(ranks.get(k).get_mpoint()));
					ranks.get(k).set_mpoint(ranks.get(k).get_mpoint()+srd.get(j).get_count());
					ranks.get(k).set_apoint(ranks.get(k).get_apoint()+srd.get(j).get_count());
					Log.i("newsms","加分"+String.valueOf(srd.get(j).get_count()));
					Log.i("newsms","sms 评分:"+String.valueOf(ranks.get(k).get_mpoint()));
					break;
				}
			}
			if(k>=ranks.size())
			{
				ranks.add(new Rank(srd.get(j).get_cid(),srd.get(j).get_name(),0,srd.get(j).get_count(),srd.get(j).get_count(),1));
			}
		}
		return ranks;
	}
	
	public void freshRankM(ArrayList<Rank> aaak)
	{
		contactdao.updateM(aaak);
	}
	
	
	
	public void freshRankA()
	{
		
	}
	
	
	public long makeDate(Date date)
	{
		return (long)((date.getYear()+1900)*10000+(date.getMonth()+1)*100+date.getDate());
	}
}
