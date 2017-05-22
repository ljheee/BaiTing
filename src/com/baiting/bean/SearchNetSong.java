package com.baiting.bean;

public class SearchNetSong implements IBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -898975524789555485L;

	private int no;
	
	private String sid;
	
	private String songName;
	
	private String singer;
	
	private String album;
	
	private String fileType;
	
	private String fileSize;
	
	/**
	 * 下载页面URL
	 */
	private String downPageUrl;
	

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getDownPageUrl() {
		return downPageUrl;
	}

	public void setDownPageUrl(String downPageUrl) {
		this.downPageUrl = downPageUrl;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}
