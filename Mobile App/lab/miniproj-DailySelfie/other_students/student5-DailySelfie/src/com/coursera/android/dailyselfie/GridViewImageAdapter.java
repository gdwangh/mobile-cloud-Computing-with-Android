package com.coursera.android.dailyselfie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GridViewImageAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<String> _filePaths = new ArrayList<String>();
	private int imageWidth;
	boolean isImageFitToScreen;
	Intent viewPicIntent;
	private static final String TAG = "SelfieBrowser";
	Utils utils;

	public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths, int imageWidth, Utils utils) {
		this._activity = activity;
		this._filePaths = filePaths;
		this.imageWidth = imageWidth;
		this.utils = utils;

		viewPicIntent = new Intent();
		viewPicIntent.setAction(android.content.Intent.ACTION_VIEW);
		viewPicIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_DEBUG_LOG_RESOLUTION
				| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);

	}

	@Override
	public int getCount() {
		return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setFilePaths(ArrayList<String> filePaths) {
		this._filePaths = filePaths;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
			imageView = new ImageView(_activity);

		//_filePaths = utils.getFilePaths();
		// get screen dimensions
		Bitmap image = decodeFile(_filePaths.get(position), imageWidth, imageWidth);

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
		imageView.setImageBitmap(image);

		// image view click listener
		imageView.setOnClickListener(new OnImageClickListener(position));

		return imageView;
	}

	class OnImageClickListener implements OnClickListener {

		int _postion;

		// constructor
		public OnImageClickListener(int position) {
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			viewPicIntent.setDataAndType(Uri.fromFile(new File(_filePaths.get(_postion))), "image/jpg");
			_activity.startActivity(viewPicIntent);
		}

	}

	/*
	 * Resizing image size
	 */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
			bmpOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, bmpOptions);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (bmpOptions.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& bmpOptions.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
