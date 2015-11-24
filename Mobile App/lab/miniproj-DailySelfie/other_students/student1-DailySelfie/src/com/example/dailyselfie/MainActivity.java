package com.example.dailyselfie;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	public static String TAG = MainActivity.class.getName(); 
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;	
	
	private Uri fileUri;
	private PhotoAdapter adapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		adapter = new PhotoAdapter(this);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageView thumbView = (ImageView)view.findViewById(R.id.thumb_image);
				Uri imageLocation = (Uri)thumbView.getTag();
				Intent intent = new Intent(MainActivity.this, ShowPicture.class);
				intent.setData(imageLocation);
				startActivity(intent);
				
			}
		});
		
		loadExistingImages();
		
		AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		Intent intent = new Intent(this, AlarmBroadcastReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				this, 0, intent, 0);
		
		// Set repeating alarm
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
				INITIAL_ALARM_DELAY,
				pendingIntent);
		
	}


	private void loadExistingImages() {
		File mediaStorageDir = getImagesDirectory();
		
		if (mediaStorageDir.exists()){
		
			String[] listfiles = mediaStorageDir.list();
			for (int i = 0; i < listfiles.length; i++) {
				adapter.add(Uri.parse(mediaStorageDir + File.separator + listfiles[i]));
			}

		}
	}
	
	
	private static File getImagesDirectory() {
		return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DailySelfie");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

	    // create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		
		return super.onOptionsItemSelected(item);
	}
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.
		File mediaStorageDir = getImagesDirectory();
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	    	if (! mediaStorageDir.mkdirs()){
	    		Log.d(TAG, "failed to create directory");
	    		return null;
	        }
	    }
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	            adapter.add(data.getData());
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}
	
}
