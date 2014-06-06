package com.AmiCity.Planner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class StaggeredAdapter extends ArrayAdapter<TileItem> {


	public StaggeredAdapter(Context context, int textViewResourceId,
			TileItem[] objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
					null);
			
			
		}
		ScaleImageView imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
		imageView.setImageDrawable(getItem(position).GetDrawable());
		return convertView;
	}

	static class ViewHolder {
		ScaleImageView imageView;
	}
}
