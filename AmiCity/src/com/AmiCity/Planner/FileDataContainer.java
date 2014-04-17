package com.AmiCity.Planner;

public class FileDataContainer implements Comparable<FileDataContainer>{

	private String m_fileName;
    private String m_fileData;
    private String m_filePath;
    
    public FileDataContainer(String fName,String fData,String fPath)
    {
    	m_fileName = fName;
    	m_fileData = fData;
    	m_filePath = fPath;
    }
    
    public String getName()
    {
        return m_fileName;
    }
    
    public String getData()
    {
        return m_fileData;
    }
    
    public String getPath()
    {
        return m_filePath;
    }
    
    @Override
    public int compareTo(FileDataContainer other) 
    {
        if(this.m_fileName != null)
            return this.m_fileName.toLowerCase().compareTo(other.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }

}
