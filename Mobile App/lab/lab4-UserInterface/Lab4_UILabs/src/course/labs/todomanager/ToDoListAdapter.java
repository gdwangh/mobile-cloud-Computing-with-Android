package course.labs.todomanager;

import java.util.ArrayList;
import java.util.List;

import course.labs.todomanager.ToDoItem.Status;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToDoListAdapter extends BaseAdapter {

	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	private final Context mContext;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context) {

		mContext = context;

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {
		Log.i(TAG, "enter ToDoListAdapter.add()"+item.getTitle());
		
		mItems.add(item);
		notifyDataSetChanged();
	}

	// Clears the list adapter of all items.

	public void clear() {

		mItems.clear();
		notifyDataSetChanged();

	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	// Create a View for the ToDoItem at specified position
	// Remember to check whether convertView holds an already allocated View
	// before created a new View.
	// Consider using the ViewHolder pattern to make scrolling more efficient
	// See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "enter getView(position="+position+")");
		
		// TODO - Get the current ToDoItem
		// final ToDoItem toDoItem = null;
		final ToDoItem toDoItem = (ToDoItem)getItem(position);
		
		ViewHolder vholder;
		
		// TODO - Inflate the View for this ToDoItem
		// from todo_item.xml
		// RelativeLayout itemLayout = null;
		RelativeLayout itemLayout = (RelativeLayout)convertView;
		
		if (itemLayout == null) {
			vholder = new ViewHolder();

			itemLayout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.todo_item, null);
			
			vholder.titleView = (TextView)itemLayout.findViewById(R.id.titleView);
			vholder.statusLabel = (TextView)itemLayout.findViewById(R.id.StatusLabel);
	        vholder.statusCheckBox = (CheckBox)itemLayout.findViewById(R.id.statusCheckBox);
	        vholder.PriorityLabel = (TextView)itemLayout.findViewById(R.id.PriorityLabel);
	        vholder.priorityView = (TextView)itemLayout.findViewById(R.id.priorityView);
	        vholder.DateLabel = (TextView)itemLayout.findViewById(R.id.DateLabel);
	        vholder.dateView = (TextView)itemLayout.findViewById(R.id.dateView);
	        
	        itemLayout.setTag(vholder);
			
		}  else {
			vholder = (ViewHolder)itemLayout.getTag();
		}
		
		// Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined
		// in the layout file

		// TODO - Display Title in TextView
		// final TextView titleView = null;
		vholder.titleView.setText(toDoItem.getTitle());

		// TODO - Set up Status CheckBox
		// final CheckBox statusView = null;
		final CheckBox statusView = vholder.statusCheckBox;
		// Log.i(TAG, "toDoItem.getXXX(): "+toDoItem.getStatus());

		switch (toDoItem.getStatus()) {
		case DONE: {	
				statusView.setChecked(true); 
				break;
			}
		case NOTDONE: {	
				statusView.setChecked(false); 
				break;
			}
		}
		
		// TODO - Must also set up an OnCheckedChangeListener,
		// which is called when the user toggles the status checkbox
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// Log.i(TAG, "click CheckedBox:"+buttonView);
						if (isChecked) {
							toDoItem.setStatus(Status.DONE);
						}  else {
							toDoItem.setStatus(Status.NOTDONE);
						}
						                    
                        
                        
					}
				});

		// TODO - Display Priority in a TextView
		// final TextView priorityView = null;
		final TextView priorityView = vholder.priorityView;
		//Log.i(TAG, "ToDoItem.getXXX(): "+toDoItem.getPriority());

		switch (toDoItem.getPriority()) {
		case LOW: {
			priorityView.setText("LOW");
			break;
		  }
		case MED: {
			priorityView.setText("MED");
			break;
		  }
		case HIGH:{
			priorityView.setText("HIGH");
			break;
		  }
		default: {
			priorityView.setText("unkown");
		  }
		}

		
		// TODO - Display Time and Date.
		// Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and
		// time String
		// final TextView dateView = null;
		vholder.dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));

		// Return the View you just created
		return itemLayout;

	}
	
	public static class ViewHolder {
        public TextView titleView;
        public TextView statusLabel;
        public CheckBox statusCheckBox;
        public TextView PriorityLabel;
        public TextView priorityView;
        public TextView DateLabel;
        public TextView dateView;
    }
}
