package com.AmiCity.Planner;

import java.util.ArrayList;
import java.util.UUID;

import android.R.integer;

public class Task {
	private ArrayList<String> m_filePaths;
	private ArrayList<String> m_imagePaths;
	private ArrayList<String> m_messages;
	private ArrayList<Integer> m_contactIDs;
	private String m_taskDescription;
	int m_priority;
	private UUID m_uuid;
	
	
	public Task()
	{
		m_filePaths = new ArrayList<String>();
		m_imagePaths = new ArrayList<String>();
		m_contactIDs = new ArrayList<Integer>();
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
	
	ArrayList<String> GetImagePaths()
	{
		return m_imagePaths;
	}
	
	ArrayList<Integer> GetContacts()
	{
		return m_contactIDs;
	}
	
	void AddContact(int ContactID)
	{
		m_contactIDs.add(ContactID);
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
	
	void SetUUID(UUID value)
	{
		m_uuid = value;
	}
	
	UUID GetUUID()
	{
		return m_uuid;
		
	}
	
	public boolean equals(Object obj) 
	{
		if (!(obj instanceof Task))
            return false;
		
		return this.GetUUID().compareTo(((Task)obj).GetUUID()) == 0;
	}
	
}
