package com.suntengfei.contactrank;

import java.util.ArrayList;
import java.util.Date;

import com.suntengfei.contactrank.widget.*;

import com.suntengfei.contactrank.dao.*;

import com.sutnengfei.contactrank.model.Call_Record;
import com.sutnengfei.contactrank.model.Contact;
import com.sutnengfei.contactrank.model.Rank;
import com.sutnengfei.contactrank.model.Sms_Record;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class ContactsRankActivity extends Activity {
    /** Called when the activity is first created. */
    ListView mListView = null;
    TextView tv;
    private CallDAO calldao;
    private SmsDAO smsdao;
    private ContactDAO contactdao;
    private TargetDAO targetdao;
    
    Call_Records crd ;
    Sms_Records srd ;
    Contacts cts ;
    
    private ArrayList<Sms_Record> asrd;
    private ArrayList<Call_Record> acrd;
    private ArrayList<Rank> amrk;
    private ArrayList<Rank> aark;
    
    private ArrayList<Rank> monthrank;
    private ArrayList<Rank> allrank;
    private ViewFlow viewFlow;
	private ListView listView;
	private ListView listView2;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        calldao = new CallDAO(this);
        smsdao = new SmsDAO(this);
        contactdao = new ContactDAO(this);
        targetdao = new TargetDAO(this);
        
        crd = new Call_Records(this);
        srd = new Sms_Records(this);
        cts = new Contacts(this);
        
        acrd =crd.refresh_Call_Records();
        asrd = srd.refresh_Sms_Records();
        tv = (TextView) findViewById(R.id.textView1);
        cts.getPhoneContacts();
       /* tv.setText(String.valueOf(cts.getPhoneContacts().size())+"    "+String.valueOf(acrd.size())
        		+"      "+String.valueOf(asrd.size()));*/
        
        cuContacts();
        
        addAllMsg(acrd,asrd);
        
        
        for(int i = 0;i<acrd.size();i++)
        	Log.i("newcall",acrd.get(i).toString());
        
        for(int i = 0;i<asrd.size();i++)
        	Log.i("newsms",asrd.get(i).toString());
        
        Date date = new Date();
        
        Log.i("201271","month"+String.valueOf(date.getMonth()+1));
        Log.i("201271","month"+String.valueOf((int)((targetdao.gettime()/100)%100)));
        if((date.getMonth()+1)!=(int)((targetdao.gettime()/100)%100))
        {
        	aark = makeARank(new Contacts(this).getPhoneContacts());
        	freshRank(aark,1);
        }
        else{
        	aark = makeMRank(new Contacts(this).getPhoneContacts(),0);
        	freshRank(aark,0);
        }
        
        for(int i = 0;i<aark.size();i++)
        	Log.i("newtest",aark.get(i).toString());
        
        
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
        DiffAdapter adapter = new DiffAdapter(this);
        viewFlow.setAdapter(adapter);
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
		
		//** To populate ListView in diff_view1.xml *//*
		listView = (ListView) findViewById(R.id.listView1);
		monthrank = contactdao.getMRank();
        listView.setAdapter(new ArrayAdapter<Rank>(this,
				android.R.layout.simple_list_item_1, monthrank));
        listView.setOnItemClickListener(itemlistener1);
        
        listView2 = (ListView) findViewById(R.id.listView2);
        allrank = contactdao.getARank();
        listView2.setAdapter(new ArrayAdapter<Rank>(this,
				android.R.layout.simple_list_item_1, allrank));
        listView2.setOnItemClickListener(itemlistener2);
        
		targetdao.update(makeDate(date), date.getTime(), 1);
    }
	
    /**
     * 同步通讯录
     */
	public void cuContacts()
	{
		Contacts cts = new Contacts(this);
		ArrayList<Contact> cc = cts.getPhoneContacts();
		for(int i = 0;i<cc.size();i++)
		{
			if(contactdao.search(cc.get(i).get_cid())==0)
			{
				contactdao.add(cc.get(i).get_cid(), cc.get(i).get_name());
			}
		}
	}
	/**
	 * 将新短信 通话记录分别插入sms call表中
	 * @param crd
	 * @param srd
	 * @return
	 */
	public int addAllMsg(ArrayList<Call_Record> crd,ArrayList<Sms_Record> srd)
	{
		calldao.add(crd);
		smsdao.add(srd);
		return 0;
	}
	
	
	//为用户一个月评分
	//裁剪掉无更新的好友
	public ArrayList<Rank> makeMRankNew(ArrayList<Contact> ctt,int target)
	{
		ArrayList<Rank> rank = makeMRank(ctt,target);
		
		for(int i = 0;i<rank.size();i++)
		{
			if(rank.get(i).get_mpoint()==0)
			{
				rank.remove(i);
				i--;
			}
		}
		return rank;
	}
	
	
	/**
	 * 为用户一个月评分||为用户总评分
	 * @param ctt
	 * @param target 0时为月评分   1时为总评分
	 * @return
	 */
	public ArrayList<Rank> makeMRank(ArrayList<Contact> ctt,int target)
	{
		int point = 0;
		ArrayList<Rank> ranks = new ArrayList<Rank>();
		ArrayList<Call_Record> crd;
		ArrayList<Sms_Record> srd;
		if(target==0)
		{
			srd = smsdao.getMData(ctt);
			crd = calldao.getMData(ctt);
		}
		else
		{
			srd = smsdao.getAData(ctt);
			crd = calldao.getAData(ctt);
		}
		
		Log.i("calltest",String.valueOf(crd.size()));
		Log.i("smstest",String.valueOf(srd.size()));
		
		Log.i("newsms","一个月的短信数："+String.valueOf(srd.size()));
		for(int i = 0;i<crd.size();i++)
		{
			if(crd.get(i).get_count()!=0)
				point = (int) (crd.get(i).get_count()*2+(crd.get(i).get_duration()/60)+1);
			else
				point = 0;
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
					int point73 = ranks.get(k).get_mpoint()+srd.get(j).get_count();
					ranks.get(k).set_mpoint(point73);
					ranks.get(k).set_apoint(point73);
					Log.i("newsms","加分"+String.valueOf(srd.get(j).get_count()));
					Log.i("newsms","sms 评分:"+String.valueOf(ranks.get(k).get_mpoint()));
					Log.i("201273",ranks.get(k).toString());
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
	
	
	public ArrayList<Rank> makeARank(ArrayList<Contact> ctt)
	{
		ArrayList<Rank> mr;
		ArrayList<Rank> ar;
		mr = makeMRank(ctt,0);
		ar = makeMRank(ctt,1);
		if(mr.size()!=ar.size())
		{
			Log.i("201271","很不幸。。。");
			Log.i("201271","ar"+String.valueOf(ar.size()));
			Log.i("201271","mr"+String.valueOf(mr.size()));
			
			return new ArrayList<Rank>();
		}
		
		for(int i = 0;i<ar.size();i++)
		{
			ar.get(i).set_apoint(ar.get(i).get_apoint()-mr.get(i).get_apoint());
			ar.get(i).set_mpoint(mr.get(i).get_mpoint());
			Log.i("201271",ar.get(i).toString());
		}
		for(int i = 0;i<ar.size();i++)
		{
			if(ar.get(i).get_apoint()==0&&ar.get(i).get_mpoint()==0)
			{
				ar.remove(i);
				i--;
			}
		}
		
		return ar;
	}
	
	/**
	 * 将更新的数据插入 rank表中
	 * @param aaak
	 * @param target 0时只更新月分数  1时更新月分数和总分数
	 */
	public void freshRank(ArrayList<Rank> aaak,int target)
	{
		if(target==0)
			contactdao.updateM(aaak);
		else
			contactdao.updateA(aaak);
	}
	
	
	
	public long makeDate(Date date)
	{
		return (long)((date.getYear()+1900)*10000+(date.getMonth()+1)*100+date.getDate());
	}
    
    private OnItemClickListener itemlistener1 = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// TODO Auto-generated method stub
			int iid = monthrank.get(position).get_cid();
			int mpt = monthrank.get(position).get_mpoint();
			String name = monthrank.get(position).get_name();
			
			Log.i("201271",String.valueOf(iid));
			Log.i("201271",String.valueOf(mpt));
			
			Intent i = new Intent(ContactsRankActivity.this,SingleDetail.class);
			i.putExtra("cid", iid);
			i.putExtra("target", 0);//0表示要获取月数据    1获取总数据
			i.putExtra("mpoint", mpt);
			i.putExtra("name", name);
			startActivity(i);
		}
	};
	
	
    private OnItemClickListener itemlistener2 = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// TODO Auto-generated method stub
			int iid = allrank.get(position).get_cid();
			int apt = allrank.get(position).get_apoint();
			String name = allrank.get(position).get_name();
			
			Log.i("201271",String.valueOf(iid));
			Log.i("201271",String.valueOf(apt));
			
			Intent i = new Intent(ContactsRankActivity.this,SingleDetail.class);
			i.putExtra("cid", iid);
			i.putExtra("target", 1);//0表示要获取月数据    1获取总数据
			i.putExtra("apoint", apt);
			i.putExtra("name", name);
			startActivity(i);
		}
	};
}