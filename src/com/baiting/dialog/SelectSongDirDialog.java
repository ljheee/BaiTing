package com.baiting.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.baiting.Music;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.SongListService;

public class SelectSongDirDialog extends Music implements ActionListener {

	private String listName;
	private JPanel panel;
	
	public SelectSongDirDialog(JPanel panel,String listName) {
		this.panel = panel;
		this.listName = listName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(panel);
		String path = "";
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		       path = fileChooser.getSelectedFile().getAbsolutePath();
		}
		SongListService songListService = new SongListService();
		if(songListService.readSongDir(listName, path)) {
			MusicListLayout.refreshSongList(listName);
		}
	}

}
