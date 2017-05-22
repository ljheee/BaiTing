package com.baiting.http.netsong;


public class NetSongList extends NetSongHttp {

	protected String[] songTypes = getConfigMap().get("net.song.types.label").toString().split(",");
	
	public NetSongList() {
		super();
	}
	
	public void setType(int type) {
		
	}

}
