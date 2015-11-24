package com.example.dailyselfie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowPicture extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_picture);
		
		ImageView image = (ImageView)findViewById(R.id.full_picture);
		image.setImageURI(getIntent().getData());
	}
}
