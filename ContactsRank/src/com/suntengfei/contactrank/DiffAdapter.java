package com.suntengfei.contactrank;

import com.suntengfei.contactrank.widget.TitleProvider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DiffAdapter extends BaseAdapter implements TitleProvider
{
	private static final int VIEW1 = 0;
    private static final int VIEW2 = 1;
    private static final int VIEW_MAX_COUNT = VIEW2 + 1;
	private final String[] names = {"月评分","总评分"};
	private LayoutInflater mInflater;
	
	public DiffAdapter(Context context)
	{
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
    public int getItemViewType(int position) {
        return position;
    }

    public int getViewTypeCount() {
        return VIEW_MAX_COUNT;
    }

    public int getCount() {
        return 2;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int view = getItemViewType(position);
        if (convertView == null) {
            switch (view) {
                case VIEW1:
                    convertView = mInflater.inflate(R.layout.diff_view1, null);
                    break;
                case VIEW2:
                    convertView = mInflater.inflate(R.layout.diff_view2, null);
                    break;
            }
        }
        return convertView;
    }



    /* (non-Javadoc)
	 * @see org.taptwo.android.widget.TitleProvider#getTitle(int)
	 */
	public String getTitle(int position) {
		return names[position];
	}

}
