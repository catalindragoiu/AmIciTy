package com.AmiCity.Planner;

import java.util.HashMap;

import android.graphics.drawable.Drawable;

public class TileItem {
	private Drawable m_drawableImage;
	private String m_text;
	private TileType m_tileType;
	/*A map where the user of the class can store usefull tile data as strings*/
	HashMap<String,String> values;
	
	public TileItem(Drawable image, String text, TileType type)
	{
		m_text = text;
		m_drawableImage = image;
		m_tileType = type;
		values = new HashMap<String, String>();
		
	}
	
	public TileType GetType()
	{
		return m_tileType;
	}
	
	public void SetType(TileType type)
	{
		m_tileType = type;
	}
	
	public String GetText()
	{
		return m_text;
	}
	
	public Drawable GetDrawable()
	{
		return m_drawableImage;
	}
	
	public enum TileType {
	    PHOTO, FILE, MESSAGE,CONTACT
	}
}

