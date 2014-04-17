package com.AmiCity.Planner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Build;

public class FilePicker extends Activity {
	private File currentFolder;
	private FilesArrayAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_picker);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_picker, menu);
		final ListView listView1 = (ListView)findViewById(R.id.FileList);
		listView1.setClickable(true);
		listView1.setOnItemClickListener(
				new AdapterView.OnItemClickListener() 
				{
					public void onItemClick(AdapterView<?> adapter,View view, int position, long arg3) 
					{
						// TODO Auto-generated method stub
						//super.onListItemClick(l, v, position, id);
						FileDataContainer o = (FileDataContainer)listView1.getItemAtPosition(position);
						if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
								currentFolder = new File(o.getPath());
								LoadFilesFromFolder(currentFolder);
						}
						else
						{
							//TODO: Select file
						}
					}
				});
		currentFolder = Environment.getExternalStorageDirectory();
		LoadFilesFromFolder(currentFolder);
		return true;
	}
	
	private void LoadFilesFromFolder(File currentFolder)
	{
		/*Inform user of current folder*/
		setTitle("Current Dir: "+currentFolder.getName());
		/*Get contained files*/
		File[] containedFiles = currentFolder.listFiles();
		
		 List<FileDataContainer>dir = new ArrayList<FileDataContainer>();
         List<FileDataContainer>fls = new ArrayList<FileDataContainer>();
         try
         {
             for(File currentFile: containedFiles)
             {
                if(currentFile.isDirectory())
                    dir.add(new FileDataContainer(currentFile.getName(),"Folder",currentFile.getAbsolutePath()));
                else
                {
                    fls.add(new FileDataContainer(currentFile.getName(),"File Size: "+currentFile.length(),currentFile.getAbsolutePath()));
                }
             }
         }
         catch(Exception e)
         {
             
         }
         Collections.sort(dir);
         Collections.sort(fls);
         dir.addAll(fls);
         if(!currentFolder.getName().equalsIgnoreCase("sdcard"))
             dir.add(0,new FileDataContainer("..","Parent Directory",currentFolder.getParent()));
         
         adapter = new FilesArrayAdapter(this,R.layout.file_view,dir);
 		 ListView listView1 = (ListView)findViewById(R.id.FileList);
         listView1.setAdapter(adapter);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.fragment_file_picker,
					container, false);
			
			return rootView;
		}
		
		
	}
	
	


}
