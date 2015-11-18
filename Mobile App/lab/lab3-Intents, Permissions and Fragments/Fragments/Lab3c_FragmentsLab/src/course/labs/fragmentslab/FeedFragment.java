package course.labs.fragmentslab;

import course.labs.fragmentslab.FriendsFragment.SelectionListener;
import android.app.Activity;
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		Log.i(TAG, "Monitor FeedFragment lifecycle: onAttach()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Monitor FeedFragment lifecycle: onCreate()");
		
		// Retain this Fragment across Activity reconfigurations
		setRetainInstance(true);
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "Monitor FeedFragment lifecycle: onCreateView()");
		
		return inflater.inflate(R.layout.feed, container, false);

	} 

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "Monitor FeedFragment lifecycle: onActivityCreated()");

		// Read in all Twitter feeds 
		if (null == feedFragmentData) {  // create 
			
			feedFragmentData = new FeedFragmentData(getActivity());

		}  else {   // recover
			
			updateFeedDisplay(mposition);
		}
		
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onStop()");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onDestroyView()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

		Log.i(TAG, "Monitor FeedFragment lifecycle: onDetach()");
	}
	
	// Display Twitter feed for selected feed

	void updateFeedDisplay(int position) {

		Log.i(TAG, "Entered updateFeedDisplay()");
				
		mTextView = (TextView) getView().findViewById(R.id.feed_view);
		mTextView.setText(feedFragmentData.getFeed(position));

		mposition = position;
	}

}
