package com.suntengfei.contactrank;

import java.util.ArrayList;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.suntengfei.contactrank.dao.CallDAO;
import com.suntengfei.contactrank.dao.SmsDAO;
import com.sutnengfei.contactrank.model.Call_Record;
import com.sutnengfei.contactrank.model.Sms_Record;

public class SingleDetail extends Activity
{

	private CallDAO ctd=new CallDAO(this);;
	private SmsDAO ssd = new SmsDAO(this);;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single);

		Intent i = getIntent();
		String name = i.getStringExtra("name");
		int cid = i.getIntExtra("cid", 0);
		int target = i.getIntExtra("target", 0);
		int point;
		if(target==0)
			point = i.getIntExtra("mpoint", 0);
		else
			point = i.getIntExtra("apoint", 0);
		
		Log.i("201271",String.valueOf(cid));
		Log.i("201271",String.valueOf(point));
		fillDetail(cid,target,point,name);
	}
	
	public void fillDetail(int cid,int target,int point,String name)
	{
		Call_Record crd = ctd.getSData(cid, target);
		Sms_Record srd = ssd.getSData(cid, target);
		TextView textViewT = (TextView)findViewById(R.id.textViewT);
		TextView textViewR = (TextView)findViewById(R.id.textViewR);
		TextView textView2 = (TextView)findViewById(R.id.textView2);//name
		TextView textView4 = (TextView)findViewById(R.id.textView4);//point
		TextView textView9 = (TextView)findViewById(R.id.textView9);//sms
		TextView textView10 = (TextView)findViewById(R.id.textView10);//call count
		TextView textView19 = (TextView)findViewById(R.id.textView19);//call duration
		RatingBar ratingbar1 = (RatingBar)findViewById(R.id.ratingBar1);//ratingbar
		
		LinearLayout llt = (LinearLayout)findViewById(R.id.linearLayoutSL);
		if(target==0)
		{
			textViewT.setText("��ͳ�Ƽ�¼");		
			textViewR.setTag("�µ÷֣�");
			llt.setBackgroundColor(0xFF555500);
		}
		else
		{
			textViewT.setText("��ͳ�Ƽ�¼");		
			textViewR.setText("�ܵ÷֣�");
			llt.setBackgroundColor(0xFF333300);
		}
		textView2.setText(name);
		textView4.setText(String.valueOf(point));
		textView9.setText(String.valueOf(srd.get_count()));
		textView10.setText(String.valueOf(crd.get_count()));
		
		int duration = (int)crd.get_duration();
		String sd="";
		if(duration/3600 !=0)
			sd+=String.valueOf((duration/3600))+"h";
		if((duration%3600)/60!=0)
			sd+=String.valueOf((duration%3600)/60)+"m";
		sd+=String.valueOf(duration%60)+"s";
		textView19.setText(sd);
		Date date = new Date();
		float rating = point/(float)(date.getDate()*2);
		if(target==1)
			rating = point/(float)100.0;
		if(rating>0&&rating<0.5)
			rating = (float)0.5;
		ratingbar1.setRating(rating);	
		// 1, ������ʾ����Ⱦͼ
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 2,������ʾ
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// 2.1, ������״ͼ����
		
		int time = ((date.getYear()+1900)*10000+(date.getMonth()+1)*100);
		
		ArrayList<Call_Record> acrds; 
		ArrayList<Sms_Record> asrds; 
		if(target==0)
		{
			asrds= ssd.getData(cid, time);
			acrds= ctd.getData(cid, time);
		}
		else
		{
			asrds = ssd.getADetailData(cid);
			acrds = ctd.getADetailData(cid);
		}
		CategorySeries series1 = new CategorySeries("ͨ����");
		CategorySeries series2 = new CategorySeries("������");
		
		
		//�������  �� �ҳ����count
		int j=0,k=0,mcount=0,yearmonth=0;
		if(target==0)
		{
			for(int i = 1;i<=date.getDate();i++)
			{
				if(j<acrds.size())	
				{
					if((int)(acrds.get(j).get_time())%100==i)
					{
						series1.add(acrds.get(j).get_count());
						if(acrds.get(j).get_count()>mcount)
							mcount = acrds.get(j).get_count();
						j++;
					}
					else
						series1.add(0);
				}
				else
					series1.add(0);
				if(k<asrds.size())
				{	
					if((int)(asrds.get(k).get_time())%100==i)
					{
						series2.add(asrds.get(k).get_count());
						if(asrds.get(k).get_count()>mcount)
							mcount = asrds.get(k).get_count();
						k++;
					}
					else
						series2.add(0);
				}
				else
					series2.add(0);
			}
		}
		else
		{
			if(asrds.size()>0)
				yearmonth = (int)asrds.get(asrds.size()-1).get_time();
			if(acrds.size()>0)
			{
				if(yearmonth!=0)
				yearmonth = yearmonth<(int)acrds.get(acrds.size()-1).get_time()?
						yearmonth:(int)acrds.get(acrds.size()-1).get_time();
				else
					yearmonth = (int)acrds.get(acrds.size()-1).get_time();
			}
					 
			for(int i=time/100%100,y = time/10000;y*100+i>=yearmonth;i--)
			{
				if(i==0)
				{
					i = 12;
					y--;
				}
				if(j<acrds.size())	
				{
					if((int)(acrds.get(j).get_time())==i+y*100)
					{
						series1.add(acrds.get(j).get_count());
						if(acrds.get(j).get_count()>mcount)
							mcount = acrds.get(j).get_count();
						j++;
					}
					else
						series1.add(0);
				}
				else
					series1.add(0);
				if(k<asrds.size())
				{	
					if((int)(asrds.get(k).get_time())==i+y*100)
					{
						series2.add(asrds.get(k).get_count());
						if(asrds.get(k).get_count()>mcount)
							mcount = asrds.get(k).get_count();
						k++;
					}
					else
						series2.add(0);
				}
				else
					series2.add(0);
			}
		}
		
		dataset.addSeries(series1.toXYSeries());
		dataset.addSeries(series2.toXYSeries());
		
		/*for (int i = 0; i < 2; i++) {
			// ע��,�����������XYSeries ��һ�㲻ͬ!!����ʹ��CategroySeries
			CategorySeries series = new CategorySeries("test" + (i + 1));
			// �������
			for (int k = 0; k < 10; k++) {
				// ֱ��������Ҫ��ʾ������,��:Y���ֵ
				series.add(Math.abs(20 + r.nextInt() % 100));
			}
			// ����Ҫ����ת��
			dataset.addSeries(series.toXYSeries());
		}*/
				// 3, �Ե�Ļ��ƽ�������
		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
				// 3.1������ɫ
		xyRenderer.setColor(Color.BLUE);

		// 3.2���õ����ʽ
		// xyRenderer.setPointStyle(PointStyle.SQUARE);
		// 3.3, ��Ҫ���Ƶĵ���ӵ����������
		renderer.addSeriesRenderer(xyRenderer);
		// 3.4,�ظ� 3.1~3.3�Ĳ�����Ƶڶ���ϵ�е�
		xyRenderer = new XYSeriesRenderer();
		xyRenderer.setColor(Color.RED);
		// xyRenderer.setPointStyle(PointStyle.CIRCLE);
		renderer.addSeriesRenderer(xyRenderer);
		// ע������x,y min ��Ҫ��ͬ
		// ������һ�����õ�����x,y��Χ�ķ���
		//˳����:minX, maxX, minY, maxY
			mcount = mcount/10*10+(mcount/10>10?mcount/10:10);
		double[] range = { 0, 10, 0, mcount };
		renderer.setRange(range);
		// �ȼ���:
		// -------------------
		// renderer.setXAxisMin(0);
		// renderer.setXAxisMax(10);
		// renderer.setYAxisMin(1);
		// renderer.setYAxisMax(200);
		// -------------------
		

		// ���ú��ʵĿ̶�,��������ʾ�������� MAX / labels
		renderer.setXLabels(10);
		renderer.setYLabels(10);

		// ����x,y����ʾ������,Ĭ���� Align.CENTER
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);

		// ����������,�����ɫ
		renderer.setAxesColor(Color.RED);
		// ��ʾ����
		renderer.setShowGrid(true);
		// ����x,y���ϵĿ̶ȵ���ɫ
		renderer.setLabelsColor(Color.BLACK);
		//���������϶�
		renderer.setPanEnabled(true, false);
		if(target==0)
			renderer.setChartTitle("��ͳ����״ͼ");
		else
			renderer.setChartTitle("��ͳ����״ͼ");
		// ����ҳ�߿հ׵���ɫ
		renderer.setMarginsColor(Color.DKGRAY);
		// �����Ƿ���ʾ,���������,Ĭ��Ϊ true
		renderer.setShowAxes(true);
		
		
		renderer.setXLabelsAngle(-25);
		renderer.setXLabels(0);
		if(target==0)
		{
			for(int i =1;i<=date.getDate();i++)
				renderer.addXTextLabel(i, String.valueOf(i)+"��");
		}
		else
		{
			for(int i=time/100%100,q=1,y = time/10000;y*100+i>=yearmonth;i--,q++)
			{
				if(i==0)
				{
					i = 12;
					y--;
				}
				renderer.addXTextLabel(q, String.valueOf(y)+"��"+String.valueOf(i%100)+"��");
			}
		}
		
		// ��������ͼ֮��ľ���
		renderer.setBarSpacing(0.1);
		int length = renderer.getSeriesRendererCount();

		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
			// ��֪�����ߵľ�������ô�����,Ĭ����Align.CENTER,���Ƕ����������ϵ�������ʾ
			// �ͻ��������ұ�
			ssr.setChartValuesTextAlign(Align.RIGHT);
			ssr.setChartValuesTextSize(12);
			ssr.setDisplayChartValues(true);
		}
		// Intent intent = new LinChart().execute(this);
		// Intent intent = ChartFactory
		// .getBarChartIntent(this, dataset, renderer, Type.DEFAULT);
		// startActivity(intent);
		
		LinearLayout barchart = (LinearLayout) findViewById(R.id.barchart);
		GraphicalView mChartView = ChartFactory.getBarChartView(this, dataset,
				renderer, Type.DEFAULT);

		barchart.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}
	
	
}


















