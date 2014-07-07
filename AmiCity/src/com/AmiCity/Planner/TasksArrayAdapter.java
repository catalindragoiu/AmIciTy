package com.AmiCity.Planner;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
                  ImageView attachImage = (ImageView)v.findViewById(R.id.AttachIcon);
                  ImageView listImage = (ImageView)v.findViewById(R.id.PriorityImage);
                  if(t1!=null && o.GetDescription() != null && o.GetDescription().length() > 0)
                  	t1.setText(o.GetDescription());
                  if(attachImage!=null && o.GetFilePaths().size() > 0)
                	  attachImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_attachment));
                  switch(o.GetPriority())
                  {
                  //lowest
                  case 0:
                  {
                	  listImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.green_ball));
                	  break;
                  }
                  case 1:
                  {
                	  listImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yellow_ball));
                	  break;
                  }
                  case 2:
                  {
                	  listImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_ball));
                	  break;
                  }
                  }
          }
          return v;
	 }

}

