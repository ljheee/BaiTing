package com.baiting;

import java.util.logging.Logger;

public interface IMusic {
	
	public final static String ICON_PATH = "icon";
	
	public final static Logger log = Logger.getLogger(IMusic.class.getName());
	
	public final static String LIST_FILE_NAME = "setting/list.conf";
	
	public final static String DOWNLOAD_SONG_PATH = "/download/song/";
	
	public final static String DOWNLOAD_PATH = "/download/";
	
	public final static String SEPARATOR = "##";
	
	//public final static String DEFAULT_PLAYLIST_NAME = "默认播放列表";
	
	public final static String DEFAULT_PLAYLIST_FILE_NAME = "default";
	
	public final static String SONG_LIST_DIR = "list";
	
	/**
	 * 播放列表文件名称后缀
	 */
	public final static String SONG_LIST_FILE_EXT = ".list";
	
	public final static String LRC_EXT = ".lrc";
	public final static String PIC_EXT = ".jpg";
	
	public final static int OPENED = 80;
	public final static int PLAY_STATUS = 1;
	public final static int STOP_STATUS = 0;
	public final static int PAUSE_STATUS = 2;
	public final static int PLAY_FINISHING = 11;
	
	/**
	 * 拖动播放进度条
	 */
	public final static int PLAY_DRAG_SLIDER_STATUS = 3;
	
	public final static int MUSIC_MUTE = 4;
	public final static int MUSIC_NOT_MUTE = 6;
	
	public final static int ADJUST_VOLUME_STATUS = 5;
	
	public final static int DRAGING_PLAY_SLIDER = 7;
	
	public final static int SKIP_PLAY_STATUS = 8;
	
	/**
	 * 最大音量
	 */
	public final static int MAX_VOLUME_SLIDER = 100;
	
	/**
	 * 最大播放进度
	 */
	public final static int MAX_PLAY_SLIDER = 100;
	
	public final static int SKIP_INACCURACY_SIZE = 512;
	
	
	/**
	 * 网络歌曲列表(baidu)---新歌TOP100
	 */
	public final static int NET_SONG_LIST_TOP100 = 1;
	
	/**
	 * 网络歌曲列表(baidu)---歌曲TOP500
	 */
	public final static int NET_SONG_LIST_HOT500 = 2;
	
	/**
	 * 网络歌曲列表(baidu)---经典老哥
	 */
	public final static int NET_SONG_LIST_OLD = 3;
	
	/**
	 * 网络歌曲列表(baidu)---网络歌曲
	 */
	public final static int NET_SONG_LIST_NET = 4;
	
	/**
	 * 搜索
	 */
	public final static int NET_SONG_LIST_SEARCH = 5;
	
	
	
}

