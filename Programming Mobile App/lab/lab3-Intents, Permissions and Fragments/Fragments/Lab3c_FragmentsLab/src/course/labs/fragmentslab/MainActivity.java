package course.labs.fragmentslab;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements
		FriendsFragment.SelectionListener {

	private static final String TAG = "Lab-Fragments";

	private FriendsFragment mFriendsFragment;
	private FeedFragment mFeedFragment;
	
	private static final String FRIENDS_FRAGMENT_TAG = "Friends-Fragment";
	private static final String FEED_FRAGMENT_TAG = "Feed-Fragment";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		Log.i(TAG, "Monitor Activity lifecycle: onCreate()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		

		// If the layout is single-pane, create the FriendsFragment 
		// and add it to the Activity

		// if single-pane����Ҫ�ֹ���frangment�ӵ�container��
		// if two-pane��ʵ���Ǿ�̬��ʽ����frangment
		
		if (!isInTwoPaneMode()) {
			mFriendsFragment = (FriendsFragment)getFragmentManager().findFragmentByTag(FRIENDS_FRAGMENT_TAG);
			mFeedFragment = (FeedFragment)getFragmentManager().findFragmentByTag(FEED_FRAGMENT_TAG);

			//TODO 1 - add the FriendsFragment to the fragment_container
			
			// Start a new FragmentTransaction
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

			
			if (mFriendsFragment == null) {  // first enter and new 

				mFriendsFragment = new FriendsFragment();

				// Add the TitleFragment to the layout
				fragmentTransaction.add(R.id.fragment_container, mFriendsFragment, FRIENDS_FRAGMENT_TAG);
				
			}  else {  // recover
				
				if (mFriendsFragment.isHidden())  {
					fragmentTransaction.show(mFeedFragment);
				}  else {
					fragmentTransaction.show(mFriendsFragment);
				}
			
			}
			
			// Commit the FragmentTransaction
			fragmentTransaction.commit();
						
		} else {

			// Otherwise, save a reference to the FeedFragment for later use

			mFeedFragment = (FeedFragment) getFragmentManager()
					.findFragmentById(R.id.feed_frag);
		}
	}

	@Override  
    protected void onStart() {  
		Log.i(TAG, "Monitor Activity lifecycle: onStart()");

        super.onStart();  
    }
	
    protected void onRestart() {  
		Log.i(TAG, "Monitor Activity lifecycle: onRestart()");

        super.onRestart();  
    }
    
    protected void onResume() {  
		Log.i(TAG, "Monitor Activity lifecycle: onResume()");

        super.onResume();  
    }
 
    @Override  
    protected void onPause() {  
		Log.i(TAG, "Monitor Activity lifecycle: onPause()");

        super.onPause();  
    }
    
    @Override  
    protected void onStop() {  
		Log.i(TAG, "Monitor Activity lifecycle: onStop()");

        super.onStop();  
    }
    
    @Override  
    protected void onDestroy() {  
		Log.i(TAG, "Monitor Activity lifecycle: onDestroy()");

        super.onDestroy();  
    }
    
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
		Log.i(TAG, "Monitor Activity lifecycle: onSaveInstanceState()");

        super.onSaveInstanceState(outState);  
    }  

    @Override  
    protected void onRestoreInstanceState(Bundle outState) {  
		Log.i(TAG, "Monitor Activity lifecycle: onRestoreInstanceState()");

        super.onRestoreInstanceState(outState);  
    }  
    
	// If there is no fragment_container ID, then the application is in
	// two-pane mode

	private boolean isInTwoPaneMode() {

		return findViewById(R.id.fragment_container) == null;
	
	}

	// Display selected Twitter feed

	public void onItemSelected(int position) {

		Log.i(TAG, "Entered onItemSelected(" + position + ")");

		// If there is no FeedFragment instance, then create one

		if (mFeedFragment == null)
			mFeedFragment = new FeedFragment();

		// If in single-pane mode, replace single visible Fragment

		if (!isInTwoPaneMode()) {
			
			//TODO 2 - replace the fragment_container with the FeedFragment
			// Start a new FragmentTransaction
			FragmentTransaction fragmentTransaction =  getFragmentManager()
														.beginTransaction();
			
			fragmentTransaction.hide(mFriendsFragment);

			if (!mFeedFragment.isAdded())  { // first enter and new
				// replace  the TitleFragment to the layout
				fragmentTransaction.add(R.id.fragment_container, mFeedFragment,FEED_FRAGMENT_TAG);  
				
			}  else { 
				fragmentTransaction.show(mFeedFragment);  
			}
			
			// Add this FragmentTransaction to the backstack
			fragmentTransaction.addToBackStack(null);
						
			// Commit the FragmentTransaction
			fragmentTransaction.commit();

			// execute transaction now
			getFragmentManager().executePendingTransactions();

		}

		// Update Twitter feed display on FriendFragment
		mFeedFragment.updateFeedDisplay(position);

	}
	
}

	
