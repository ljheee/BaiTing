package com.baiting.bean;

public class Song implements IBaseBean {

	private static final long serialVersionUID = 2836367062889016611L;
	
	/**
	 * 序号
	 */
	private int no;
	
	/**
	 * 歌曲名称
	 */
	private String name;
	
	/**
	 * 演唱者
	 */
	private String singer;
	
	/**
	 * 歌曲所在路径
	 */
	private String path;
	
	private String url;
	
	/**
	 * 歌曲长度
	 */
	private String longTime;
	
	/**
	 * 歌曲全名
	 */
	private String fullName;
	
	/**
	 * 播放列表名称
	 */
	private String playListName;
	
	/**
	 * 1--表示有歌词;0--表示正在下载歌词；-1--表示歌词下载失败;
	 */
	private int lrcState;
	
	
	
	public Song() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLongTime() {
		return longTime;
	}

	public void setLongTime(String longTime) {
		this.longTime = longTime;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getPlayListName() {
		return playListName;
	}

	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}

	public int getLrcState() {
		return lrcState;
	}

	public void setLrcState(int lrcState) {
		this.lrcState = lrcState;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
