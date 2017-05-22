package com.baiting.layout;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import com.baiting.Music;
import com.baiting.bean.DownedSong;
import com.baiting.bean.Song;
import com.baiting.font.Fonts;
import com.baiting.listener.MusicMouseListener;
import com.baiting.service.DownloadSongService;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.SongListService;
import com.baiting.util.CommonUtil;

public class DownedLayout extends Music {

	private static DownedLayout instance;
	private static MusicTable downedTable;
	private static DefaultTableModel tabelModel;
	private static List<DownedSong> downedSongList;
	private JScrollPane tablePanel;
	private static String[][] datas;
	private String[] titles = getConfigMap().get("downed.table.header").toString().split(",");
	
	private DownedLayout() {
		init();
	}
	
	public static DownedLayout getInstance() {
		if(null == instance) {
			instance = new DownedLayout();
		}
		return instance;
	}
	
	private void init() {
		int size = 0;
		downedSongList = DownloadSongService.getInstance().getDownedSongAll();
		if(null != downedSongList && downedSongList.size()>0) {
			size = downedSongList.size();
			datas = new String[size][8];
			for(int i=0;i<downedSongList.size();i++) {
				datas[i][0] = downedSongList.get(i).getNo()+"";
				datas[i][1] = getConfigMap().get("play.label").toString();
				datas[i][2] = getConfigMap().get("add.label").toString();
				datas[i][3] = downedSongList.get(i).getSongName();
				datas[i][4] = downedSongList.get(i).getSinger();
				
				int pos = downedSongList.get(i).getFileName().lastIndexOf(".");
				String ext = downedSongList.get(i).getFileName().substring(pos+1);
				
				datas[i][5] = ext;
				datas[i][6] = downedSongList.get(i).getFileSize()+"M";
				datas[i][7] = downedSongList.get(i).getCreateTime();
			}
		} else {
			datas = new String[0][8];
		}
		tabelModel = new DefaultTableModel(datas,titles);
		downedTable = new MusicTable(tabelModel);
		downedTable.setOpaque(false);
		
		downedTable.getTableHeader().setPreferredSize(new Dimension(0, 25));
		downedTable.getTableHeader().setFont(Fonts.songTi13());
		downedTable.getTableHeader().setOpaque(true);
		
		downedTable.setShowVerticalLines(false);
		downedTable.setAutoscrolls(true);
		downedTable.setShowHorizontalLines(false);
		downedTable.getTableHeader().setBackground(CommonUtil.getColor(getConfigMap().get("down.table.header.background.color").toString()));
		SongDownedTableCellRenderer r = new SongDownedTableCellRenderer();
		downedTable.setDefaultRenderer(Object.class, r);
		downedTable.setRowHeight(22);
		downedTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		downedTable.getColumnModel().getColumn(1).setPreferredWidth(35);
		downedTable.getColumnModel().getColumn(2).setPreferredWidth(35);
		downedTable.getColumnModel().getColumn(3).setPreferredWidth(130);
		downedTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		downedTable.getColumnModel().getColumn(5).setPreferredWidth(30);
		downedTable.getColumnModel().getColumn(6).setPreferredWidth(40);
		downedTable.getColumnModel().getColumn(7).setPreferredWidth(140);
		downedTable.addMouseListener(new MusicMouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					int row = downedTable.rowAtPoint(e.getPoint());
					int col = downedTable.columnAtPoint(e.getPoint());
					if(col == 2) {
						if(row>=downedSongList.size()) {
							downedSongList = DownloadSongService.getInstance().getDownedSongAll();
						}
						if(null != downedSongList && downedSongList.size()>0) {
							DownedSong dowedSong = downedSongList.get(row);
							Song songTmp = new Song();
							songTmp.setName(dowedSong.getSongName());
							songTmp.setSinger(dowedSong.getSinger());
							songTmp.setPlayListName(getPlayListName());
							songTmp.setUrl("");
							songTmp.setPath(dowedSong.getPath());
							SongListService listService = new SongListService();
							int no = 0;
							int noTmp = listService.existSongList(songTmp);
							if(noTmp == -1) {
								no = listService.addSong(songTmp);
								songTmp.setNo(no);
								MusicListLayout.refreshSongList(getPlayListName());
							}
						}
					}
					if(col == 1) {
						if(row>=downedSongList.size()) {
							downedSongList = DownloadSongService.getInstance().getDownedSongAll();
						}
						if(null != downedSongList && downedSongList.size()>0) {
							DownedSong dowedSong = downedSongList.get(row);
							Song songTmp = new Song();
							songTmp.setName(dowedSong.getSongName());
							songTmp.setSinger(dowedSong.getSinger());
							songTmp.setPlayListName(getPlayListName());
							songTmp.setUrl("");
							songTmp.setPath(dowedSong.getPath());
							SongListService listService = new SongListService();
							int no = 0;
							int noTmp = listService.existSongList(songTmp);
							if(noTmp == -1) {
								no = listService.addSong(songTmp);
								songTmp.setNo(no);
								MusicListLayout.refreshSongList(getPlayListName());
							} else {
								no = noTmp;
								songTmp = listService.getSong(noTmp);
							}
							
							MusicPlayerService.getInstance().stop();
							ImageIcon playIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("play.icon").toString());
							MusicPlayControllerLayout.getInstance().getPlayLabel().setIcon(playIcon);
							MusicPlayControllerLayout.setClickPlay(1);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							ImageIcon playIcon2 = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("pause.icon").toString());
							MusicPlayControllerLayout.getInstance().getPlayLabel().setIcon(playIcon2);
							MusicPlayControllerLayout.setClickPlay(0);
							
							MusicListLayout.getInstance().getSongList().setSelectedIndex(no-1);
							MusicListLayout.getInstance().getSongList().setSelectionBackground(getSongListSelectionBackground());
							MusicListLayout.getInstance().getSongList().setSelectionForeground(getSongListSelectionForeground());
							MusicPlayerService.getInstance().play(songTmp);
						}
					}
				}
			}
		});
		tablePanel = new JScrollPane(downedTable);
		tablePanel.setOpaque(false);
		
		tablePanel.setBorder(null);
		tablePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tablePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePanel.setPreferredSize(new Dimension(getWidth()/2+60, getHeight()/2+100));
		tablePanel.getViewport().setOpaque(false);
	}
	
	public JScrollPane create() {
		return tablePanel;
	}
	
	
	/**
	 * 添加一行数据
	 * @param downingNetSong
	 * @return
	 */
	public static boolean addRows(DownedSong downedSong,int row) {
		if(null == downedSong || row < 1) {
			return false;
		}
		String[] data = new String[8];
		data[0] = row+"";
		data[1] = getConfigMap().get("play.label").toString();
		data[2] = getConfigMap().get("add.label").toString();
		data[3] = downedSong.getSongName();
		data[4] = downedSong.getSinger();
		
		int pos = downedSong.getFileName().lastIndexOf(".");
		String ext = downedSong.getFileName().substring(pos+1);
		
		data[5] = ext;
		data[6] = downedSong.getFileSize()+"M";
		data[7] = downedSong.getCreateTime();
		if(null != tabelModel) {
		  tabelModel.addRow(data);
		  return true;
		} else {
			return false;
		}
	}
}
