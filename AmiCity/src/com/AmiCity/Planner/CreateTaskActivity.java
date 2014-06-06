package com.AmiCity.Planner;

import java.util.ArrayList;
import java.util.HashSet;

import com.origamilabs.library.views.StaggeredGridView;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class CreateTaskActivity extends Activity {
	private static final int REQUEST_TAKE_PHOTO = 755; 
	private static final int REQUEST_ATTACH_FILE = 756;
	private Task m_task;
	private ImageView m_imageContainer;
	private EditText m_descriptionEditText;
	HashSet<TileItem> m_tileList;
	StaggeredGridView m_tileGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_task = new Task();
		m_tileList = new HashSet<TileItem>();
		setContentView(R.layout.activity_create_task);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_task, menu);
		inflater.inflate(R.menu.action_bar_new_task, menu);
		m_descriptionEditText = (EditText)findViewById(R.id.TaskDetails);
		//this.imageContainer = (ImageView)this.findViewById(R.id.ImgContainer);
		Bundle extras = getIntent().getExtras();
		
		m_tileGridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
		m_tileGridView.setItemMargin(0); // set the GridView margin
		m_tileGridView.setPadding(0, 0, 0, 0); // have the margin on the sides as well 
		
		if(extras != null)
		{
			String serializedTask = extras.getString("data");
			Gson deserializer = new Gson();
			m_task = deserializer.fromJson(serializedTask, Task.class);
			LoadTaskDataToUI();
		}
		
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		if (id == R.id.action_camera) 
		{
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO); 
			return true;
		}
		if (id == R.id.action_attach) 
		{
			Intent intent = new Intent(this, FilePicker.class);
	    	startActivityForResult(intent, REQUEST_ATTACH_FILE);
			return true;
		}
		if (id == R.id.action_accept) 
		{
			/*Return the newly created task*/
			EditText descriptionEditText = (EditText)findViewById(R.id.TaskDetails);
			m_task.SetDescription(descriptionEditText.getText().toString());
			Gson gson = new Gson();
			String serializedTask = gson.toJson(m_task);
			/*TODO: Image Paths*/
			
			Intent databackIntent = new Intent(); 
			databackIntent.putExtra("new_task", serializedTask); 
			setResult(Activity.RESULT_OK, databackIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{  
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) 
        {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            this.m_imageContainer.setImageBitmap(photo);
        }
        if (requestCode == REQUEST_ATTACH_FILE && resultCode == RESULT_OK) 
        {  
        	String fileName = (String) data.getExtras().get("file_name"); 
        	/*Perhaps add another list here to allow the user to upload more files*/
        	m_task.AddNewFilePath(fileName);
        	LoadTaskDataToUI();
        }
    }
	
	private void LoadTaskDataToUI()
	{
		m_descriptionEditText.setText(m_task.GetDescription());
		m_tileList.clear();
		if(m_task.GetFilePaths().size() > 0)
		{
			for (String file : m_task.GetFilePaths()) 
			{
				m_tileList.add(new TileItem(getResources().getDrawable(R.drawable.ic_action_attachment), file));
			}
		}
		
		if(m_tileList.size() > 0)
		{
			StaggeredAdapter adapter = new StaggeredAdapter(this, R.id.imageView1, m_tileList.toArray(new TileItem[m_tileList.size()]));
			m_tileGridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();	
		}
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_task,
					container, false);
			return rootView;
		}
	}

}
