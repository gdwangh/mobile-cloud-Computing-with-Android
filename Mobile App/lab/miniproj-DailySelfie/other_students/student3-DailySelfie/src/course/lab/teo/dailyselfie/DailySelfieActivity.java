package course.lab.teo.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class DailySelfieActivity extends ActionBarActivity {
	private static String TAG = "DailySelfieActivity";
	public static String colorString = "#FF74E088";
	public static String darkcolorString = "#FF4EB158";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_TAKE_PHOTO = 2;

	private static List<File> files;
	private static File storageDir;
	private PicsAdapter mPicsAdapter;
	private ListView list;
	private Intent intentService;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_selfie);

		intentService = new Intent(this, DailySelfieService.class);

		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(colorString)));

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// finally change the color
			window.setStatusBarColor(Color.parseColor(darkcolorString));
		}

		storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if (null == storageDir || !storageDir.isDirectory())
			storageDir.mkdirs();
		Log.d(TAG, "storageDir: " + storageDir);
		loadData();

		list = (ListView) findViewById(R.id.list_selfies);
		mPicsAdapter = new PicsAdapter(DailySelfieActivity.this);
		registerForContextMenu(list);

		// listener que responde al hacer clic en un elemnento del listView
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> lv, View v, int position,
					long id) {
				Log.d(TAG, "Click on item position: " + position + " id:" + id);
				final ProgressDialog pd = ProgressDialog.show(
						DailySelfieActivity.this, "Cargando Datos...",
						"Espere por favor", true, false);
				Intent intent = new Intent(DailySelfieActivity.this,
						GalleryActivity.class);

				for (int i = 0; i < files.size(); i++) {
					if (files.get(i).hashCode() == id)
						intent.putExtra("i", i);
				}
				intent.putExtra("position", position);

				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				pd.dismiss();
				// se inicia el diálogo de progreso
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daily_selfie, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_camera) {
			dispatchTakePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			loadData();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		stopService(intentService);
	}

	@Override
	protected void onPause() {
		super.onPause();
		startService(intentService);
	}

	private void loadData() {
		// pueste que se comunica con el hilo
		final Handler puente = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if ((Integer) msg.obj == 0) {
					list.setAdapter(mPicsAdapter);
				} else {
					list.setAdapter(mPicsAdapter);
				}
			}
		};

		// se inicia el diálogo de progreso
		final ProgressDialog pd = ProgressDialog.show(DailySelfieActivity.this,
				"Loading data...", "Please wait", true, false);
		// tarea que inicia la generación de la remesa y envía un mensaje al
		// puente para que mustre el diálogo de confirmación
		new Thread(new Runnable() {
			public void run() {
				boolean mensaje = true;
				files = getListFiles(storageDir);
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

	public static List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		if (null != files && files.length > 0)
			for (File file : files)
				if (!file.isDirectory() && file.getName().startsWith("JPEG_"))
					if (file.length() > 0)
						inFiles.add(file);
					else
						file.delete();

		return inFiles;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				Log.e(TAG, "Error creating image file: " + ex.getMessage());

			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new java.util.Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		Log.d(TAG, "imageFileName: " + imageFileName);

		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		return image;
	}

	// función que infla el listView con los datos del vector datos
	private static class PicsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		Context context = null;
		TextView texto1, texto2, texto3, flecha;
		ImageView imagen;

		public PicsAdapter(Context c) {
			context = c;
			mInflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final int id;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item, null);
			}

			texto1 = (TextView) convertView.findViewById(R.id.texto1);
			texto2 = (TextView) convertView.findViewById(R.id.texto2);
			texto3 = (TextView) convertView.findViewById(R.id.texto3);
			flecha = (TextView) convertView.findViewById(R.id.flecha);
			imagen = (ImageView) convertView.findViewById(R.id.imagen);

			imagen.setVisibility(View.VISIBLE);
			flecha.setVisibility(View.VISIBLE);

			for (File file : files)
				if (file.hashCode() == getItemId(position)) {
					setPic(file.getAbsolutePath());
					texto1.setText(file.getName());
					texto2.setVisibility(View.GONE);
					texto3.setText((new Date(file.lastModified())
							.toLocaleString()));
				}

			return convertView;
		}

		public int getCount() {
			return files.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return files.get(position).hashCode();
		}

		private void setPic(String photoPath) {
			// Get the dimensions of the View
			int targetW = imagen.getMeasuredWidth();
			int targetH = imagen.getMeasuredHeight();
			// Avoid dimensions of View being 0
			targetW = targetW == 0 ? 50 : targetW;
			targetH = targetH == 0 ? 50 : targetH;
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
			imagen.setImageBitmap(bitmap);
		}
	}

}
