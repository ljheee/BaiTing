package com.baiting.bean;

/**
 * 下载失败
 * @author lmq
 *
 */
public class DownFailSong implements IBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558287859371109053L;

	private int no;
	
	private String songName;
	
	private String singer;
	
	private String format;
	
	private String failTime;

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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFailTime() {
		return failTime;
	}

	public void setFailTime(String failTime) {
		this.failTime = failTime;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
	
}
