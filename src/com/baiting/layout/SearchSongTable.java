package com.baiting.layout;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ListSelectionModel;

import com.baiting.Music;
import com.baiting.bean.NetSong;
import com.baiting.font.Fonts;
import com.baiting.listener.MusicMouseListener;
import com.baiting.service.DownloadSongService;
import com.baiting.service.NetSongService;
import com.baiting.util.CommonUtil;

/**
 * 
 * @author lmq
 *
 */
public class SearchSongTable extends MusicTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2435672481240666434L;

	public static List<NetSong> searchSongList;
	
	//private Object[][] datas;
	public SearchSongTable(Object[][] obj,Object[] obj2,List<NetSong> searchSongList) {
		super(obj, obj2);
		SearchSongTable.searchSongList = searchSongList;
		SearchSongTableCellRenderer r = new SearchSongTableCellRenderer();
		this.setDefaultRenderer(Object.class, r);
		r = null;
		this.setShowVerticalLines(false);
		this.setAutoscrolls(true);
		this.setShowHorizontalLines(false);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setRowHeight(22);
		
		this.getTableHeader().setPreferredSize(new Dimension(0, 25));
		this.getTableHeader().setFont(Fonts.songTi13());
		this.getTableHeader().setOpaque(true);
		this.setOpaque(true);
		this.getTableHeader().setBackground(CommonUtil.getColor(Music.getConfigMap().get("search.song.table.header.background.color").toString()));
		this.getColumnModel().getColumn(0).setPreferredWidth(26);
		//this.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		this.getColumnModel().getColumn(1).setPreferredWidth(120);
		this.getColumnModel().getColumn(2).setPreferredWidth(92);
		this.getColumnModel().getColumn(3).setPreferredWidth(93);
		this.getColumnModel().getColumn(4).setPreferredWidth(31);
		this.getColumnModel().getColumn(5).setPreferredWidth(31);
		this.setOpaque(true);
		this.addMouseListener(new MusicMouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					int row = rowAtPoint(e.getPoint());
					int col = columnAtPoint(e.getPoint());
					
					if(col == 3) {
						if(SearchSongTable.getSearchSongList() != null && SearchSongTable.searchSongList.size()>0) {
							NetSong netSong = SearchSongTable.getSearchSongList().get(row);
							netSong.setSongName(netSong.getSongName());
							netSong.setSinger(netSong.getSinger());
							NetSongService netSongService = new NetSongService();
							netSongService.playNetSong(netSong,null);
							netSongService = null;
							netSong = null;
						}
					} else if(col == 4) {
						if(SearchSongTable.getSearchSongList() != null && SearchSongTable.searchSongList.size()>0) {
							NetSong netSong = SearchSongTable.getSearchSongList().get(row);
							NetSongService netSongService = new NetSongService();
							netSongService.addNetSongToPlayList(netSong,null);
							netSongService = null;
							netSong = null;
						}
					} else if(col == 5) {
						
						if(SearchSongTable.getSearchSongList() != null && SearchSongTable.searchSongList.size()>0) {
							NetSong netSong = SearchSongTable.getSearchSongList().get(row);
							boolean flag = DownloadSongService.getInstance().existSongByInfo(netSong.getSongName(), netSong.getSinger());
							if(!flag) {
								NetSongService netSongService = new NetSongService();
								netSongService.downloadNetSong(netSong,null);
								netSongService = null;
							} else {
								MusicListLayout.showMsg(Music.getConfigMap().get("search.msg.alert").toString());
							}
							netSong = null;
						}
						
					}
				}
			}
		});
	}
	public static List<NetSong> getSearchSongList() {
		return searchSongList;
	}
	public static void setSearchSongList(List<NetSong> searchSongList) {
		SearchSongTable.searchSongList = searchSongList;
	}
}
