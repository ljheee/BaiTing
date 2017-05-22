package com.baiting.test;

import com.baiting.bean.Song;
import com.baiting.http.picture.DownloadPictureSogou;

public class BaiduTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Song songTmp = new Song();
		songTmp.setName("");
		songTmp.setSinger("任贤齐");
		DownloadPictureSogou pic = new DownloadPictureSogou(songTmp);
		pic.startDownload();
	}

}
