package com.example.dailyselfie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	private Context mcontext;
	private static String TAG = "Main";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private ListView mlist;
	private ListViewAdapter<ListViewItem> mAdapter;
	ArrayList<String> listItems=new ArrayList<String>();
	ArrayAdapter<String> aadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mcontext = this.getBaseContext();

		mlist = (ListView)this.findViewById(R.id.list);
		mAdapter = new ListViewAdapter<ListViewItem>(this);
		mlist.setAdapter(mAdapter);

		mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i(TAG, "click " + arg2 + " " + arg3);
				ListViewItem item = mAdapter.getItem(arg2);
				//Intent inf=new Intent(MainActivity.this,Activityfullscreen.class);
				/*inf.putExtra("data", item.getName());
		        startActivity(inf);
				 */
				loadPhoto(item.getName(), 400,400);
			}

		});

		//aadapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		//mlist.setAdapter(aadapter);

		getActionBar().setIcon(R.drawable.ic_photo_camera_white_24dp);
		loadList();
	}



	private void setAlarm()
	{
		Intent intent = new Intent(this, NotificationLauncher.class);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		alarmMgr = (AlarmManager)mcontext.getSystemService(Context.ALARM_SERVICE);

		//  TODO switch back to alarm repeating and 2 minutes.
		alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 
				//  1000 * 60 * 2, alarmIntent);
				1000 * 60 * 2,  
				alarmIntent);
		Log.i(TAG, "alarm set");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		//  Start the alarm.
		//  TODO set in on create.
		Log.i(TAG, "on resume");
		setAlarm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_photo:
			Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Starting camera.");
			dispatchTakePictureIntent();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	private void loadPhoto(String n, int width, int height)
	{
		ImageView image = new ImageView(this);
		image.setImageBitmap(BitmapFactory.decodeFile(getFilesDir().toString() + "/" + n));
		//ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		//LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		//View layout = inflater.inflate(R.layout.custom_fullimage_dialog, (ViewGroup) findViewById(R.id.layout_root));
		//ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		//image.setImageDrawable(drawable);
		//imageDialog.setView(layout);
		View mv = View.inflate(this,  R.layout.custom_fullimage_dialog, null);
		image = (ImageView)mv.findViewById(R.id.fullimage);
		
		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this)
			.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setView(mv);
		
		image.setImageBitmap(BitmapFactory.decodeFile(getFilesDir().toString() + "/" + n));
        //  Show the dialog.
        imageDialog.create().show();
	}

	private void saveFile(String name, Bitmap b)
	{
		try{
			Log.i(TAG, getFilesDir().toString());
			FileOutputStream fos = new FileOutputStream(getFilesDir().toString() + "/" + name);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
			fos.write(byteStream.toByteArray());
			fos.close();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void loadList()
	{
		String s[] = getFilesDir().list();
		for (String fn : s)
		{
			Log.i(TAG, fn);
			Bitmap b = BitmapFactory.decodeFile(getFilesDir().toString() + "/" + fn);
			ListViewItem i = new ListViewItem(fn, b, new BitmapDrawable(b));
			mAdapter.add(i);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
		{
			Log.i(TAG, "Received image");
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			String name = System.currentTimeMillis() + "";
			saveFile(name, imageBitmap);
			ListViewItem item = new ListViewItem(name, imageBitmap, new BitmapDrawable(imageBitmap));
			mAdapter.add(item);
			mAdapter.notifyDataSetChanged();
			//mImageView.setImageBitmap(imageBitmap);
			//aadapter.add(name);
		}
		else
		{
			Log.i(TAG, "None");
		}
	}

	@Override
	public void onClick(View arg0) {
		Log.i(TAG, "click!");
		Intent inf=new Intent(MainActivity.this,Activityfullscreen.class);

		startActivity(inf);

	}

}
