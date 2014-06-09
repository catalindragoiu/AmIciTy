package com.AmiCity.Planner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


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
		if(getItem(position).GetType() != TileItem.TileType.MESSAGE)
		{
			ScaleImageView imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
			imageView.setImageDrawable(getItem(position).GetDrawable());
			TextView messageEditText= (TextView) convertView .findViewById(R.id.Message);
			messageEditText.setVisibility(android.view.View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {
		ScaleImageView imageView;
	}
}
