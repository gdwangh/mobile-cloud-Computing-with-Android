package com.paulw.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by paulweir on 11/16/15.
 */

public class DailySelfieMainActivity extends AppCompatActivity {

    private static final String TAG = "DailySelfieMainActivity";
    private static final long TWO_MINUTE_DELAY = 2 * 60 * 1000L;
    static final int REQUEST_TAKE_PHOTO = 1;

    SelfieViewAdaptor mAdapter;
    ListView mListView;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Inside of onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily_selfie_main);

        mAdapter = new SelfieViewAdaptor(getApplicationContext());

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(((SelfieViewAdaptor.ViewHolder) view.getTag()).uriLoaction, "image/");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Context Menu");
                menu.add(0, v.getId(), 0, R.string.remove_selfie_ctx_menu_item);
            }
        });

        registerForContextMenu(mListView);
        registerAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean rtnValue = true;
        if (item.getItemId() == R.id.camera) {
            startCamera();
        } else {
            rtnValue = super.onOptionsItemSelected(item);
        }
        return rtnValue;
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "Inside of onResume");
        super.onResume();

        if (mAdapter.getList().isEmpty()) {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);

            int idx = 0;
            String filePath = prefs.getString(String.valueOf(idx), "");
            while (!filePath.isEmpty()) {
                mAdapter.add(new SelfieRecord(filePath));
                filePath = prefs.getString(String.valueOf(++idx), "");
            }
        }
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Inside of onStop");
        SharedPreferences prefs =  getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        for (int idx = 0; idx < mAdapter.getCount(); idx++) {
            editor.putString(String.valueOf(idx),
                    ((SelfieRecord) mAdapter.getItem(idx)).getSelfieFilePath());
        }
        editor.commit();
        super.onStop();
    }

    private void startCamera() {
        Context context = getApplicationContext();

        Log.i(TAG, "starting camera");
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(this, "This device does not have a camera.", Toast.LENGTH_SHORT).show();
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    File photoFile = createImageFile();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    Log.i(TAG, "started camera");
                } catch (IOException ex) {
                    Log.e(TAG, "could not create photo file", ex);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            SelfieRecord sr = new SelfieRecord( mCurrentPhotoPath );
            mAdapter.add(sr);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile( imageFileName, ".jpg", storageDir );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, R.string.remove_selfie_ctx_menu_item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean rtnValue = true;
        if( item.getTitle().equals(getResources().getString(R.string.remove_selfie_ctx_menu_item)))  {
            for (SelfieRecord sr : mAdapter.getList()) {
                new File(sr.getSelfieFilePath()).delete();
                Log.i(TAG, "deleted File " + sr.getSelfieFilePath());
            }
            mAdapter.removeAllViews();
            Log.i(TAG, "cleared selfie list");
        }
        else {
            rtnValue = false;
        }
        return rtnValue;
    }

    private void registerAlarm() {
        Intent notificationReceiverIntent = new Intent(this, SelfieAlarmNotificationReceiver.class);
        PendingIntent notificationReceiverPendingIntent = PendingIntent.getBroadcast(this, 0, notificationReceiverIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TWO_MINUTE_DELAY,
                TWO_MINUTE_DELAY,
                notificationReceiverPendingIntent);
    }
}