package com.AmiCity.Planner;

import java.util.ArrayList;
import java.util.UUID;

public class TasksManager {
	private ArrayList<Task> m_tasks;
	
	public TasksManager()
	{
		m_tasks = new ArrayList<Task>();
	}
	
	public ArrayList<Task> GetTasks()
	{
		return m_tasks;
	}

	public void addTask(Task newTask) 
	{
		m_tasks.add(newTask);
	}
	public void removeTask(Task taskToRemove)
	{
		m_tasks.remove(taskToRemove);
	}
}
