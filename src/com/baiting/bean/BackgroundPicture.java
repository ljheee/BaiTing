package com.baiting.bean;

/**
 * 背景图片
 * @author lev
 *
 */
public class BackgroundPicture {

	/**
	 * 图片路径
	 */
	private String path;
	
	/**
	 * 是否为本地图片(true--表示本地图片；false--表示网络图片(http))
	 */
	private boolean isLocal;
	
	private long height;
	
	private long width;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

}
