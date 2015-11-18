package com.example.lab_modernartui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.os.Build;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ColorBlockActivity extends ActionBarActivity {
	static final String TAG = "ColorBlock";
	static final int SEEKBAR_MAX = 256;
	static private final String URL = "http://www.moma.org";
	
	// For use with app chooser
	static private final String CHOOSER_TEXT = "Load " + URL + " with:";
	    
		
	private TextView mTextViewLeftUp, mTextViewLeftBottom;
	private TextView mTextViewRightUp, mTextViewRightMid, mTextViewRightBottom;
	private SeekBar mSeekBar;
	
	private int mColor1, mColor2, mColor3, mColor4, mColor5;
	private int mBeginColor1, mBeginColor2, mBeginColor3, mBeginColor4, mBeginColor5;
	private int mEndColor1, mEndColor2, mEndColor3, mEndColor4, mEndColor5;
	
	private Dialog mDialog;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_block);
        
        mTextViewLeftUp = (TextView)findViewById(R.id.ColorBlockLeftUp);
        mTextViewLeftBottom = (TextView)findViewById(R.id.ColorBlockLeftBottom);
        mTextViewRightUp = (TextView)findViewById(R.id.ColorBlockRightUp);
        mTextViewRightMid = (TextView)findViewById(R.id.ColorBlockRightMid);
        mTextViewRightBottom = (TextView)findViewById(R.id.ColorBlockRightBottom);
        /* 用缺省生成的fragment会闪退，改成直接在active中layout，就成功了*/
        
        mBeginColor1 = getResources().getColor(R.color.begin_color1);
        mBeginColor2 = getResources().getColor(R.color.begin_color2);
        mBeginColor3 = getResources().getColor(R.color.begin_color3);
        mBeginColor4 = getResources().getColor(R.color.begin_color4);
        mBeginColor5 = getResources().getColor(R.color.begin_color5);
 
        mEndColor1 = getResources().getColor(R.color.end_color1);
        mEndColor2 = getResources().getColor(R.color.end_color2);
        mEndColor3 = getResources().getColor(R.color.end_color3);
        mEndColor4 = getResources().getColor(R.color.end_color4);
        mEndColor5 = getResources().getColor(R.color.end_color5);
               
        mTextViewLeftUp.setBackgroundColor(mBeginColor1);
        mTextViewLeftBottom.setBackgroundColor(mBeginColor2);
        mTextViewRightUp.setBackgroundColor(mBeginColor3);
        mTextViewRightMid.setBackgroundColor(mBeginColor4);
        mTextViewRightBottom.setBackgroundColor(mBeginColor5); 
               
        mSeekBar = (SeekBar)findViewById(R.id.colorSeekBar);
        mSeekBar.setMax(SEEKBAR_MAX);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Stop Tracking Touch("+seekBar.getProgress()+")");
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Start Tracking Touch("+seekBar.getProgress()+")");

			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Log.i(TAG, "on Progress Changed("+progress+")");
				// mTextViewLeftUp.setBackgroundColor((mColor1+progress) % SEEKBAR_MAX);
				
				// update Block Color
				mTextViewLeftUp.setBackgroundColor(changeColor(mBeginColor1, mEndColor1,progress));
		        mTextViewLeftBottom.setBackgroundColor(changeColor(mBeginColor2, mEndColor2, progress));
		        mTextViewRightUp.setBackgroundColor(changeColor(mBeginColor3, mEndColor3,progress));
		        // mTextViewRightMid.setBackgroundColor(changeColor(mBeginColor4, mEndColor4, progress));
		        mTextViewRightBottom.setBackgroundColor(changeColor(mBeginColor5, mEndColor5, progress)); 
			}
		}  ); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.color_block, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
        case  R.id.menu_moreInfo: 
        	mDialog = new CustomDialog(this, R.style.MyAlertDialogStyle);
        	mDialog.show();
        	
            return true;
        
        default:
        	return false;
        }
    }
    
    private void dispColor(int color) {
    	Log.i(TAG, "color:"+Color.red(color)+","+Color.green(color)+","+Color.blue(color));
    }
    
    private int changeColor(int beginColor, int endColor, int unit) {
        int red = Color.red(beginColor) + (Color.red(endColor) - Color.red(beginColor)) * unit / SEEKBAR_MAX;
        int green = Color.green(beginColor) + (Color.green(endColor) - Color.green(beginColor)) * unit / SEEKBAR_MAX;
        int blue = Color.blue(beginColor) + (Color.blue(endColor) - Color.blue(beginColor)) * unit / SEEKBAR_MAX;
        int alpha = Color.alpha(beginColor) + (Color.alpha(endColor) - Color.alpha(beginColor)) * unit / SEEKBAR_MAX;
        
        return Color.argb(alpha, red, green, blue);     
    }
    
    
	void gotoMONA(boolean shouldContinue) {
		if (shouldContinue) {
			Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
			
			// TODO - Create a chooser intent, for choosing which Activity
			// will carry out the baseIntent
			// (HINT: Use the Intent class' createChooser() method)
			// Intent chooserIntent = null;
	        Intent chooserIntent = Intent.createChooser(baseIntent, CHOOSER_TEXT);
	        
			Log.i(TAG,"Chooser Intent Action:" + chooserIntent.getAction());
	        
	        
			// TODO - Start the chooser Activity, using the chooser intent
			startActivity(chooserIntent);
		
		
		} else {

			// Abort ShutDown and dismiss dialog
			mDialog.dismiss();
			
		}
	}
	
	public class CustomDialog extends Dialog {
		private Context mContext;
		private int mTheme;
		
		public CustomDialog(Context context, int theme) {
			super(context, theme);
			
			mContext = context;
			mTheme = theme;
			// TODO Auto-generated constructor stub
		}

		 protected void onCreate(Bundle savedInstanceState) {  
			super.onCreate(savedInstanceState);  
			setContentView(R.layout.custom_dialog);  
			    	
			final Button yesButton = (Button)findViewById(R.id.positiveButton);

			// Set an OnClickListener on this Button
			// Called each time the user clicks the Button		
			yesButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					gotoMONA(true);
					
				}
			});
    	
			final Button noButton = (Button)findViewById(R.id.negativeButton);
			// Set an OnClickListener on this Button
			// Called each time the user clicks the Button
	    	noButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					gotoMONA(false);
					
				}
			});
	    	
	  }
	}
}
