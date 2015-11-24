package com.example.dailyselfie;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class Activityfullscreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutb);
       ImageView img=(ImageView)findViewById(R.id.widget45);
       
       Bundle e = getIntent().getExtras();
       if (e != null)
       {
    	   try
    	   {
    	   String f = e.getString("data");
    	   Log.i("EX", f);
    	   java.io.FileInputStream in;
			in = openFileInput(f);
		   img.setImageBitmap(BitmapFactory.decodeStream(in));
    	   } catch (FileNotFoundException e1) {
    	   }
       }
       else
       {
    	   Log.i("EX", "null");
       }
    }
}