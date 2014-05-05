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
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class HomeScreen extends Activity 
{
	private static final int REQUEST_NEW_TASK = 654;
	ArrayList<Task> m_tasks;
	private TasksArrayAdapter taskAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	m_tasks = new ArrayList<Task>();
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
        
		final ListView listView1 = (ListView)findViewById(R.id.TaskList);
		listView1.setClickable(true);
		taskAdapter = new TasksArrayAdapter(this,R.layout.file_view,m_tasks);
        listView1.setAdapter(taskAdapter);
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
        	m_tasks.add(newTask);
        	taskAdapter = new TasksArrayAdapter(this,R.layout.file_view,m_tasks);
        	
        	ListView listView1 = (ListView)findViewById(R.id.TaskList);
            listView1.setAdapter(taskAdapter);
        }
    } 

}
