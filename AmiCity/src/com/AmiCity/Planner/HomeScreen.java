package com.AmiCity.Planner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.AmiCity.Planner.SwipeDismissListViewTouchListener.DismissCallbacks;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class HomeScreen extends Activity 
{
	private static final int REQUEST_NEW_TASK = 654;
	private static final String SaveFile = "storage.dat";
	
	private TasksArrayAdapter taskAdapter;
	TasksManager m_tasksManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	LoadState();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.home_screen, menu);
        inflater.inflate(R.menu.action_bar_menu, menu);
        
        if(m_tasksManager != null)
        {
			final ListView listView1 = (ListView)findViewById(R.id.TaskList);
			listView1.setClickable(true);
			taskAdapter = new TasksArrayAdapter(this,R.layout.file_view,m_tasksManager.GetTasks());
	        listView1.setAdapter(taskAdapter);
	        SwipeDismissListViewTouchListener touchListener =
	        		 new SwipeDismissListViewTouchListener(
	        				 listView1,
	        				 new DismissCallbacks() {
        					 		public void onDismiss(ListView listView, int[] reverseSortedPositions) 
        					 		{
						        		for (int position : reverseSortedPositions) 
						        		{
						        			Task taskToRemove = taskAdapter.getItem(position);
						        			m_tasksManager.removeTask(taskToRemove);
						        			taskAdapter.remove(taskToRemove);
						        		}
					        		 	taskAdapter.notifyDataSetChanged();
					        		 	SaveState();
					        		}

					@Override
					public boolean canDismiss(int position) {
						// TODO Auto-generated method stub
						return true;
					}
	        		 });
	        		 listView1.setOnTouchListener(touchListener);
	        		 listView1.setOnScrollListener(touchListener.makeScrollListener());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        if(id == R.id.action_new)
        {
        	StartNewTaskActivity();
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
            View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
            return rootView;
        }
    }
    
    public void LoadState()
    {
    	try
    	{
	    	FileInputStream in = openFileInput(SaveFile);
	        InputStreamReader inputStreamReader = new InputStreamReader(in);
	        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = bufferedReader.readLine()) != null) {
	            sb.append(line);
	        }
	        
	        Gson deserializer = new Gson();
	        m_tasksManager = deserializer.fromJson(sb.toString(), TasksManager.class);
	        
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		/*We must create the instance if it was not created by deserialization*/
    		if(m_tasksManager == null)
    		{
    			m_tasksManager = new TasksManager();
    		}
    	}
    }
    
    public void SaveState()
    {
    	/*Save the serialized tasks*/
    	Gson serializer = new Gson();
    	String serializedData = serializer.toJson(m_tasksManager);
    	
    	FileOutputStream outputStream;
		
    	try {
    	  outputStream = openFileOutput(SaveFile, Context.MODE_PRIVATE);
    	  outputStream.write(serializedData.getBytes());
    	  outputStream.close();
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }
    
    public void StartNewTaskActivity()
    {
    	Intent intent = new Intent(this, CreateTaskActivity.class);
    	startActivityForResult(intent,REQUEST_NEW_TASK);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{  
        if (requestCode == REQUEST_NEW_TASK && resultCode == RESULT_OK) 
        {  
        	String serializedTask = (String)data.getExtras().get("new_task");
        	Gson gson = new Gson();
        	Task newTask = gson.fromJson(serializedTask, Task.class);
        	m_tasksManager.addTask(newTask);
        	taskAdapter = new TasksArrayAdapter(this,R.layout.file_view,m_tasksManager.GetTasks());
        	
        	ListView listView1 = (ListView)findViewById(R.id.TaskList);
            listView1.setAdapter(taskAdapter);
            
            SaveState();
        }
    } 

}
