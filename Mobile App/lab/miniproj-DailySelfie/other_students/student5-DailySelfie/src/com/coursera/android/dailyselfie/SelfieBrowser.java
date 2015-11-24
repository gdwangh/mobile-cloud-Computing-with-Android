package com.coursera.android.dailyselfie;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class SelfieBrowser extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri imageFileUri;
	private static final String TAG = "SelfieBrowser";
	Intent cameraIntent;
	private AlarmManager mAlarmManager;
	Intent selfieReceiverIntent;
	private PendingIntent selfieReceiverPendingIntent;
	private static final long ALARM_INTERVAL = 2 * 60 * 1000L;
	public static String filesPath;
	GridViewImageAdapter imageAdapter;
	GridView imageGridView;
	ImageView fullImageView;
	int columnWidth;
	Utils utils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfie_browser);

		utils = new Utils(this);

		initGridLayout();

		cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);

		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		selfieReceiverIntent = new Intent(getApplicationContext(), SelfieAlarmReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		selfieReceiverPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, selfieReceiverIntent, 0);

		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + ALARM_INTERVAL,
				ALARM_INTERVAL, selfieReceiverPendingIntent);

		filesPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
		imageAdapter = new GridViewImageAdapter(SelfieBrowser.this, utils.getFilePaths(), columnWidth, utils);
		imageGridView.setAdapter(imageAdapter);
	}

	protected void onNewIntent(Intent intent){
	    super.onNewIntent(intent);
	    Log.i(TAG, "New intent with flags "+intent.getFlags());
	}
	
	private void initGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstant.GRID_PADDING,
				r.getDisplayMetrics());

		imageGridView = (GridView) findViewById(R.id.grid_view);
		
		columnWidth = (int) ((getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		imageGridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		imageGridView.setColumnWidth(columnWidth);
		imageGridView.setStretchMode(GridView.NO_STRETCH);
		imageGridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
		imageGridView.setHorizontalSpacing((int) padding);
		imageGridView.setVerticalSpacing((int) padding);
	}

	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selfie_browser, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_take_selfie:
			String fileName = utils.getNewFileName();
			imageFileUri = Uri.fromFile(new File(fileName));
			Log.i(TAG, "Image filename: " + fileName);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
			startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			break;

		case R.id.action_delete:
			new AlertDialog.Builder(this).setTitle("Delete all Selfies").setMessage(R.string.delete_alert_message)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							File selfieDirectory = new File(filesPath);
							if (selfieDirectory.isDirectory()) {
								String[] children = selfieDirectory.list();
								for (int i = 0; i < children.length; i++) {
									new File(selfieDirectory, children[i]).delete();
								}
							}
							Toast.makeText(getApplicationContext(), R.string.delete_selfies, Toast.LENGTH_LONG).show();
							imageAdapter.setFilePaths(utils.getFilePaths());
							imageAdapter.notifyDataSetChanged();
						}
					}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				Log.i(TAG, "Image saved to:\n" + imageFileUri);
				imageAdapter.setFilePaths(utils.getFilePaths());
				imageAdapter.notifyDataSetChanged();

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
				Toast.makeText(this, R.string.image_capture_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static String getFilesPath() {
		return filesPath;
	}
}
