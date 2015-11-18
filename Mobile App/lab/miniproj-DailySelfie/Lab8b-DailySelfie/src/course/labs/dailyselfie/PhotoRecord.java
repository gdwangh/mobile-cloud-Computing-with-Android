package course.labs.dailyselfie;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class PhotoRecord {
	private String mPhotoPath;
	private String mPhotoName;
	
	private String TAG = "Lab-PhotoRecord";
	
	public PhotoRecord(String photoPath) {
		this.mPhotoPath = photoPath;
	}
	
	public String getPhotoPath() {
		return mPhotoPath;
	}
	
	public String getPhotoName() {
		File f = new File(mPhotoPath);
    	if (! f.exists() || f.length()==0) {
        	Log.i(TAG, "createCaptureBitmap(1), file not exists:"+mPhotoPath);

    		return null;
    	}
    	
    	String fname = f.getName();
    	return fname.substring(0, fname.length()-4);
	}
	
    public Bitmap getBitmap(ImageView iv) {
    	Log.i(TAG, "enter createCaptureBitmap()");
    	
    	File f = new File(mPhotoPath);
    	if (! f.exists() || f.length()==0) {
        	Log.i(TAG, "createCaptureBitmap(1), file not exists:"+mPhotoPath);

    		return null;
    	}
    	
    	Log.i(TAG, "createCaptureBitmap(2)");

        // Get the dimensions of the View
        // int targetW = iv.getWidth();
    	int targetW = iv.getLayoutParams().width;
    	
        // int targetH = iv.getHeight();
    	int targetH = iv.getLayoutParams().height;

      
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoPath, bmOptions);
        
    	Log.i(TAG, "createCaptureBitmap(3)");

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
      
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
      
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
      
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, bmOptions);
    	Log.i(TAG, "createCaptureBitmap(4)");

        // mImageView.setImageBitmap(bitmap);
        return bitmap;
    }
}
