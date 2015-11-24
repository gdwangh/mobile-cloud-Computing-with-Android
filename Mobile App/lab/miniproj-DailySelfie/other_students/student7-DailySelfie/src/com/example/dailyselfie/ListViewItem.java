package com.example.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ListViewItem
{
	String mn;
	Drawable mi;
	Bitmap bm;
	
	public ListViewItem(String name, Bitmap b, Drawable d)
	{
		mn = name;
		mi = d;
		bm = b;
	}
	public String getName()
	{
		return mn;
	}
	public Bitmap getBitmap()
	{
		return bm;
	}
	public Drawable getIcon()
	{
		return mi;
	}
}
