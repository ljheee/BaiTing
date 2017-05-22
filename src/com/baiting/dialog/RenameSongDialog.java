package com.baiting.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.baiting.Music;
import com.baiting.bean.Song;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.SongListService;
import com.baiting.util.StringUtil;

public class RenameSongDialog extends Music implements ActionListener{

	private Song song;
	
	public RenameSongDialog(Song song) {
		this.song = song;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String rename = JOptionPane.showInputDialog(getConfigMap().get("pop.menu.rename.song.dialog.alert.label").toString(),song.getName()+"-"+song.getSinger());
		if(!StringUtil.isEmpty(rename)) {
			SongListService songListService = new SongListService();
			if(songListService.renameSongName(song,rename )){
				MusicListLayout.refreshSongList(song.getPlayListName());
			}
		}
	}

}
