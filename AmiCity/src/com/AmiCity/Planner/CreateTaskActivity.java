package com.AmiCity.Planner;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class CreateTaskActivity extends Activity {
	private static final int REQUEST_TAKE_PHOTO = 755; 
	private static final int REQUEST_ATTACH_FILE = 756;
	private ArrayList<String> m_attachedFiles;
	private ImageView imageContainer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		m_attachedFiles = new ArrayList<String>();
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
		this.imageContainer = (ImageView)this.findViewById(R.id.ImgContainer);
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
			Task newTask = new Task();
			for(String filePath : m_attachedFiles)
			{
				newTask.AddNewFilePath(filePath);
			}
			Gson gson = new Gson();
			String serialedTask = gson.toJson(newTask);
			/*TODO: Image Paths*/
			
			Intent databackIntent = new Intent(); 
			databackIntent.putExtra("new_task", serialedTask); 
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
            this.imageContainer.setImageBitmap(photo);
        }
        if (requestCode == REQUEST_ATTACH_FILE && resultCode == RESULT_OK) 
        {  
        	String fileName = (String) data.getExtras().get("file_name"); 
        	/*Perhaps add another list here to allow the user to upload more files*/
        	m_attachedFiles.add(fileName);
        	TextView fileNameText = (TextView)this.findViewById(R.id.AttachedFile);
        	fileNameText.setText(fileName);
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
