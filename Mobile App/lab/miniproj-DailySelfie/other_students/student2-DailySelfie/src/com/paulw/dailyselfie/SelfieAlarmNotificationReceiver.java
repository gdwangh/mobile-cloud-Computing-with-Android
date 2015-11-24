package com.paulw.dailyselfie;

import java.text.DateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
/**
 * Created by paulweir on 11/16/15.
 */

public class SelfieAlarmNotificationReceiver extends BroadcastReceiver {

	private static final int MY_NOTIFICATION_ID = 1432;
	private static final String TAG = "AlarmNotificationRvr";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceived intent");

		Intent notificationIntent = new Intent(context, DailySelfieMainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		String message = context.getString(R.string.notification_msg);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(android.R.drawable.ic_menu_camera)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentIntent(pIntent)
				.setPriority(5) //private static final PRIORITY_HIGH = 5;
				.setContentText(message)
				.setAutoCancel(true)
				.setTicker(message)
				.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

		((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
				.notify(MY_NOTIFICATION_ID, mBuilder.build());

		Log.i(TAG, "Sending notification at:"+ DateFormat.getDateTimeInstance().format(new Date()));
	}
}
