package com.AmiCity.Planner;

import android.graphics.drawable.Drawable;

public class TileItem {
	private Drawable m_drawableImage;
	private String m_text;
	
	public TileItem(Drawable image, String text)
	{
		m_text = text;
		m_drawableImage = image;
	}
	
	public String GetText()
	{
		return m_text;
	}
	
	public Drawable GetDrawable()
	{
		return m_drawableImage;
	}
}
