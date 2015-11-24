package com.example.dailyselfie;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoAdapter extends BaseAdapter {

	private List<Uri> list = new ArrayList<Uri>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public PhotoAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(Uri location) {
		list.add(location);
		notifyDataSetChanged();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView = convertView;
		ViewHolder holder;

		Uri currentUri = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater
					.inflate(R.layout.activity_main, parent, false);
			holder.photo = (ImageView) newView.findViewById(R.id.thumb_image);
			holder.name = (TextView) newView.findViewById(R.id.image_name);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}

		setPic(holder.photo, currentUri);
		holder.photo.setTag(currentUri);
		holder.name.setText(currentUri.getLastPathSegment());

		return newView;
	}

	static class ViewHolder {
		ImageView photo;
		TextView name;
	}
	
	private void setPic(ImageView mImageView, Uri mCurrentPhotoUri) {
	    // Get the dimensions of the View
	    int targetW = 160;
	    int targetH = 120;

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), bmOptions);
	    mImageView.setImageBitmap(bitmap);
	}
}
