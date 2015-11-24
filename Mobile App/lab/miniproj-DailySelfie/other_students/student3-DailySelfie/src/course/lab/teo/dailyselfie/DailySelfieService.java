package course.lab.teo.dailyselfie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DailySelfieService extends Service {
	private static String TAG = "DailySelfieService";
	AlarmReceiver alarm = new AlarmReceiver();

	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
		alarm.SetAlarm(this);
		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart()");
		alarm.SetAlarm(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind()");
		return null;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		alarm.CancelAlarm(this);
		super.onDestroy();
	}
}