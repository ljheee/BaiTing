package com.baiting.http;

import com.baiting.Music;
import com.baiting.bean.Song;

/**
 * 下载
 * @author lmq
 *
 */
public class Download extends Music implements Runnable {
	
	   protected String url;
	   protected Song song;
	   
	   public Download() {
		   
	   }
	   
	   public Download(Song song) {
		   this.song = song;
	   }
	   
	   public Download(Song song,String url) {
		   this.song = song;
		   this.url = url;
	   }
	
	  protected boolean startDownload() {
		  return false;
	  }

	@Override
	public void run() {
		
	} 
	
	
}
