package com.baiting.service;

import com.baiting.bean.NetSong;
import com.baiting.http.netsong.SongUrlHtmlParse;
import com.baiting.layout.MusicListLayout;

public class NetSongService extends MusicService {

	/**
	 * 添加网络歌曲到播放列表
	 * @return
	 */
	public void addNetSongToPlayList(NetSong netSong,String downPageUrl) {
		
		SongUrlHtmlParse htmlParse = new SongUrlHtmlParse(netSong,SongUrlHtmlParse.FLAG_ADD,downPageUrl);
		Thread mThread = new Thread(htmlParse);
		mThread.start();
	}
	
	/**
	 * 试听网络歌曲
	 * @param netSong
	 */
	public void playNetSong(NetSong netSong,String downPageUrl) {
		MusicListLayout.showMsg(getConfigMap().get("loading.song.msg").toString());
		SongUrlHtmlParse htmlParse = new SongUrlHtmlParse(netSong,SongUrlHtmlParse.FLAG_LISTENING,downPageUrl);
		Thread mThread = new Thread(htmlParse);
		mThread.start();
	}
	
	/**
	 * 下载网络歌曲
	 * @param netSong
	 */
	public void downloadNetSong(NetSong netSong,String downPageUrl) {
		MusicListLayout.showMsg(getConfigMap().get("loading.song.down.address.msg").toString());
		SongUrlHtmlParse htmlParse = new SongUrlHtmlParse(netSong,SongUrlHtmlParse.FLAG_DOWNLOAD,downPageUrl);
		Thread mThread = new Thread(htmlParse);
		mThread.start();
	}
}
