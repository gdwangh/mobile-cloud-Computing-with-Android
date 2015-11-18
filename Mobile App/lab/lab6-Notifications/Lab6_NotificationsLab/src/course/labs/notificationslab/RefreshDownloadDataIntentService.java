package course.labs.notificationslab;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import course.labs.notificationslab.DownloaderTaskFragment.DownloaderTask;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RefreshDownloadDataIntentService extends IntentService {
	// TODO: Rename actions, choose action names that describe tasks that this
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_RE_DOWNLOAD = MainActivity.ACTION_RE_DOWNLOAD;

	private static final String TAG = "ReDownload-IntentService";

	private DownloaderTaskFragment mDownloaderFragment;
	private Integer[] mDownParam;
	
	
	public RefreshDownloadDataIntentService() {
		super("RefreshDownloadDataIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "Alarm at " + DateFormat.getDateTimeInstance().format(new Date()));

		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_RE_DOWNLOAD.equals(action)) {
				if (mDownloaderFragment == null)  {
					
					mDownloaderFragment = new DownloaderTaskFragment();

					ArrayList<Integer> listArgs = intent.getIntegerArrayListExtra(MainActivity.TAG_FRIEND_RES_IDS);
					mDownParam = listArgs.toArray(new Integer[listArgs.size()]);
				}
				
				mDownloaderFragment.downloaderForAlarmService(this.getApplicationContext(), mDownParam);
				
			} else {
				Log.i(TAG, "intent.action is not ACTION_RE_DOWNLOAD! ");
			}
		}
	}

}
