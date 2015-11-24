package com.ggalvin.dailyselfie;

import android.graphics.Bitmap;

/**
 * Created by ggalv_000 on 2015-11-18.
 */
public class SelfieImage {
    private String absoluteFileName;
    private Bitmap thumbnail;

    public SelfieImage(String filename, Bitmap image)
    {
        absoluteFileName = filename;
        thumbnail = image;
    }

    public String getFileName()
    {
        return absoluteFileName;
    }

    public Bitmap getThumbnail()
    {
        return thumbnail;
    }
}
