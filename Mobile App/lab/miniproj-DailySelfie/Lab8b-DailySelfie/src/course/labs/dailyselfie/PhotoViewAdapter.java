package course.labs.dailyselfie;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoViewAdapter extends BaseAdapter {
	
	private ArrayList<PhotoRecord> list = new ArrayList<PhotoRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private String TAG = "Lab-PhotoViewAdapter";
	
	public PhotoViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}
	
	public int getCount() {
		return list.size();
	}

	public PhotoRecord getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		PhotoRecord curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.photo_view, parent, false);
			holder.previewBitmap = (ImageView) newView.findViewById(R.id.preview);
			
			holder.file_name_tv = (TextView) newView.findViewById(R.id.photoname);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}
		
		Log.i(TAG, "imageView width="+holder.previewBitmap.getWidth());
		
		holder.previewBitmap.setImageBitmap(curr.getBitmap(holder.previewBitmap));
		holder.file_name_tv.setText(curr.getPhotoName());

		return newView;
	}

	static class ViewHolder {
		ImageView previewBitmap;
		TextView file_name_tv;
	}

	// add at end
	public void add(PhotoRecord listItem) {
		list.add(0, listItem);
		notifyDataSetChanged();
	}

	public void addTail(PhotoRecord listItem) {
		
		list.add(listItem);
		
		notifyDataSetChanged();
	}
	
	public ArrayList<PhotoRecord> getList() {
		return list;
	}

	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}
	
	public void deleteItem(int position) {
		list.remove(position);
		this.notifyDataSetChanged();
	}
}
