package com.baiting.http.netsong;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.baiting.bean.DowningNetSong;
import com.baiting.bean.NetSong;
import com.baiting.bean.Song;
import com.baiting.layout.DowningLayout;
import com.baiting.layout.MusicListLayout;
import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.service.DownloadSongService;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.SongListService;
import com.baiting.util.StringUtil;
import com.baiting.util.UrlFileUtil;

public class SongUrlHtmlParse extends SearchNetSongHttp {
	
	public static final int FLAG_LISTENING = 1;
	public static final int FLAG_ADD = 2;
	public static final int FLAG_DOWNLOAD = 3;
	public boolean isSearch;
	
	private NetSong netSong;
	private int flag;
	private String downPageUrl;
	
	public SongUrlHtmlParse() {
		super();
	}
	
	public SongUrlHtmlParse(NetSong netSong,int flag,String downPageUrl) {
		super();
		this.netSong = netSong;
		this.flag = flag;
		if(!StringUtil.isEmpty(downPageUrl)) {
			isSearch = true;
			this.downPageUrl = downPageUrl;
		} else {
			isSearch = false;
		}
	}
	
	public synchronized String parseNetSongUrl(NetSong netSong) {
		if(null == netSong) {
			return null;
		}
		String result = "";
		String songUrl = "";
		String singer = netSong.getSinger().replaceAll("&", ",");
		result = netSong.getSongName()+" "+singer;
		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		String parseUrl = "http://music.baidu.com/search?key="+result;
		log.info(parseUrl);
		String[] contents = null;
		try {
			contents = getHtmlContents(parseUrl);
			for (int i = 0; i < contents.length; i++) {
				String line = contents[i].replaceAll("\r|\n|\t", "").trim();
				if(StringUtil.isContains(line, "<li data-songitem = '\\{&quot;songItem&quot;")) {
					String datas = line.replaceAll("(.*)'\\{&quot;songItem&quot;\\:(.*)&quot;\\}\\}'(.*)", "$2").trim();
					datas = datas.replaceAll("&quot;", "'")+"'}";
					NetSong netSongTmp = parseJsonData(datas);
					if(null != netSongTmp) {
						if(netSongTmp.getSongName().equals(netSong.getSongName()) &&
								netSongTmp.getSinger().equals(netSong.getSinger())) {
							songUrl = super.getNetSongUrl(netSongTmp.getSid(), NET_SONG_LIST_SEARCH);
							netSongTmp = null;
							break;
						}
					}
				}
			}
			
		} catch (Exception e) {
			log.info("在线试听---HttpRequest---异常");
			e.printStackTrace();
			return null;
		} finally {
			contents = null;
			netSong = null;
		}
		
		return songUrl;
	}

	@Override
	public synchronized void run() {
		String url = "";
	    if(!isSearch) {
	    	if(!StringUtil.isEmpty(netSong.getSid())) {
	    		url = getNetSongUrl(netSong.getSid(), netSong.getType());
	    	} else {
	    		url = parseNetSongUrl(netSong);
	    	}
	    } else {
	    	url = downPageUrl;
	    }
		if(!StringUtil.isEmpty(url)) {
			netSong.setUrl(url);
			if(flag == FLAG_ADD || flag == FLAG_LISTENING) {
				SongListService listService = new SongListService();
				int rowTmp = listService.existSongList(netSong,getPlayListName());
				int row = 0;
				boolean isAdd = false;
				if(rowTmp == -1) {
					row = listService.addNetSong(netSong,getPlayListName());
					isAdd = true;
					MusicListLayout.refreshSongList(getPlayListName());
				} else {
					row = rowTmp;
				}
				if(flag == FLAG_LISTENING && row>0) {
					Song songTmp = new Song();
					if(isAdd) {
						songTmp.setName(netSong.getSongName());
						songTmp.setSinger(netSong.getSinger());
						songTmp.setPlayListName(getPlayListName());
						songTmp.setUrl(url);
						songTmp.setNo(row);
					} else {
						songTmp = listService.getSong(row);
					}
					MusicPlayerService.getInstance().stop();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					MusicPlayControllerLayout.getInstance().setPlayButtonToPauseIcon();
					
					
					MusicListLayout.hiddenInfo();
					MusicListLayout.getInstance().getSongList().setSelectedIndex(row-1);
					MusicListLayout.getInstance().getSongList().setSelectionBackground(getSongListSelectionBackground());
					MusicListLayout.getInstance().getSongList().setSelectionForeground(getSongListSelectionForeground());
					MusicPlayerService.getInstance().play(songTmp);
				}
			} else if(flag == FLAG_DOWNLOAD) {
				if(null == downingNetSongList) {
					downingNetSongList = new ArrayList<DowningNetSong>();
				}
				int no = downingNetSongList.size();
				DowningNetSong downingNetSong = new DowningNetSong();
				downingNetSong.setFileName(netSong.getSongName()+"-"+netSong.getSinger());
				downingNetSong.setSinger(netSong.getSinger());
				downingNetSong.setSongName(netSong.getSongName());
				downingNetSong.setNo(no+1);
				downingNetSong.setListNo(0);
				downingNetSong.setProgress("--");
				downingNetSong.setRemainTime("--");
				downingNetSong.setSpeed("--");
				downingNetSong.setStatus(getConfigMap().get("down.status.wait").toString());
				downingNetSong.setUrl(netSong.getUrl());
				long fileSize = UrlFileUtil.getUrlFileSize(netSong.getUrl());
				DecimalFormat formatter = new DecimalFormat("0.00");
				downingNetSong.setFileSize(formatter.format((double)fileSize/1024/1024)+"M");
				MusicListLayout.showMsg(netSong.getSongName()+getConfigMap().get("downed.alert.msg").toString());
				downingNetSongList.add(downingNetSong);
				DowningLayout.addRows(downingNetSong);
				DownloadSongService.getInstance().startDownload();
			}
		} else {
			MusicListLayout.showErrorMsg(getConfigMap().get("song.down.notfind.resource.msg").toString().replaceAll("\\$\\{param\\}", netSong.getSongName()));
			log.info(netSong.getSongName()+"--对应的URL获取失败----");
		}
	}
}
