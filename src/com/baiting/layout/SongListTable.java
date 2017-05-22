package com.baiting.layout;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ListSelectionModel;

import com.baiting.Music;
import com.baiting.bean.NetSong;
import com.baiting.listener.MusicMouseListener;
import com.baiting.service.DownloadSongService;
import com.baiting.service.NetSongService;

public class SongListTable extends MusicTable{

	private static final long serialVersionUID = -8298003521968394492L;
	//private Object[][] datas;
	public static List<NetSong> netSongList;
	public SongListTable(Object[][] obj,Object[] obj2,List<NetSong> netSongList) {
		super(obj, obj2);
		SongListTable.netSongList = netSongList;
		//datas = obj;
		SongListTableCellRenderer r = new SongListTableCellRenderer();
		this.setDefaultRenderer(Object.class, r);
		this.setShowVerticalLines(false);
		this.setAutoscrolls(true);
		this.setShowHorizontalLines(false);
		//this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setRowHeight(22);
		this.getColumnModel().getColumn(0).setPreferredWidth(45);
		this.getColumnModel().getColumn(1).setPreferredWidth(200);
		this.getColumnModel().getColumn(2).setPreferredWidth(110);
		this.getColumnModel().getColumn(3).setPreferredWidth(41);
		this.getColumnModel().getColumn(4).setPreferredWidth(41);
		this.getColumnModel().getColumn(5).setPreferredWidth(41);
		this.setOpaque(true);
		this.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					e.getPoint();
					int row = rowAtPoint(e.getPoint());
					int col = columnAtPoint(e.getPoint());
					
					if(col == 3) {
						NetSong netSong = SongListTable.getNetSongList().get(row);
						NetSongService netSongService = new NetSongService();
						netSongService.playNetSong(netSong,"");
						netSongService = null;
						netSong = null;
					} else if(col == 4) {
						NetSong netSong = SongListTable.getNetSongList().get(row);
						NetSongService netSongService = new NetSongService();
						netSongService.addNetSongToPlayList(netSong,"");
						netSongService = null;
						netSong = null;
					} else if(col == 5) {
						NetSong netSong = SongListTable.getNetSongList().get(row);
						boolean flag = DownloadSongService.getInstance().existSongByInfo(netSong.getSongName(), netSong.getSinger());
						if(!flag) {
							NetSongService netSongService = new NetSongService();
							netSongService.downloadNetSong(netSong,"");
							netSongService = null;
						} else {
							MusicListLayout.showMsg(Music.getConfigMap().get("search.msg.alert").toString());
						}

						netSong = null;
					}
				}
			}
		});
	}
	public static List<NetSong> getNetSongList() {
		return netSongList;
	}
	
}
