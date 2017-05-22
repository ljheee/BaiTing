package com.baiting.bean;

public class PlayList {
	
	/**
	 * 文件名(按第一个字段 )
	 */
	public static final int PLAYLIST_FILE = 0;
	
	/**
	 * 播放列表名称（按第二个字段）
	 */
	public static final int PLAYLIST_NAME = 1;
	
	private String fileName;
	private String playListName;
	
	public PlayList() {
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPlayListName() {
		return playListName;
	}

	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}
	
	

}
