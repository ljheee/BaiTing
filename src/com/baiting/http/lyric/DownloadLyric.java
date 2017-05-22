package com.baiting.http.lyric;

import com.baiting.bean.Song;
import com.baiting.http.Download;
import com.baiting.http.HttpRequest;
import com.baiting.service.MusicPlayerService;

public class DownloadLyric extends Download {
	
	protected HttpRequest request;
	
	public DownloadLyric() {
		super();
		request = new HttpRequest();
	}
	
	public DownloadLyric(Song song) {
		super(song);
		request = new HttpRequest();
	}
	
	public DownloadLyric(Song song,String url) {
		super(song, url);
		request = new HttpRequest();
	}
	
	
	public boolean startDownload() {
		
		return false;
	}

	@Override
	public void run() {
		try {
			if(this.startDownload()) {
				MusicPlayerService.getInstance().reloadLrc(song);
			} else {
				return;
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
		
	}

	public void setSong(Song song) {
		this.song = song;
	}
	
}
