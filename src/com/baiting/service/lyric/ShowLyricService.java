package com.baiting.service.lyric;

import com.baiting.layout.LyricDisplayer;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.MusicService;

public class ShowLyricService extends MusicService implements Runnable {

	private LyricDisplayer showLyric;
	private int index;
	private int preIndex;
	private long time = 0;
	private MusicPlayerService player;
	
	public ShowLyricService(MusicPlayerService player,LyricDisplayer showLyric) {
		this.player = player;
		this.showLyric = showLyric;
	}
	
	
	@Override
	public void run() {
		while(player.getStatus() != STOP_STATUS ) {
			if(index != preIndex && "lrcLabel".equals(tabSelectedName)) {
				showLyric.displayLyric(index);
				preIndex = index;
			} else if("lrcLabel".equals(tabSelectedName) && time>10*1000) {
				showLyric.setChangeBk(true);
				showLyric.displayLyric(index);
				time = 0;
			}
		    try {
			  Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		    time +=100;
		}
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
