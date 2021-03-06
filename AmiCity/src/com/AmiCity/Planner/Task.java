package com.AmiCity.Planner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import android.R.integer;

public class Task {
	private ArrayList<String> m_filePaths;
	private ArrayList<String> m_imagePaths;
	private ArrayList<String> m_messages;
	private ArrayList<Integer> m_contactIDs;
	private ArrayList<GregorianCalendar > m_notifications;
	private String m_taskDescription;
	int m_priority;
	private UUID m_uuid;
	
	/*The archieve from which the task was imported*/
	public String ImportedFrom;
	
	public Task()
	{
		ImportedFrom = "";
		m_uuid = null;
		m_taskDescription = "";
		m_filePaths = new ArrayList<String>();
		m_imagePaths = new ArrayList<String>();
		m_contactIDs = new ArrayList<Integer>();
		m_notifications = new ArrayList<GregorianCalendar>();
		m_priority = 1;
	}
	
	public boolean AddNewNotification(GregorianCalendar calendar)
	{
		return m_notifications.add(calendar);
	}
	
	public ArrayList<GregorianCalendar> GetNotifications()
	{
		return m_notifications;
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
	
	static String GetFileNameFromPath(String fullPath)
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
		
		/*extra check for error case*/
		if(((Task) obj).GetUUID() == null || m_uuid == null)
			return true;
		
		return this.GetUUID().compareTo(((Task) obj).GetUUID()) == 0;
	}
	
}
