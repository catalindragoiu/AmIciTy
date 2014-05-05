package com.AmiCity.Planner;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TasksArrayAdapter extends ArrayAdapter<Task>{

	private Context mContext;
	private int mID;
	private List<Task>mItems;
	
	public TasksArrayAdapter(Context context, int textViewResourceId,
			List<Task> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
		mID = textViewResourceId;
		mItems = objects;
	}
	
	public Task getItem(int i)
	 {
		 return mItems.get(i);
	 }
	 @Override
	public View getView(int position, View convertView, ViewGroup parent) {
          View v = convertView;
          if (v == null) {
              LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              v = vi.inflate(mID, null);
          }
          final Task o = mItems.get(position);
          if (o != null) {
                  TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                  TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                  
                  if(t1!=null && o.GetDescription().length() > 0)
                  	t1.setText(o.GetDescription());
                  if(t2!=null && o.m_filePaths.size() > 0)
                  	t2.setText(o.m_filePaths.get(0));
                  
          }
          return v;
	 }

}

