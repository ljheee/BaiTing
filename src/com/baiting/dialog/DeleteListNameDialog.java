package com.baiting.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.baiting.Music;
import com.baiting.bean.PlayList;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.PlayListService;

public class DeleteListNameDialog extends Music implements ActionListener {
	
	private String listName;
	private JPanel panel;
	
	public DeleteListNameDialog(JPanel panel,String listName) {
		this.panel = panel;
		this.listName = listName;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(getConfigMap().get("play.list.default.name").toString().equals(listName)) {
			JOptionPane.showMessageDialog(panel,getConfigMap().get("pop.menu.play.list.not.remove.dialog.label").toString());
		} else {
			int selected = JOptionPane.showConfirmDialog(panel, getConfigMap().get("pop.menu.play.list.remove.dialog.alert.label").toString());
			if(selected == 0) {  
				PlayListService playListService = new PlayListService();
				  if(playListService.deletePlayList(listName, PlayList.PLAYLIST_NAME)){
					  MusicListLayout.delRefreshPlayList(listName);
				  }
			}
		}
	}

}
