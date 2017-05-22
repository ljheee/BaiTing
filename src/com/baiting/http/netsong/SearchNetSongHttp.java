package com.baiting.http.netsong;

import com.baiting.bean.NetSong;

public class SearchNetSongHttp extends NetSongHttp {

	protected NetSong netSong;
	
	public SearchNetSongHttp() {
		super();
	}
	
	public SearchNetSongHttp(NetSong netSong) {
		this.netSong = netSong;
	}

	public void setNetSong(NetSong netSong) {
		this.netSong = netSong;
	}
	
}
