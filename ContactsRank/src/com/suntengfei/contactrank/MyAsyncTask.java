package com.suntengfei.contactrank;

import java.util.ArrayList;
import java.util.Date;

import com.suntengfei.contactrank.dao.CallDAO;
import com.suntengfei.contactrank.dao.ContactDAO;
import com.suntengfei.contactrank.dao.SmsDAO;
import com.suntengfei.contactrank.dao.TargetDAO;
import com.suntengfei.contactrank.widget.TitleFlowIndicator;
import com.suntengfei.contactrank.widget.ViewFlow;
import com.sutnengfei.contactrank.model.Call_Record;
import com.sutnengfei.contactrank.model.Contact;
import com.sutnengfei.contactrank.model.Rank;
import com.sutnengfei.contactrank.model.Sms_Record;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyAsyncTask extends AsyncTask<Integer, Integer, List_Result>
{
	ListView mListView = null;
    //TextView tv;
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
    
    private ViewFlow viewFlow;
	private ListView listView;
	private ListView listView2;

	private OnItemClickListener ol1;
	private OnItemClickListener ol2;
	private ContactsRankActivity myac;
	
	private ArrayList<Rank> monthrank;
    private ArrayList<Rank> allrank;
	
	public MyAsyncTask(ContactsRankActivity mm)
	{
		myac = mm;
		calldao = new CallDAO(mm);
        smsdao = new SmsDAO(mm);
        contactdao = new ContactDAO(mm);
        targetdao = new TargetDAO(mm);
        
        crd = new Call_Records(mm);
        srd = new Sms_Records(mm);
        cts = new Contacts(mm);
        //listView = (ListView) mm.findViewById(R.id.listView1);
        //listView2 = (ListView) mm.findViewById(R.id.listView2);
        viewFlow = (ViewFlow) mm.findViewById(R.id.viewflow);
	}
	@Override
	protected List_Result doInBackground(Integer... params)
	{
		cuContacts();
		acrd =crd.refresh_Call_Records();
        asrd = srd.refresh_Sms_Records();
        addAllMsg(acrd,asrd);
        Date date = new Date();

        if((date.getMonth()+1)!=(int)((targetdao.gettime()/100)%100))
        {
        	aark = makeARank(new Contacts(myac).getPhoneContactsS());
        	freshRank(aark,1);
        }
        else{
        	aark = makeMRank(new Contacts(myac).getPhoneContactsS(),0);
        	freshRank(aark,0);
        }
        targetdao.update(makeDate(date), date.getTime(), 1);
		return new List_Result(contactdao.getMRank(),contactdao.getARank());
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		myac.setContentView(R.layout.splash_screen2);
		TextView tvs = (TextView)myac.findViewById(R.id.textViewSS);
		TargetDAO tds = new TargetDAO(myac);
		long etime = tds.getetime();
		if(etime!=0)
		{
			Date dates = new Date();
			dates.setTime(etime);
			String sptext = String.valueOf(dates.getYear()+1900)+"年"+String.valueOf(dates.getMonth()+1)
					        +"月"+String.valueOf(dates.getDate())+"日"+String.valueOf(dates.getHours())
					        +"时"+String.valueOf(dates.getMinutes())+"分"+String.valueOf(dates.getSeconds())+"秒";
			tvs.setText(sptext);
		}
		SystemClock.sleep(500);
	}

	@Override
	protected void onPostExecute(List_Result result)
	{
		super.onPostExecute(result);
		
		myac.setContentView(R.layout.main);
		viewFlow = (ViewFlow) myac.findViewById(R.id.viewflow);
        DiffAdapter adapter = new DiffAdapter(myac);
        viewFlow.setAdapter(adapter);
		TitleFlowIndicator indicator = (TitleFlowIndicator) myac.findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);

		monthrank = result.getMrank();
		allrank = result.getArank();
		
		listView = (ListView) myac.findViewById(R.id.listView1);
        listView2 = (ListView) myac.findViewById(R.id.listView2);
        listView.setAdapter(new ArrayAdapter<Rank>(myac,
				R.layout.list_item1, result.getMrank()));
        //listView.setOnItemClickListener(ol1);
        
        listView2.setAdapter(new ArrayAdapter<Rank>(myac,
				R.layout.list_item2, result.getArank()));
        //listView2.setOnItemClickListener(ol2);
        
		//targetdao.update(makeDate(date), date.getTime(), 1);
		//一会写在异步方法里
         ol1 = new OnItemClickListener(){

    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position,
    				long id)
    		{
    			// TODO Auto-generated method stub
    			int iid = monthrank.get(position).get_cid();
    			int mpt = monthrank.get(position).get_mpoint();
    			String name = monthrank.get(position).get_name();
    			
//    			Log.i("201271",String.valueOf(iid));
//    			Log.i("201271",String.valueOf(mpt));
    			
    			Intent i = new Intent(myac,SingleDetail.class);
    			i.putExtra("cid", iid);
    			i.putExtra("target", 0);//0表示要获取月数据    1获取总数据
    			i.putExtra("mpoint", mpt);
    			i.putExtra("name", name);
    			myac.startActivity(i);
    		}
    	};
    	
    	
         ol2 = new OnItemClickListener(){

    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position,
    				long id)
    		{
    			int iid = allrank.get(position).get_cid();
    			int apt = allrank.get(position).get_apoint();
    			String name = allrank.get(position).get_name();

    			
    			Intent i = new Intent(myac,SingleDetail.class);
    			i.putExtra("cid", iid);
    			i.putExtra("target", 1);//0表示要获取月数据    1获取总数据
    			i.putExtra("apoint", apt);
    			i.putExtra("name", name);
    			myac.startActivity(i);
    		}
    	};
    	listView.setOnItemClickListener(ol1);
    	listView2.setOnItemClickListener(ol2);
    	myac.setIsload(true);
	}

	@Override
	protected void onProgressUpdate(Integer... values)
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	
	
	/**
     * 同步通讯录
     */
	public void cuContacts()
	{
		Contacts cts = new Contacts(myac);
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
		
//		Log.i("calltest",String.valueOf(crd.size()));
//		Log.i("smstest",String.valueOf(srd.size()));
		
		Log.i("newsms","一个月的短信数："+String.valueOf(srd.size()));
		for(int i = 0;i<crd.size();i++)
		{
			if(crd.get(i).get_count()!=0)
				point = (int) (crd.get(i).get_count()*2+(crd.get(i).get_duration()/60)+1);
			else
				point = 0;
//			Log.i("newcall","count:"+String.valueOf(crd.get(i).get_count())+"*2  duration:"+String.valueOf(crd.get(i).get_duration()));
//			Log.i("newcall",crd.get(i).toString());
			ranks.add(new Rank(crd.get(i).get_cid(),crd.get(i).get_name(),0,point,point,1));
		}
		
		for(int j=0;j<srd.size();j++)
		{
			int k;
			for( k=0;k<ranks.size();k++)
			{
				if(srd.get(j).get_cid()==ranks.get(k).get_cid())
				{
					int point73 = ranks.get(k).get_mpoint()+srd.get(j).get_count();
					ranks.get(k).set_mpoint(point73);
					ranks.get(k).set_apoint(point73);
//					Log.i("newsms","加分"+String.valueOf(srd.get(j).get_count()));
//					Log.i("newsms","sms 评分:"+String.valueOf(ranks.get(k).get_mpoint()));
//					Log.i("201273",ranks.get(k).toString());
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
//			Log.i("201271","很不幸。。。");
//			Log.i("201271","ar"+String.valueOf(ar.size()));
//			Log.i("201271","mr"+String.valueOf(mr.size()));
//			
			return new ArrayList<Rank>();
		}
		
		for(int i = 0;i<ar.size();i++)
		{
			ar.get(i).set_apoint(ar.get(i).get_apoint()-mr.get(i).get_apoint());
			ar.get(i).set_mpoint(mr.get(i).get_mpoint());
//			Log.i("201271",ar.get(i).toString());
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
//		for(int i = 0;i<aaak.size();i++)
//			Log.i("201275",aaak.get(i).toString());
		
		if(target==0)
			contactdao.updateM(aaak);
		else
			contactdao.updateA(aaak);
	}
	
	
	
	public long makeDate(Date date)
	{
		return (long)((date.getYear()+1900)*10000+(date.getMonth()+1)*100+date.getDate());
	}
	
}
