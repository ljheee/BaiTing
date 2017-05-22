package com.baiting.service;

import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.baiting.IMusic;
import com.baiting.Music;
import com.baiting.bean.Song;
import com.baiting.layout.MusicListLayout;
import com.baiting.layout.MusicPlayControllerLayout;

public class PlayStatusListener extends TimerTask {

	@Override
	public void run() {
		MusicPlayerService player = MusicPlayerService.getInstance();
		if(player.getStatus() == IMusic.PLAY_FINISHING) {
			Song song = player.getSong();
			if(song != null) {
				SongListService listService = new SongListService();
				Song songTmp = listService.getSong(song.getPlayListName(),song.getNo()+1 );
				listService = null;
				if(songTmp != null) {
					MusicPlayerService.getInstance().close();
						
					ImageIcon playIcon2 = new ImageIcon(MusicService.getIconPath()+MusicService.getSeparator()+Music.getConfigMap().get("play.icon").toString());
					MusicPlayControllerLayout.getInstance().getPlayLabel().setIcon(playIcon2);
					playIcon2 = null;
					MusicPlayControllerLayout.setClickPlay(0);
						
					MusicListLayout.getInstance().getSongList().setSelectedIndex(song.getNo());
					MusicListLayout.getInstance().getSongList().setSelectionBackground(MusicService.getSongListSelectionBackground());
					MusicListLayout.getInstance().getSongList().setSelectionForeground(MusicService.getSongListSelectionForeground());
					player.setStatus(IMusic.PLAY_STATUS);
					MusicPlayerService.getInstance().play(songTmp);
				}
				songTmp = null;
			}
		}
	}

}
