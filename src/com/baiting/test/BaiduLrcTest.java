package com.baiting.test;

import com.baiting.bean.Song;
import com.baiting.http.lyric.DownloadLyricBaidu;

public class BaiduLrcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Song songTmp = new Song();
		songTmp.setName("陪着我的时候想着她");
		songTmp.setSinger("郭静");
		DownloadLyricBaidu lrc = new DownloadLyricBaidu(songTmp);
		lrc.startDownload();
	}

}
