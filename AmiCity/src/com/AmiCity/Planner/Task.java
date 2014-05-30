package com.AmiCity.Planner;

import java.util.ArrayList;

public class Task {
	private ArrayList<String> m_filePaths;
	private ArrayList<String> m_imagePaths;
	private String m_taskDescription;
	int m_priority;
	public Task()
	{
		m_filePaths = new ArrayList<String>();
		m_imagePaths = new ArrayList<String>();
		m_priority = 1;
	}
	
	public boolean AddNewFilePath(String Path)
	{
		return m_filePaths.add(Path);
	}
	
	public boolean AddImagePath(String Path)
	{
		return m_imagePaths.add(Path);
	}
	
	void SetDescription(String description)
	{
		m_taskDescription = description;
	}
	
	String GetDescription()
	{
		return m_taskDescription;
	}
	
	ArrayList<String> GetFilePaths()
	{
		return m_filePaths;
	}
	
	String GetFileNameFromPath(String fullPath)
	{
		String[] splitPath = fullPath.split("/");
		return splitPath[splitPath.length -1];
	}
	
	int GetPriority()
	{
		return m_priority;
	}
	
	void SetPriority(int priority)
	{
		m_priority = priority;
	}
	
	
}
