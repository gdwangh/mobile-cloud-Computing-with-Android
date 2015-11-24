package course.lab.teo.dailyselfie;

import java.io.File;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryActivity extends Activity {

	private static String TAG = "GalleryActivity";
	private static List<File> files;
	private static File storageDir;
	private ImageView imageView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {

			Window window = getWindow();

			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			// finally change the color
			window.setStatusBarColor(Color
					.parseColor(DailySelfieActivity.darkcolorString));
		}

		imageView = (ImageView) findViewById(R.id.image1);

		storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		loadData();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		GalleryActivity.this.finish();
	}

	private void loadData() {

		final Handler puente = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int i = getIntent().getExtras().getInt("i");
				setPic(imageView, files.get(i).getAbsolutePath());

				// Note that Gallery view is deprecated in Android 4.1---
				Gallery gallery = (Gallery) findViewById(R.id.gallery1);
				gallery.setAdapter(new ImageAdapter(GalleryActivity.this));
				gallery.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Toast.makeText(
								getBaseContext(),
								(new Date(files.get(position).lastModified())
										.toLocaleString()), Toast.LENGTH_SHORT)
								.show();
						// display the images selected

						setPic(imageView, files.get(position).getAbsolutePath());
					}
				});
			}
		};

		final ProgressDialog pd = ProgressDialog.show(GalleryActivity.this,
				"Loading data...", "Please wait", true, false);

		new Thread(new Runnable() {
			public void run() {
				boolean mensaje = true;
				files = DailySelfieActivity.getListFiles(storageDir);
				Log.d(TAG, "Loading data ");
				Message msg = new Message();
				if (mensaje)
					msg.obj = 0;
				else
					msg.obj = 1;
				puente.sendMessage(msg);
				pd.dismiss();
			}
		}).start();
	}

	public static void setPic(ImageView imageView, String photoPath) {
		// Get the dimensions of the View
		int targetW = imageView.getMeasuredWidth();
		int targetH = imageView.getMeasuredHeight();
		targetW = targetW == 0 ? 50 : targetW;
		targetH = targetH == 0 ? 50 : targetH;
		Log.d(TAG, "targetW: " + targetW + " targetH: " + targetH);
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		// int scaleFactor = Math.min(photoW / 50, photoH / 50);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
		imageView.setImageBitmap(bitmap);
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;

		public ImageAdapter(Context c) {
			context = c;
			// sets a grey background; wraps around the images
			TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
			itemBackground = a.getResourceId(
					R.styleable.MyGallery_android_galleryItemBackground, 0);
			a.recycle();
		}

		// returns the number of images
		public int getCount() {
			return files.size();
		}

		// returns the ID of an item
		public Object getItem(int position) {
			return position;
		}

		// returns the ID of an item
		public long getItemId(int position) {
			return files.get(position).hashCode();
		}

		// returns an ImageView view
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			for (File file : files)
				if (file.hashCode() == getItemId(position)) {
					GalleryActivity.setPic(imageView, file.getAbsolutePath());
					// imageView
					// .setLayoutParams(new Gallery.LayoutParams(100, 100));
					imageView.setBackgroundResource(itemBackground);
				}
			return imageView;
		}
	}

}
