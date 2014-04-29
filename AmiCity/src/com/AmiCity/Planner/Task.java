package com.AmiCity.Planner;

import java.util.ArrayList;

public class Task {
	
	public Task()
	{
		m_filePaths = new ArrayList<String>();
		m_imagePaths = new ArrayList<String>();
	}
	
	public boolean AddNewFilePath(String Path)
	{
		return m_filePaths.add(Path);
	}
	
	public boolean AddImagePath(String Path)
	{
		return m_imagePaths.add(Path);
	}
	
	ArrayList<String> m_filePaths;
	ArrayList<String> m_imagePaths;
}
