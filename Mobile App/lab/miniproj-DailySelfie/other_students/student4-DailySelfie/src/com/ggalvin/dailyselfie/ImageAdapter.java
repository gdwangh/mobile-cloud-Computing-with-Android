package com.ggalvin.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ggalv_000 on 2015-11-17.
 */
public class ImageAdapter extends BaseAdapter
{
    private Context context;
    ArrayList<SelfieImage> selfieArray = new ArrayList<SelfieImage>();
    private int imageHeight = 150;
    private int imageWidth = 150;

    public ImageAdapter(Context c)
    {
        context = c;
    }

    //---returns the number of images---
    public int getCount() {

        return selfieArray.size();
    }

    //---returns the item---
    public Object getItem(int position) {
        return selfieArray.get(position);
    }

    //---returns the ID of an item---
    public long getItemId(int position) {
        return position;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView,
                        ViewGroup parent)
    {
        ImageView imageView = null;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new
                    GridView.LayoutParams(imageWidth, imageHeight));
            imageView.setScaleType(
                    ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(selfieArray.get(position).getThumbnail());

        return imageView;
    }

    public void addImage(String mCurrentPhotoPath)
    {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/imageWidth, photoH/imageHeight);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        SelfieImage selfie = new SelfieImage(mCurrentPhotoPath, BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions));
        selfieArray.add(selfie);
        notifyDataSetChanged();
    }

    public void addImage(Bitmap image)
    {
        if (image != null)
        {
            SelfieImage selfie = new SelfieImage(null, image);
            selfieArray.add(selfie);
            notifyDataSetChanged();
        }
    }

}
