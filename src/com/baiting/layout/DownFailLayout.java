package com.baiting.layout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.baiting.Music;
import com.baiting.bean.DownFailSong;
import com.baiting.bean.NetSong;
import com.baiting.font.Fonts;
import com.baiting.listener.MusicMouseListener;
import com.baiting.service.DownloadSongService;
import com.baiting.service.NetSongService;
import com.baiting.util.CommonUtil;

public class DownFailLayout extends Music {

	private static DownFailLayout instance;
	private static MusicTable downFailTable;
	private static DefaultTableModel tabelModel;
	private static List<DownFailSong> downFailSongList;
	private JScrollPane tablePanel;
	private String[][] datas;
	private String[] titles = getConfigMap().get("down.fail.table.header").toString().split(",");
	private int selectRow = -1;
	
	private DownFailLayout() {
		init();
	}
	
	public static DownFailLayout getInstance() {
		if(null == instance) {
			instance = new DownFailLayout();
		}
		return instance;
	}
	
	private void init() {
		int size = 0;
		downFailSongList = DownloadSongService.getInstance().getDownFailSongAll();
		if(null != downFailSongList && downFailSongList.size()>0) {
			size = downFailSongList.size();
			datas = new String[size][6];
			for(int i=0;i<downFailSongList.size();i++) {
				datas[i][0] = downFailSongList.get(i).getNo()+"";
				datas[i][1] = getConfigMap().get("down.fail.label").toString();
				datas[i][2] = downFailSongList.get(i).getSongName();
				datas[i][3] = downFailSongList.get(i).getSinger();
				datas[i][4] = downFailSongList.get(i).getFormat();
				datas[i][5] = downFailSongList.get(i).getFailTime();
			}
		} else {
			datas = new String[0][6];
		}
		tabelModel = new DefaultTableModel(datas,titles);
		downFailTable = new MusicTable(tabelModel);
		downFailTable.setOpaque(false);
		
		downFailTable.getTableHeader().setPreferredSize(new Dimension(0, 25));
		downFailTable.getTableHeader().setFont(Fonts.songTi13());
		downFailTable.getTableHeader().setOpaque(true);
		
		downFailTable.setShowVerticalLines(false);
		downFailTable.setAutoscrolls(true);
		downFailTable.setShowHorizontalLines(false);
		downFailTable.getTableHeader().setBackground(CommonUtil.getColor(getConfigMap().get("down.table.header.background.color").toString()));
		MusicTableCellRenderer r = new MusicTableCellRenderer();
		downFailTable.setDefaultRenderer(Object.class, r);
		downFailTable.setRowHeight(22);
		downFailTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		downFailTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		downFailTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		downFailTable.getColumnModel().getColumn(3).setPreferredWidth(110);
		downFailTable.getColumnModel().getColumn(4).setPreferredWidth(40);
		downFailTable.getColumnModel().getColumn(5).setPreferredWidth(150);
		downFailTable.addMouseListener(new MusicMouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					selectRow = downFailTable.rowAtPoint(e.getPoint());
					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem reDownItem = new JMenuItem(getConfigMap().get("redown.label").toString());
					reDownItem.setFont(Fonts.songTi11());
					reDownItem.setHorizontalAlignment(SwingConstants.CENTER);
					reDownItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							NetSong netSong =  new NetSong();
							netSong.setSongName((String)datas[selectRow][2]);
							netSong.setSinger((String)datas[selectRow][3]);
							boolean flag = DownloadSongService.getInstance().existSongByInfo(netSong.getSongName(), netSong.getSinger());
							if(!flag) {
								NetSongService netSongService = new NetSongService();
								netSongService.downloadNetSong(netSong,"");
							} else {
								MusicListLayout.showMsg(getConfigMap().get("down.exits.label").toString());
							}
						}
					});
					
					
					JMenuItem delItem = new JMenuItem(getConfigMap().get("remove.label").toString());
					delItem.setFont(Fonts.songTi11());
					delItem.setHorizontalAlignment(SwingConstants.CENTER);
					delItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DownloadSongService.getInstance().delDownFailSong(selectRow+1);
							delRows(selectRow);
						}
					});
					popupMenu.add(reDownItem);
					popupMenu.add(delItem);
					popupMenu.show(e.getComponent(),e.getX(), e.getY());
				}
			}
		});
		tablePanel = new JScrollPane(downFailTable);
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
	public static boolean addRows(DownFailSong downFailSong,int row) {
		if(null == downFailSong || row < 1 ) {
			return false;
		}
		String[] data = new String[6];
		data[0] = row+"";
		data[1] = downFailSong.getSongName();
		data[2] = downFailSong.getSinger();
		
		data[3] = downFailSong.getFormat();
		data[4] = downFailSong.getFailTime();
		if(null != tabelModel) {
		  tabelModel.addRow(data);
		  return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 删除一行数据
	 * @param downingNetSong
	 * @return
	 */
	public static boolean delRows(int row) {
		if(row < 0 ) {
			return false;
		}
		tabelModel.removeRow(row);
		return true;
		
	}
}
