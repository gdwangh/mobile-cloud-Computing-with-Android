package course.labs.fragmentslab;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeedFragment extends Fragment {

	private static final String TAG = "Lab-Fragments";

	private TextView mTextView;
	private static FeedFragmentData feedFragmentData;
	private int mposition=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Retain this Fragment across Activity reconfigurations
		setRetainInstance(true);
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.feed, container, false);

	} 

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Read in all Twitter feeds 
		if (null == feedFragmentData) {  // create 
			
			feedFragmentData = new FeedFragmentData(getActivity());

		}  else {   // recover
			
			updateFeedDisplay(mposition);
		}
		
	}


	// Display Twitter feed for selected feed

	void updateFeedDisplay(int position) {

		Log.i(TAG, "Entered updateFeedDisplay()");
				
		mTextView = (TextView) getView().findViewById(R.id.feed_view);
		mTextView.setText(feedFragmentData.getFeed(position));

		mposition = position;
	}

}
