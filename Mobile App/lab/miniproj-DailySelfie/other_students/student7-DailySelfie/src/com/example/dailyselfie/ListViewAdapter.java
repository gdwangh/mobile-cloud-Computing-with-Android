package com.example.dailyselfie;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewAdapter<T extends ListViewItem> extends BaseAdapter
{
	private static String TAG = "ListViewAdapter";
	private Context mContext = null;
	private ArrayList<T> mItems = null;
	
    public ListViewAdapter(Context context, List<T> items)
    {
        init(context, items);
    }
    public ListViewAdapter(Context context)
    {
        init(context, null);
    }
    
    private void init(Context context, List<T> items)
    {
    	mContext = context;
    	mItems = new ArrayList<T>();
    	
    	if (items == null)
    		return;
    	mItems.addAll(items);
    }
    
    protected Context getContext()
    {
    	return mContext;
    }
    protected void clear()
    {
    	mItems.clear();
    }
    protected void add(T item)
    {
    	mItems.add(item);
    }
    protected void addAll(T items[])
    {
    	if (items == null)
    		return;
    	
    	for (int i = 0; i < items.length; i++)
    	{
    		mItems.add(items[i]);
    	}
    }
    @Override
	public int getCount()
    {
		return mItems.size();
	}
	@Override
	public T getItem(int position)
	{
		return mItems.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
 
	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        
        if(convertView == null)
        {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view 
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        // update the item view
        T item = getItem(position);
        viewHolder.ivIcon.setImageDrawable(item.getIcon());
        //  Set icon character and color.
        String name = item.getName();
        //  Set text for name and description.
        viewHolder.tvTitle.setText(item.getName());
        
        return convertView;
    }
    
    
    
    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     * 
     * @see http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView ivIcon;
    	TextView tvTitle;
    }

}
