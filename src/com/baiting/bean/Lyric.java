package com.baiting.bean;

import java.util.List;

/**
 * 歌词
 * @author lmq
 *
 */
public class Lyric implements IBaseBean {

	private static final long serialVersionUID = -5550325983260062071L;

	private String title;
	private String singer;
	private String album;
	private List<LyricStatement> LrcInfos;
	
	public Lyric() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<LyricStatement> getLrcInfos() {
		return LrcInfos;
	}

	public void setLrcInfos(List<LyricStatement> lrcInfos) {
		LrcInfos = lrcInfos;
	}

}
