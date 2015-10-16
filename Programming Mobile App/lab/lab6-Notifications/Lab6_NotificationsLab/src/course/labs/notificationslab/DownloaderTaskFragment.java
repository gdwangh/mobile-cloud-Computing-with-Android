package course.labs.notificationslab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloaderTaskFragment extends Fragment {

	private DownloadFinishedListener mCallback;
	private Context mContext;
	private final int MY_NOTIFICATION_ID = 11151990;

	private DownloaderTask mDownloadTask;
	
	@SuppressWarnings("unused")
	private static final String TAG = "Lab-Notifications";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Preserve across reconfigurations
		setRetainInstance(true);

		// TODO: Create new DownloaderTask that "downloads" data
		DownloaderTask mDownloadTask = new DownloaderTask();
		
		Log.i(TAG, "MainActivity DownloaderTaskFragment prepareParame");

		// TODO: Retrieve arguments from DownloaderTaskFragment
		// Prepare them for use with DownloaderTask.
		ArrayList<Integer> listArgs = this.getArguments().getIntegerArrayList(MainActivity.TAG_FRIEND_RES_IDS);
		Integer[] mDownParam = listArgs.toArray(new Integer[listArgs.size()]);	
		
		Log.i(TAG, "MainActivity DownloaderTaskFragment execute download Task");
		
		// TODO: Start the DownloaderTask
		mDownloadTask.execute(mDownParam);
		
	}

	// Assign current hosting Activity to mCallback
	// Store application context for use by downloadTweets()
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mContext = activity.getApplicationContext();

		// Make sure that the hosting activity has implemented
		// the correct callback interface.
		try {
			mCallback = (DownloadFinishedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DownloadFinishedListener");
		}
	}

	// Null out mCallback
	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDownloadTask = null;
	}
	
	public void downloaderForAlarmService(Context context, Integer[] param) {
		
		mContext = context;
		
		if (mDownloadTask==null) {
			// TODO: Create new DownloaderTask that "downloads" data
			mDownloadTask = new DownloaderTask();
		}
			
		Log.i(TAG, "enter downloaderForAlarmService()" + DateFormat.getDateTimeInstance().format(new Date()));

		// TODO: Start the DownloaderTask
		mDownloadTask.execute(param);
		
	}
	
	// TODO: Implement an AsyncTask subclass called DownLoaderTask.
	// This class must use the downloadTweets method (currently commented
	// out). Ultimately, it must also pass newly available data back to
	// the hosting Activity using the DownloadFinishedListener interface.

	 public class DownloaderTask extends AsyncTask<Integer, Void, String[]> {

		@Override
		protected String[] doInBackground(Integer... resId) {
			Log.i(TAG, "MainActivity DownloaderTaskFragment doInBackground()");

			String[] data = downloadTweets(resId);
			
			Log.i(TAG, "MainActivity DownloaderTaskFragment doInBackground() exit");

			return data;
		}
	
		@Override
		protected void onPostExecute(String[] result) {
			Log.i(TAG, "MainActivity DownloaderTaskFragment onPostExecute(): result len="+result.length);
			
			if (mCallback != null)  {
				Log.i(TAG, "MainActivity DownloaderTaskFragment notifyDataRefreshed()");

				mCallback.notifyDataRefreshed(result);
			}
		}
	
	
	
	
		// TODO: Uncomment this helper method
		// Simulates downloading Twitter data from the network

	  // change private to public
	  // private String[] downloadTweets(Integer resourceIDS[]) {
		public String[] downloadTweets(Integer resourceIDS[]) {
	 
			final int simulatedDelay = 2000;
			String[] feeds = new String[resourceIDS.length];
			boolean downLoadCompleted = false;

			try {
				for (int idx = 0; idx < resourceIDS.length; idx++) {
					InputStream inputStream;
					BufferedReader in;
					try {
						// Pretend downloading takes a long time
						Thread.sleep(simulatedDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					inputStream = mContext.getResources().openRawResource(
							resourceIDS[idx]);
					in = new BufferedReader(new InputStreamReader(inputStream));

					String readLine;
					StringBuffer buf = new StringBuffer();

					while ((readLine = in.readLine()) != null) {
						buf.append(readLine);
					}

					feeds[idx] = buf.toString();

					if (null != in) {
						in.close();
					}
				}

				downLoadCompleted = true;
				saveTweetsToFile(feeds);

			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.i(TAG, "MainActivity DownloaderTaskFragment download complete");

			// Notify user that downloading has finished
			notify(downLoadCompleted);
			
			Log.i(TAG, "MainActivity DownloaderTaskFragment downloadTweets feeds:"+feeds.length);
			
			return feeds;

		}

		// Uncomment this helper method.
		// If necessary, notifies the user that the tweet downloads are
		// complete. Sends an ordered broadcast back to the BroadcastReceiver in
		// MainActivity to determine whether the notification is necessary.

	   
		private void notify(final boolean success) {
			Log.i(TAG, "MainActivity DownloaderTaskFragment notify("+success+")");

			final Intent restartMainActivityIntent = new Intent(mContext,
					MainActivity.class);
			restartMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Sends an ordered broadcast to determine whether MainActivity is
			// active and in the foreground. Creates a new BroadcastReceiver
			// to receive a result indicating the state of MainActivity

			// The Action for this broadcast Intent is
			// MainActivity.DATA_REFRESHED_ACTION
			// The result, MainActivity.IS_ALIVE, indicates that MainActivity is
			// active and in the foreground.
			Log.i(TAG, "sendOrderedBroadcast mContext="+mContext.toString());
			
			
			Intent tmpIntent = new Intent(MainActivity.DATA_REFRESHED_ACTION);
			tmpIntent.setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);  // debug

			/* mContext.sendOrderedBroadcast(intent, 
			 * 								receiverPermission, 
			 * 								resultReceiver, 
			 * 								scheduler, 
			 * 								initialCode, 
			 * 								initialData, 
			 * 								initialExtras)
			 */
			mContext.sendOrderedBroadcast(tmpIntent, null,
					new BroadcastReceiver() {

						final String failMsg = mContext
								.getString(R.string.download_failed_string);
						final String successMsg = mContext
								.getString(R.string.download_succes_string);
						final String notificationSentMsg = mContext
								.getString(R.string.notification_sent_string);

						@Override
						public void onReceive(Context context, Intent intent) {

							// TODO: Check whether or not the MainActivity
							// received the broadcast
							
							boolean isActive = (getResultCode()==MainActivity.IS_ALIVE);
							Log.i(TAG, "sendOrderedBroadcast.resultCode="
									+getResultCode()+", isActive="+isActive);
														
							// if (true || false) {
							// when called by RefreshDownloadDataIntentService, could not get
							// the activity to callback.
							if ((mCallback==null) || (!isActive)) {  
								Log.i(TAG, "MainActivity DownloaderTaskFragment PendingIntent");
								
								// TODO: If not, create a PendingIntent using
								// the
								// restartMainActivityIntent and set its flags
								// to FLAG_UPDATE_CURRENT
								PendingIntent mContentIntent =  PendingIntent.getActivity(mContext, 0,
																restartMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
								
								
								// Uses R.layout.custom_notification for the
								// layout of the notification View. The xml
								// file is in res/layout/custom_notification.xml
								Log.i(TAG, "MainActivity DownloaderTaskFragment create notification view");
								
								RemoteViews mContentView = new RemoteViews(
										mContext.getPackageName(),
										R.layout.custom_notification); 

								// TODO: Set the notification View's text to
								// reflect whether the download completed
								// successfully
								mContentView.setTextViewText(R.id.text, success ? successMsg : failMsg);

								
								// TODO: Use the Notification.Builder class to
								// create the Notification. You will have to set
								// several pieces of information. You can use
								// android.R.drawable.stat_sys_warning
								// for the small icon. You should also
								// setAutoCancel(true).

								// Notification.Builder notificationBuilder = null;
								Log.i(TAG, "MainActivity DownloaderTaskFragment Notification.Builder");
								
								Notification.Builder notificationBuilder = new Notification.Builder(mContext)
																				.setAutoCancel(true)
																				.setTicker(success ? successMsg : failMsg)
																				.setSmallIcon(android.R.drawable.stat_sys_warning)
																				.setContentIntent(mContentIntent)
																				.setContent(mContentView);
								
								
								// TODO: Send the notification
								Log.i(TAG, "MainActivity DownloaderTaskFragment Send the notification");

								NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
								mNotificationManager.notify(MY_NOTIFICATION_ID,	notificationBuilder.build()); 

								Toast.makeText(mContext, notificationSentMsg,
										Toast.LENGTH_LONG).show();

							} else {
								Toast.makeText(mContext,
										success ? successMsg : failMsg,
										Toast.LENGTH_LONG).show();
							}
						}
					}, null, 0, null, null);
					

		}


	
		// Uncomment this helper method
		// Saves the tweets to a file
	  
		private void saveTweetsToFile(String[] result) {
			PrintWriter writer = null;
			try {
				FileOutputStream fos = mContext.openFileOutput(
						MainActivity.TWEET_FILENAME, Context.MODE_PRIVATE);
				writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(fos)));

				for (String s : result) {
					writer.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != writer) {
					writer.close();
				}
			}
		}

	 }
}