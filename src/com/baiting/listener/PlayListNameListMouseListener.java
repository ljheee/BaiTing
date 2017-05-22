package com.baiting.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.baiting.dialog.DeleteListNameDialog;
import com.baiting.dialog.RenameListNameDialog;
import com.baiting.dialog.SelectSongDirDialog;
import com.baiting.dialog.SelectSongFileDialog;
import com.baiting.font.Fonts;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.SongListService;
import com.baiting.util.StringUtil;

public class PlayListNameListMouseListener extends MusicMouseListener {

	private JList playListNameList;
	private String listName;
	private JPanel listNamePanel;
	
	public PlayListNameListMouseListener(JList playListNameList,String listName,JPanel listNamePanel) {
		this.playListNameList = playListNameList;
		this.listName = listName;
		this.listNamePanel = listNamePanel;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		playListNameList.setSelectionBackground(getPlayListSelectionBackground());
		playListNameList.setSelectionForeground(getPlayListSelectionForeground());
		if(e.getButton() == MouseEvent.BUTTON3) {  //鼠标右键
			log.info("鼠标右键-----");
			Object obj = playListNameList.getSelectedValue();
			if(obj != null) {
			   listName = obj.toString();
			   if(!StringUtil.isEmpty(listName)) {
				   setPlayListName(listName);
				   //重命名播放列表名称
				   JPopupMenu popMenu = new JPopupMenu();
				   JMenuItem rename = new JMenuItem();
				   rename.setText(getConfigMap().get("pop.menu.play.list.rename.label").toString());
				   rename.setHorizontalAlignment(SwingConstants.CENTER);
				   rename.setFont(Fonts.songTi11());
				   rename.addActionListener(new RenameListNameDialog(listNamePanel,listName));
				   JMenuItem del = new JMenuItem();
				   
				   //删除播放列表
				   del.setText(getConfigMap().get("pop.menu.play.list.remove.label").toString());
				   del.setHorizontalAlignment(SwingConstants.CENTER);
				   del.setFont(Fonts.songTi11());
				   del.addActionListener(new DeleteListNameDialog(listNamePanel,listName));
				   popMenu.add(rename);
				   popMenu.add(del);
				   
				   //清除播放列表里面的歌曲
				   JMenuItem clear = new JMenuItem();
				   clear.setText(getConfigMap().get("pop.menu.play.list.clear.label").toString());
				   clear.setHorizontalAlignment(SwingConstants.CENTER);
				   clear.setFont(Fonts.songTi11());
				   clear.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							SongListService songListService = new SongListService();
							if(songListService.clearSongList(listName)) {
								MusicListLayout.refreshSongList(listName);
							}
							songListService = null;
						}
				   });
				   popMenu.add(clear);
				   popMenu.addSeparator();
				   
				   //添加本地歌曲文件
				   JMenuItem locationSongFile = new JMenuItem();
				   locationSongFile.setText(getConfigMap().get("pop.menu.local.add.song.file.label").toString());
				   locationSongFile.setFont(Fonts.songTi11());
				   locationSongFile.setHorizontalAlignment(SwingConstants.CENTER);
				   locationSongFile.addActionListener(new SelectSongFileDialog(listNamePanel, listName));
				   
				 //添加本地歌曲目录
				   JMenuItem locationSongDir = new JMenuItem();
				   locationSongDir.setText(getConfigMap().get("pop.menu.local.add.song.dir.label").toString());
				   locationSongDir.setFont(Fonts.songTi11());
				   locationSongDir.setHorizontalAlignment(SwingConstants.CENTER);
				   locationSongDir.addActionListener(new SelectSongDirDialog(listNamePanel, listName));
				
				   popMenu.add(locationSongFile);
				   popMenu.add(locationSongDir);
				   popMenu.show(e.getComponent(), e.getX(), e.getY());
			   }
			}
		} else if(e.getButton() == MouseEvent.BUTTON1) {
			Object obj = playListNameList.getSelectedValue();
			if(obj != null) {
			   String listName = obj.toString();
			   if(!StringUtil.isEmpty(listName)) {
				   playListName = listName;
				   MusicListLayout.refreshSongList(listName);
			   }
			}
		}
	}
	
}
