package com.baiting.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.baiting.Music;
import com.baiting.bean.Song;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.SongListService;

public class ExchangeNameListener extends Music implements ActionListener{
	
	private Song song;
	
	public ExchangeNameListener(Song song) {
		this.song = song;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(null != song) {
			SongListService songListService = new SongListService();
			if(songListService.nameSingerExchange(song)) {
				MusicListLayout.refreshSongList(song.getPlayListName());
			}
			songListService = null;
		}
	}

}
