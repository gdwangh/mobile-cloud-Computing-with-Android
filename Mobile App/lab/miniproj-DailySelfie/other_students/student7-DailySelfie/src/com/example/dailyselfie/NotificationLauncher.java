package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NotificationLauncher extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("NotificationLauncher", "Alarm received");
		notifyArea(context);
	}
	
	protected void notifyArea(Context context)
	{
		//RemoteViews mContentView = new RemoteViews(
			//	context.getPackageName(),
				//R.layout.custom_notification);
		
	//	mContentView.setTextViewText(R.id.text, "t123");
		//mContentView.setOnClickPendingIntent(R.id.toast_layout_root, i);
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent = 
				PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
 
		Notification not = new NotificationCompat.Builder(context)
				.setContentTitle("Daily Selfie")
				.setContentText("Time for another selfie")
				.setAutoCancel(true)
				//.setSmallIcon(android.R.drawable.ic_menu_camera)
				//.setSmallIcon(android.R.drawable.ic_menu_camera)
				.setSmallIcon(R.drawable.ic_photo_camera_white_24dp)
				.setContentIntent(resultPendingIntent)
				.build();

		NotificationManager mgr = 
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify(0, not);

		//Toast.makeText(context, "blah", Toast.LENGTH_LONG).show();
	}
	/*
	private void launchAsService(Context context)
	{
		Intent i = new Intent(context, NotifierService.class);
        context.startService(i);
	}
	*/

}
