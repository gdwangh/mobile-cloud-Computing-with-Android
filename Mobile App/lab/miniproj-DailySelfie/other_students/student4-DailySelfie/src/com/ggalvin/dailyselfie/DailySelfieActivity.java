package com.ggalvin.dailyselfie;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class DailySelfieActivity extends AppCompatActivity
        implements LargeImageFragment.OnFragmentInteractionListener, GridViewFragment.ImageGridListener
{
    private LargeImageFragment  largeImageFragment = null;
    private GridViewFragment    imageGridFragment = null;
    private PendingIntent alarmIntent = null;
    private boolean gridViewPresent = true;

    private static final long ALARM_DURATION = 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily_selfie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupAlarm();

        GridViewFragment gridViewFrag = GridViewFragment.newInstance();

        // Create the GridView Fragment and display it.
        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();

        // Add the FeedFragent to the layout
        fragmentTransaction.add(R.id.fragment_container,
                gridViewFrag);

        // Add this FragmentTransaction to the backstack
        fragmentTransaction.addToBackStack(null);

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // execute transaction now
        getFragmentManager().executePendingTransactions();
    }

    // Menu options to set and cancel the alarm.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.set_alarm) {
            setupAlarm();
            return true;
        } else if (item.getItemId() == R.id.cancel_alarm){
            cancelAlarm();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        }
        else {
            finish();
        }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_selfie, menu);
        return true;
    }

    public void deleteButtonPressed(String filename)
    {
        Toast.makeText(getBaseContext(), "Delete File: " + filename, Toast.LENGTH_SHORT).show();

        getFragmentManager().popBackStack();
    }

    public void onImageSelected(String imageFileName)
    {
        LargeImageFragment imageFragment = LargeImageFragment.newInstance(imageFileName);

        // Create the GridView Fragment and display it.
        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();

        // Add the FeedFragent to the layout
        fragmentTransaction.replace(R.id.fragment_container, imageFragment);

        // Add this FragmentTransaction to the backstack
        fragmentTransaction.addToBackStack(null);

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // execute transaction now
        getFragmentManager().executePendingTransactions();
    };

    private void setupAlarm()
    {
        Log.i("setupAlarm", "Setting Alarm.");
        Intent receiverIntent = new Intent(this, SelfieAlarmReceiver.class);
        PendingIntent receiverPendingIntent = PendingIntent.getBroadcast(DailySelfieActivity.this, 0, receiverIntent, 0);

        Intent loggerIntent = new Intent(this, AlarmLoggerReceiver.class);
        PendingIntent loggerPendingIntent = PendingIntent.getBroadcast(DailySelfieActivity.this, 0, loggerIntent, 0);

        AlarmManager alarmMgr = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);;

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + ALARM_DURATION, ALARM_DURATION, receiverPendingIntent);

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + ALARM_DURATION, ALARM_DURATION, loggerPendingIntent);

        Log.i("setupAlarm", "Alarm set.");

   }

    public void cancelAlarm()
    {
        AlarmManager alarmMgr = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

    }
}
