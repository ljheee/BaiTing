package com.baiting.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.baiting.bean.DowningNetSong;
import com.baiting.bean.Song;
import com.baiting.dialog.RenameSongDialog;
import com.baiting.font.Fonts;
import com.baiting.layout.DowningLayout;
import com.baiting.layout.MusicListLayout;
import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.service.DownloadSongService;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.SongListService;
import com.baiting.util.StringUtil;
import com.baiting.util.UrlFileUtil;

public class SongListMouseListener extends MusicMouseListener {

	private JList playListNameList;
	public SongListMouseListener(JList playListNameList) {
		this.playListNameList = playListNameList;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			String listName = (String)playListNameList.getSelectedValue();
			JList songInfo = (JList)e.getSource();
			String name = (String)songInfo.getSelectedValue();
			songInfo = null;
			if(!StringUtil.isEmpty(name)) {
				//播放按钮改为播放图标
				MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
				String[] cols = name.split("\\.");
				SongListService songListService = new SongListService();
				Song songTmp = songListService.getSong(listName, Integer.parseInt(cols[0].trim()));
				songListService = null;
				if(songTmp != null) {
					MusicPlayerService.getInstance().stop();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					MusicPlayControllerLayout.getInstance().setPlayButtonToPauseIcon();
					MusicPlayerService.getInstance().play(songTmp);
					songTmp = null;
				}
			}
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			final JList songInfo = (JList)e.getSource();
			String name = (String)songInfo.getSelectedValue();
			if(!StringUtil.isEmpty(name)) {
				final String[] cols = name.split("\\.");
				JPopupMenu popMenu = new JPopupMenu();
				
				JMenuItem playItem = new JMenuItem();
				playItem.setText(getConfigMap().get("play.label").toString());
				playItem.setFont(Fonts.songTi11());
				playItem.setHorizontalAlignment(SwingConstants.CENTER);
				playItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(cols != null && cols.length>0) {
							MusicPlayerService.getInstance().stop();
							MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
							
							SongListService listService = new SongListService();
							Song songTmp = listService.getSong(playListName, Integer.parseInt(cols[0].trim()));
							if(songTmp != null) {
								listService = null;
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								MusicPlayControllerLayout.getInstance().setPlayButtonToPauseIcon();
								MusicPlayerService.getInstance().play(songTmp);
								songTmp = null;
							}
						}
					}
				});
				
				JMenuItem downItem = new JMenuItem();
				downItem.setText(getConfigMap().get("down.label").toString());
				downItem.setFont(Fonts.songTi11());
				downItem.setHorizontalAlignment(SwingConstants.CENTER);
				downItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(cols != null && cols.length>0) {
							SongListService listService = new SongListService();
							Song songTmp = listService.getSong(playListName, Integer.parseInt(cols[0].trim()));
							listService = null;
							if(songTmp != null) {
								if(StringUtil.isEmpty(songTmp.getPath()) && !StringUtil.isEmpty(songTmp.getUrl())) {
									//启动下载进程
									//showMsg("启动下载线程....");
									if(null == downingNetSongList) {
										downingNetSongList = new ArrayList<DowningNetSong>();
									}
									int no = downingNetSongList.size();
									DowningNetSong downingNetSong = new DowningNetSong();
									downingNetSong.setFileName(songTmp.getName()+"-"+songTmp.getSinger());
									downingNetSong.setSinger(songTmp.getSinger());
									downingNetSong.setSongName(songTmp.getName());
									downingNetSong.setNo(no+1);
									downingNetSong.setProgress("--");
									downingNetSong.setRemainTime("--");
									downingNetSong.setSpeed("--");
									downingNetSong.setStatus(getConfigMap().get("song.down.wait.label").toString());
									downingNetSong.setUrl(songTmp.getUrl());
									downingNetSong.setPlayListName(songTmp.getPlayListName());
									downingNetSong.setListNo(songTmp.getNo());
									long fileSize = UrlFileUtil.getUrlFileSize(songTmp.getUrl());
									DecimalFormat formatter = new DecimalFormat("0.00");
									downingNetSong.setFileSize(formatter.format((double)fileSize/1024/1024)+"M");
									downingNetSongList.add(downingNetSong);
									DowningLayout.addRows(downingNetSong);
									downingNetSong = null;
									formatter = null;
									DownloadSongService.getInstance().startDownload();
								} else if(!StringUtil.isEmpty(songTmp.getPath())) {
									MusicListLayout.showErrorMsg(getConfigMap().get("song.down.exits.label").toString());
								} else if(StringUtil.isEmpty(songTmp.getUrl())) {
									MusicListLayout.showErrorMsg(getConfigMap().get("song.down.notfind.resource.label").toString());
								}
								songTmp = null;
							}
					    }
					}
				});
				
				JMenuItem delItem = new JMenuItem();
				delItem.setText(getConfigMap().get("remove.label").toString());
				delItem.setFont(Fonts.songTi11());
				delItem.setHorizontalAlignment(SwingConstants.CENTER);
				delItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Object[] names = songInfo.getSelectedValues();
						if(null != names && names.length>0) {
							MusicPlayerService player = MusicPlayerService.getInstance();
							Song song = player.getSong();
							SongListService listService = new SongListService();
							Song songTmp = null;
							List<Song> songList = new ArrayList<Song>();
							for (int i = 0; i < names.length; i++) {
								String[] selectCols = null;
								try {
									selectCols = names[i].toString().split("\\.");
								} catch (Exception ex) {
									ex.printStackTrace();
									selectCols = null;
								}
								if(null != selectCols && selectCols.length>0) {
									songTmp = listService.getSong(playListName, Integer.parseInt(selectCols[0].trim()));
									if(songTmp != null) {
										if(song != null && (song.getNo()==songTmp.getNo() && song.getPlayListName().equals(songTmp.getPlayListName()))) {
											ImageIcon playIcon2 = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("play.icon").toString());
											MusicPlayControllerLayout.getInstance().getPlayLabel().setIcon(playIcon2);
											playIcon2 = null;
											MusicPlayControllerLayout.setClickPlay(1);
											MusicPlayerService.getInstance().stop();
											try {
												Thread.sleep(1000);
											} catch (InterruptedException e1) {
												e1.printStackTrace();
											}
											MusicPlayControllerLayout playLayout = MusicPlayControllerLayout.getInstance();
											playLayout.getSongInfoLabel().setText("");
											playLayout.getTotalTimeLabel().setText("00:00");
											playLayout = null;
										}
										songList.add(songTmp);
									}
								}
							}
							if(listService.delSongList(songList)) {
								MusicListLayout.refreshSongList(playListName);
							}
							player = null;
							song = null;
							listService = null;
							songTmp = null;
							songList = null;
						}
						names = null;
					}
				});
				
				JMenuItem renameItem = new JMenuItem();
				renameItem.setText(getConfigMap().get("rename.label").toString());
				renameItem.setFont(Fonts.songTi11());
				renameItem.setHorizontalAlignment(SwingConstants.CENTER);
				
				SongListService listService = new SongListService();
				Song songTmp = listService.getSong(playListName, Integer.parseInt(cols[0].trim()));
				renameItem.addActionListener(new RenameSongDialog(songTmp));
			
				
				JMenuItem exchangeItem = new JMenuItem();
				exchangeItem.setText(getConfigMap().get("exchange.name.singer.label").toString());
				exchangeItem.setFont(Fonts.songTi11());
				exchangeItem.setHorizontalAlignment(SwingConstants.CENTER);
				exchangeItem.addActionListener(new ExchangeNameListener(songTmp));
				songTmp = null;
				
				popMenu.add(playItem);
				popMenu.add(downItem);
				popMenu.addSeparator();
				popMenu.add(delItem);
				popMenu.add(renameItem);
				popMenu.addSeparator();
				popMenu.add(exchangeItem);
				popMenu.show(e.getComponent(),e.getX(),e.getY());
				//playItem = null;
				//downItem = null;
				//delItem = null;
			}//end if
		}//end else if
	}//end mouseClicked()
}
