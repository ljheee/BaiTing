package com.baiting.http.netsong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

import com.baiting.bean.NetSong;
import com.baiting.layout.NetSongPanel;
import com.baiting.layout.SearchSongTable;
import com.baiting.layout.ShowMsgPanel;
import com.baiting.util.CommonUtil;
import com.baiting.util.StringUtil;

public class BaiduSearchSongList extends SearchNetSongHttp {
	
	private static final String startBody = "<html><body><a href='#'>";
	private static final String endBody = "</a></body></html>";
	
	public BaiduSearchSongList() {
		super();
		this.url = "http://music.baidu.com/";
	}
	
	public BaiduSearchSongList(NetSong netSong) {
		super(netSong);
		this.url = "http://music.baidu.com/";
	}
	
	public synchronized List<NetSong> startGradSongList() {
		if(StringUtil.isEmpty(netSong.getSongName()) && 
				StringUtil.isEmpty(netSong.getSinger())) {
			return null;
		}
		List<NetSong> netSongList = new ArrayList<NetSong>();
		String result = "";
		if(!StringUtil.isEmpty(netSong.getSongName())) {
			result = netSong.getSongName();
		}
		if(!StringUtil.isEmpty(netSong.getSinger())) {
			if(StringUtil.isEmpty(result)) {
				result = netSong.getSinger();
			} else {
				result += "+"+netSong.getSinger();
			}
		}
		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String parseUrl = url + "search?key="+result;
		String[] contents = this.getHtmlContents(parseUrl);
		int count = 0;
		try {
			if(null != contents && contents.length>0) {
				contents = getHtmlContents(parseUrl);
				for (int i = 0; i < contents.length; i++) {
					String line = contents[i].replaceAll("\r|\n|\t", "").trim();
					if(StringUtil.isContains(line, "<li data-songitem = '\\{&quot;songItem&quot;")) {
						String datas = line.replaceAll("(.*)'\\{&quot;songItem&quot;\\:(.*)&quot;\\}\\}'(.*)", "$2").trim();
						datas = datas.replaceAll("&quot;", "'")+"'}";
						NetSong netSongTmp = parseJsonData(datas);
						if(null != netSongTmp) {
							netSongTmp.setType(NET_SONG_LIST_SEARCH);
							if(!StringUtil.isEmpty(netSong.getSongName()) && 
									!StringUtil.isEmpty(netSong.getSinger())) {
								if(netSongTmp.getSongName().equals(netSong.getSongName()) &&
										netSongTmp.getSinger().equals(netSong.getSinger())) {
									netSongList.add(netSongTmp);
								}
							} else if(!StringUtil.isEmpty(netSong.getSongName()) && 
									StringUtil.isEmpty(netSong.getSinger())) {
								if(StringUtil.isContains(netSongTmp.getSongName(), netSong.getSongName()) ||
										StringUtil.isContains(netSongTmp.getSinger(),netSong.getSongName())) {
									netSongList.add(netSongTmp);
								}
							}
							if(count>50) {
								break;
							}
						}
						count++;
					}
				}
			}
		} catch (Exception e) {
			log.info("搜索歌曲---HttpRequest---异常");
			e.printStackTrace();
		}  finally {
			contents = null;
		}
		return (netSongList.size()>0?netSongList:null);
	}
	
	@Override
	public void run() {
		ShowMsgPanel msgPanel = ShowMsgPanel.getInstance();
		msgPanel.setOpaque(false);
		NetSongPanel.getInstance().create().removeAll();
		NetSongPanel.getInstance().create().repaint();
		NetSongPanel.getInstance().create().add(msgPanel,BorderLayout.CENTER);
		NetSongPanel.getInstance().create().updateUI();
		msgPanel.setMsg(getConfigMap().get("searching.song.msg").toString());
		log.info("正在搜索歌曲信息...");
		List<NetSong> lists = startGradSongList();
		if(null != lists && lists.size()>0) {
			String[] titles = getConfigMap().get("search.song.table.header").toString().split(",");
			String[][] datas = new String[lists.size()][6];
			int count = 0;
			for(NetSong netSong : lists) {
				datas[count][0] = (count+1)+"";
				datas[count][1] = netSong.getSongName();
				datas[count][2] = netSong.getSinger();
				datas[count][3] = startBody+getConfigMap().get("search.listen.test.label").toString()+endBody;
				datas[count][4] = startBody+getConfigMap().get("add.label").toString()+endBody;
				datas[count][5] = startBody+getConfigMap().get("down.label").toString()+endBody;
				count++;
			}
			//log.info(lists.size()+"");
			SearchSongTable table = new SearchSongTable(datas,titles,lists);
			table.getTableHeader().setBackground(CommonUtil.getColor("#f8eae7"));
			JScrollPane tablePanel = new JScrollPane(table);
			tablePanel.setOpaque(false);
			tablePanel.getViewport().setOpaque(false);
			
			tablePanel.setBorder(null);
			tablePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			tablePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			tablePanel.setPreferredSize(new Dimension(getWidth()/2+60, getHeight()/2+100));
            NetSongPanel.getInstance().create().removeAll();
			NetSongPanel.getInstance().create().repaint();
			NetSongPanel.getInstance().create().add(tablePanel,BorderLayout.CENTER);
			NetSongPanel.getInstance().create().updateUI();
			tablePanel = null;
		} else {
			msgPanel.setMsg(getConfigMap().get("search.fail.msg").toString());
		}
		msgPanel = null;
	}

}
