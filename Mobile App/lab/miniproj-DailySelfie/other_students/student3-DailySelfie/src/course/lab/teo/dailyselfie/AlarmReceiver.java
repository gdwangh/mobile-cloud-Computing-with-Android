package course.lab.teo.dailyselfie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	private static final int ONGOING_NOTIFICATION_ID = 2;
	private static String TAG = "AlarmReceiver";
	private static long TWO_MIN = 2 * 60 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();

		Log.d(TAG, "onReceive AlarmReceiver");
		Toast.makeText(context, "Remember take Daily Selfie", Toast.LENGTH_LONG)
				.show();

		NotificationManager mNotifyManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(context,
				DailySelfieActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);

		Notification notification = builder.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_menu_camera).setAutoCancel(true)
				.setContentTitle("Daily Selfie")
				.setContentText("Remember take Daily Selfie")
				.setColor(Color.parseColor(DailySelfieActivity.colorString))
				.build();

		mNotifyManager.notify(ONGOING_NOTIFICATION_ID, notification);

		wl.release();
	}

	public void SetAlarm(Context context) {
		Log.d(TAG, "SetAlarm");
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), TWO_MIN, pi);
	}

	public void CancelAlarm(Context context) {
		Log.d(TAG, "CancelAlarm");
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}