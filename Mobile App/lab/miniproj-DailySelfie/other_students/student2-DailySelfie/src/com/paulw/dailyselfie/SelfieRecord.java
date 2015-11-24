package com.paulw.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by paulweir on 11/16/15.
 */
public class SelfieRecord {

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private Bitmap mSelfiePreview;
    private String mDateTimeString;
    private String mSelfiePath;

    public SelfieRecord(String selfiePath) {
        this( selfiePath, BitmapFactory.decodeFile(selfiePath) );
    }

    public SelfieRecord(String selfiePath, Bitmap imageBitmap) {
        mSelfiePath = selfiePath;
        mDateTimeString =  SDF.format( new File(selfiePath).lastModified() );
        mSelfiePreview = imageBitmap;
    }

    public Bitmap getSelfiePreview() {
        return mSelfiePreview;
    }

    public String getSelfieDateTime() {
        return mDateTimeString;
    }

    public String getSelfieFilePath() { return mSelfiePath; }
}
