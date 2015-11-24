package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	private static final int APP_NOTIFICATION_ID = 1;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent mNotificationIntent = new Intent(context, MainActivity.class);
		// The PendingIntent that wraps the underlying Intent
		 PendingIntent mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(context.getText(R.string.tickerText))
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(context.getText(R.string.contentTitle))
				.setContentText(context.getText(R.string.contentText)).setContentIntent(mContentIntent)
				.setSmallIcon(android.R.drawable.ic_menu_camera);
				//.setSound(soundURI).setVibrate(mVibratePattern);
		
		
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Pass the Notification to the NotificationManager:
		mNotificationManager.notify(APP_NOTIFICATION_ID,
				notificationBuilder.build());
	}

}
