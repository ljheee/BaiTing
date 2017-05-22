package com.baiting.http.netsong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.baiting.bean.NetSong;
import com.baiting.font.Fonts;
import com.baiting.layout.NetSongPanel;
import com.baiting.layout.ShowMsgPanel;
import com.baiting.layout.SongListTable;
import com.baiting.util.CommonUtil;
import com.baiting.util.StringUtil;

public class BaiduSongList extends NetSongList {

	private String[] songListUrls = {
			"http://music.baidu.com/top/new",
			"http://music.baidu.com/top/dayhot",
			"http://music.baidu.com/top/oldsong",
			"http://music.baidu.com/top/netsong"};
	private String typeName;
	private JScrollPane tablePanel;
	
	public BaiduSongList() {
		super();
	}
	
	public void setType(int type) {
		if(type>0 && type<=songTypes.length) {
			netSongType = type;
		} else {
			if(netSongType == 0) {
				netSongType = 1;
			}
			type = netSongType;
		}
		typeName = songTypes[type-1];
		url = songListUrls[type-1];
	}
	
	public synchronized List<NetSong> startGradSongList() {
		if(StringUtil.isEmpty(url)) {
			url = songListUrls[0];
		}
		List<NetSong> netSongs = new ArrayList<NetSong>();
		log.info("---正准备抓去歌曲列表----");
		String[] contents = super.getHtmlContents(url);
		if(null != contents && contents.length>0) {
			for (int i = 0; i < contents.length; i++) {
				String line = contents[i].trim().replaceAll("\t|\n|\r", "");
				if(StringUtil.isContains(line, "song-item-hook")) {
					String datas = line.replaceAll("(.*)song-item-hook \\{ 'songItem'\\:(.*)\\} \\}(.*)", "$2\\}").trim();
					if(datas.startsWith("{")) {
						NetSong netSong = parseJsonData(datas);
						if(null != netSong) {
							//log.info(netSong.getSongName()+"---"+netSong.getSinger());
							netSong.setType(netSongType);
							netSongs.add(netSong);
						}
					}
				}
				
			}
		}
		contents = null;
		return (netSongs.size()>0?netSongs:null);

	}
	
	
	@Override
	public synchronized void run() {
		ShowMsgPanel msgPanel = ShowMsgPanel.getInstance();
		msgPanel.setOpaque(false);
		JPanel netSongPanel = NetSongPanel.getInstance().create();
		netSongPanel.removeAll();
		netSongPanel.repaint();
		if(null != netSongPanel) {
			netSongPanel.add(msgPanel,BorderLayout.CENTER);
			netSongPanel.updateUI();
			msgPanel.setMsg(getConfigMap().get("loading.msg").toString()+typeName+".....");
		}
		List<NetSong> netSongList = startGradSongList();
		if(null != netSongList && netSongList.size()>0) {
			String[] titles = getConfigMap().get("net.song.tabel.title").toString().split(",");
			String[][] datas = new String[netSongList.size()][6];
			int count = 0;
			for(NetSong netSong:netSongList) {
				datas[count][0] = "";
				datas[count][1] = netSong.getSongName();
				datas[count][2] = netSong.getSinger();
				datas[count][3] = getConfigMap().get("search.listen.test.label").toString();
				datas[count][4] = getConfigMap().get("add.label").toString();
				datas[count][5] = getConfigMap().get("down.label").toString();
				count++;
			}
			SongListTable table = new SongListTable(datas,titles,netSongList);
			table.getTableHeader().setBackground(CommonUtil.getColor(getConfigMap().get("net.song.list.table.background.color").toString()));
			table.getTableHeader().setFont(Fonts.songTi13());
			table.getTableHeader().setOpaque(true);
			tablePanel = new JScrollPane(table);
			tablePanel.setBorder(null);
			tablePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			tablePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			tablePanel.setPreferredSize(new Dimension(getWidth()/2+60, getHeight()/2+100));
			tablePanel.getViewport().setOpaque(false);
			netSongPanel.removeAll();
			netSongPanel.repaint();
			netSongPanel.add(tablePanel,BorderLayout.CENTER);
			netSongPanel.updateUI();
			table = null;
		} else {
			msgPanel.setMsg(getConfigMap().get("net.loading.fail.msg").toString());
		}
		msgPanel = null;
		netSongPanel = null;
	}
}
