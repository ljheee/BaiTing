package com.baiting.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.baiting.Music;
import com.baiting.bean.PlayList;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.PlayListService;
import com.baiting.util.StringUtil;

public class CreateListNameDialog extends Music implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String listName = JOptionPane.showInputDialog(getConfigMap().get("play.list.create.name.dialog.label").toString());
		if (!StringUtil.isEmpty(listName)) {
			PlayList playList = new PlayList();
			String listFileName = String.valueOf(System.currentTimeMillis());
			playList.setFileName(listFileName);
			playList.setPlayListName(listName);
			
			PlayListService playListService = new PlayListService();
			playListService.addPlayList(playList);
			MusicListLayout.addRefreshPlayList(listName);
		}

	}

}
