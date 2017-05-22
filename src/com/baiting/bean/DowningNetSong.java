package com.baiting.bean;

public class DowningNetSong implements IBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5047560126114430646L;
	
	private int no;
	
	private String singer;
	
	private String songName;
	
	private String status;
	
	private String fileName;
	
	private String fileSize;
	
	private String url;
	
	/**
	 * 播放列表名称
	 */
	private String playListName;
	
	/**
	 * 列表中的序号
	 */
	private int listNo;
	
	/**
	 * 下载进度
	 */
	private String progress;
	
	/**
	 * 下载速度
	 */
	private String speed;
	
	/**
	 * 剩余时间
	 */
	private String remainTime;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(String remainTime) {
		this.remainTime = remainTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getPlayListName() {
		return playListName;
	}

	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}

	public int getListNo() {
		return listNo;
	}

	public void setListNo(int listNo) {
		this.listNo = listNo;
	}
	
	
}
