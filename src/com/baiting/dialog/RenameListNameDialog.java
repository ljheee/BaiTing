package com.baiting.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.baiting.Music;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.PlayListService;
import com.baiting.util.StringUtil;

public class RenameListNameDialog extends Music implements ActionListener{
	
	private String listName;
	private JPanel panel;
	
	public RenameListNameDialog(JPanel panel,String listName) {
		this.panel = panel;
		this.listName = listName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(getConfigMap().get("play.list.default.name").toString().equals(listName)) {
			JOptionPane.showMessageDialog(panel, getConfigMap().get("pop.menu.play.list.not.rename.dialog.label").toString());
		} else {
			String rename = JOptionPane.showInputDialog(getConfigMap().get("pop.menu.play.list.rename.dialog.alert.label").toString());
			if(!StringUtil.isEmpty(rename)) {
				PlayListService playListService = new PlayListService();
				if(playListService.renamePlayList(listName,rename )){
					MusicListLayout.renameRefreshPlayList(listName,rename);
				}
			}
		}
	}

}
