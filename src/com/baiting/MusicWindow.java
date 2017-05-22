package com.baiting;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.baiting.menu.CloseWindow;

/**
 * 窗口
 * @author lmq
 *
 */
public abstract class MusicWindow extends Music {
	
	protected MusicFrame musicFrame;
	
	private String title;
	private int locationX;
	private int locationY;
	
	public MusicWindow() {
		title = getConfigMap().get("title").toString();
		defaultLocation();
	}
	
	public MusicWindow(String title,int width,int height) {
		this.title = title;
		setWidth(width);
		setHeight(height);
		defaultLocation();
	}
	
	public MusicWindow(String title,int width,int height,int locationX,int locationY) {
		this.title = title;
		setWidth(width);
		setHeight(height);
		this.locationX = locationX;
		this.locationY = locationY;
	}
	
	private void defaultLocation() {
		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		locationX = (screenSize.width-getWidth())/2;
		locationY = (screenSize.height-getHeight())/2;
	}
	
	protected MusicFrame createWindow() {
		musicFrame = new MusicFrame();
		musicFrame.setTitle(title);
		musicFrame.setSize(getWidth(), getHeight());
		//musicFrame.setLocation(locationX, locationY);
		musicFrame.setLocationRelativeTo(null);
		musicFrame.addWindowListener(new CloseWindow());
		musicFrame.setMinimumSize(new Dimension(600, 450));
		musicFrame.setVisible(true);
		return musicFrame;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	
	

	
}
