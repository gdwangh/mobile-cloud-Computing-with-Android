package course.labs.dailyselfie;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ImageActivity extends Activity {
	private TextView mTvPhotoPath;
	private ImageView mImageV;
	private String TAG = "Lab-showPhoto";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_page);

		Log.i(TAG, "ImageActivity.onCreate()");
		
		// get the path of photo to show
		Intent intent=getIntent();
		String photoPath = intent.getStringExtra(MainActivity.SHOW_IMAGE);
		if (photoPath.equals(""))  {
			finish();
		}
		
		// get and create bitmap from file
		PhotoRecord photoRecord = new PhotoRecord(photoPath);
		mImageV = (ImageView)findViewById(R.id.photo_image);
		mImageV.setImageBitmap(photoRecord.getBitmap(mImageV));
		
    	Toast.makeText(getApplicationContext(), photoPath, Toast.LENGTH_LONG).show();

	}
}
