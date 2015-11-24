package com.ggalvin.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import com.ggalvin.dailyselfie.DailySelfieActivity;

/**
 * Created by ggalv_000 on 2015-11-18.
 */
public class SelfieAlarmReceiver extends BroadcastReceiver {
    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG = "SelfieAlarmReceiver";

    // Notification Text Elements
    private final CharSequence contentText = "Time for a SELFIE!!!!!";

    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    // Notification Sound and Vibration on Arrival
//    private final Uri soundURI = Uri
//           .parse("android.resource://course.examples.Alarms.AlarmCreate/"
//                    + R.raw.alarm_rooster);
    private final long[] mVibratePattern = { 0, 200, 200, 300 };

    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm Notification Received at:"
                + DateFormat.getDateTimeInstance().format(new Date()));

        // The Intent to be used when the user clicks on the Notification View
        mNotificationIntent = new Intent(context, DailySelfieActivity.class);
        mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // The PendingIntent that wraps the underlying Intent
        mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Uses R.layout.custom_notification for the
        // layout of the notification View. The xml
        // file is in res/layout/custom_notification.xml
        RemoteViews mContentView = new RemoteViews(
                context.getPackageName(),
                R.layout.custom_notification);

        mContentView.setTextViewText(R.id.text, contentText);

        // TODO: Use the Notification.Builder class to
        // create the Notification. You will have to set
        // several pieces of information. You can use
        // android.R.drawable.stat_sys_warning
        // for the small icon. You should also
        // setAutoCancel(true).

        Notification.Builder notificationBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setAutoCancel(true)
                        .setContentIntent(mContentIntent)
                        .setContent(mContentView);

        // TODO: Send the notification
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());

        Log.i(TAG, "Alarm Notification Sent:"
                + DateFormat.getDateTimeInstance().format(new Date()));
    }

}
