package com.AmiCity.Planner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.widget.Toast;

public class TaskShareManager {
	private Task m_taskToShare;
	Context m_context;
	
	TaskShareManager(Context context)
	{
		m_context = context;
	}
	
	
	public void ShareTaskByEmail(Task taskToShare)
	{
		String arhivePath = CreateTaskArhive(taskToShare);
		String subject = "You have received a new task";
		String emailtext = taskToShare.GetDescription();
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		/*Add email data*/
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://" + arhivePath));
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext);
		/*Start email Activity*/
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try
		{
			m_context.startActivity(emailIntent);		
		} 
		catch (Throwable t) 
		{
			Toast.makeText(m_context, "Request failed: " + t.toString(), Toast.LENGTH_LONG).show();
		}
	}
	/*We parse the task and add its serialization and dependencies to a zip arhive 
	 Returns the arhive path*/
	private String CreateTaskArhive(Task taskToArhive)
	{
		ZipOutputStream zos = null;
		String filename = Environment.getExternalStorageDirectory().getPath() + "/AmiCity/" + "task_" + System.currentTimeMillis();
		try {
			File outputTaskFile = new File(filename);
			OutputStream os = new FileOutputStream(outputTaskFile);
			zos = new ZipOutputStream(new BufferedOutputStream(os));
		    for(String path : taskToArhive.GetFilePaths())
		    {
		    	PutFileInArhive(zos,path);
		    }
		    for(String path : taskToArhive.GetImagePaths())
		    {
		    	PutFileInArhive(zos,path);
		    }
		    
		    File tempMainFile = CreateTaskMainFile(taskToArhive);
		    PutFileInArhive(zos,tempMainFile.getPath());
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		     try {
				zos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		return filename;
		
	}
	
	private File CreateTaskMainFile(Task task)
	{
		File taskFile = new File(m_context.getFilesDir().getPath() + "/TempTask");
		/*We must save without the full paths*/
		Task taskToSave = new Task();
		for(String path : task.GetFilePaths())
		{
			taskToSave.AddNewFilePath(Task.GetFileNameFromPath(path));
		}
		for(String path : task.GetImagePaths())
		{
			taskToSave.AddNewFilePath(Task.GetFileNameFromPath(path));
		}
		for(GregorianCalendar calendar : task.GetNotifications())
		{
			taskToSave.AddNewNotification((GregorianCalendar)calendar.clone());
		}
		
		/*Serialize the task*/
		Gson serializer = new Gson();
		String serializedString = serializer.toJson(taskToSave);
		try {
			FileOutputStream stream = new FileOutputStream(taskFile);
			stream.write(serializedString.getBytes());;
			stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Todo: add the serialized task */
		return taskFile;
	}
	
	private void PutFileInArhive(ZipOutputStream zos,String path)
	{
		File fileToAdd = new File(path);
		byte[] data;
		try {
			data = readFile(fileToAdd);
			
			String filename = Task.GetFileNameFromPath(path);
	        ZipEntry entry = new ZipEntry(filename);
	        zos.putNextEntry(entry);
	        zos.write(data);
	        zos.closeEntry();
		}
        catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
}
