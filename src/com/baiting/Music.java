package com.baiting;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.baiting.bean.BackgroundPicture;
import com.baiting.bean.DowningNetSong;
import com.baiting.util.CommonUtil;

public class Music implements IMusic {
	
	/**
	 * 窗口宽度
	 */
	public static int MUSIC_WINDOW_WIDTH;
	/**
	 * 窗口高度
	 */
	public static int MUSIC_WINDOW_HEIGHT;
	
	public static int MUSIC_WINDOW_RIGHT_WIDTH;
	
	public static int MUSIC_WINDOW_TOP_HEIGHT;
	
	public static int MUSIC_WINDOW_BOTTOM_HEIGHT;
	
	public static int MUSIC_WINDOW_TITLE_HEIGHT;
	
	public static int MUSIC_WINDOW_PLAY_LIST_WIDTH;

	protected static final Logger log = Logger.getLogger(Music.class.getName());
	
	public static Map<String,Object> CONFIG_MAP = null;
	
	private static int width;
	private static int height;
	private static String basePath;
	//protected static Song song;
	protected static String playListName = null;
	protected static String tabSelectedName = "lrcLabel";
	
	/**
	 * 网络歌曲模块：1--新歌；2--热歌；3--经典
	 */
	protected static int netSongType = 1;
	
	/**
	 * 下载模块：1--正在下载；2--已下载；3--下载失败
	 */
	protected static int downloadType = 1;
	protected static List<DowningNetSong> downingNetSongList;
	
	protected static List<BackgroundPicture> bkPicList = new ArrayList<BackgroundPicture>();
	
	public Music() {
		width = MUSIC_WINDOW_WIDTH;
		height = MUSIC_WINDOW_HEIGHT;
		if(null != CONFIG_MAP) {
			playListName = getConfigMap().get("play.list.default.name").toString();
		}
	}
	
	/**
	 * 初始化常量值
	 */
	public void initConstValue() {
		Object autoSize = getConfigMap().get("music.window.auto.size");
		if(null != autoSize && autoSize.equals("0")) {
			MUSIC_WINDOW_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.85);
			MUSIC_WINDOW_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.85);
		} else {
			String widthStr = getConfigMap().get("music.window.width").toString();
			String heightStr = getConfigMap().get("music.window.height").toString();
			if(null == widthStr || "".equals(widthStr.trim())) {
				MUSIC_WINDOW_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.85);
			} else {
				MUSIC_WINDOW_WIDTH = Integer.parseInt(widthStr.trim());
			}
			
			if(null == heightStr || "".equals(heightStr.trim())) {
				MUSIC_WINDOW_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.85);
			} else {
				MUSIC_WINDOW_HEIGHT = Integer.parseInt(heightStr.trim());
			}
		}
		MUSIC_WINDOW_RIGHT_WIDTH = Integer.parseInt(getConfigMap().get("music.window.right.width").toString().trim());
		MUSIC_WINDOW_TOP_HEIGHT = Integer.parseInt(getConfigMap().get("music.window.top.height").toString().trim());
		MUSIC_WINDOW_BOTTOM_HEIGHT = Integer.parseInt(getConfigMap().get("music.window.bottom.height").toString().trim());
		MUSIC_WINDOW_TITLE_HEIGHT = Integer.parseInt(getConfigMap().get("music.window.title.height").toString().trim());
		MUSIC_WINDOW_PLAY_LIST_WIDTH = Integer.parseInt(getConfigMap().get("music.window.play.list.width").toString().trim());
		
	}

	public static int getWidth() {
		return width;
	}

	public static void setWidth(int width) {
		Music.width = width;
	}
	
	public static String getFontPath() {
		return getBasePath()+getSeparator()+"fonts/simsun.ttc";
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		Music.height = height;
	}

	public static String getBasePath() {
		basePath = System.getProperty("user.dir");
		//log.info("当前工作目录:"+basePath);
		return basePath;
	}
	
	/**
	 * 文件分隔符 
	 * @return
	 */
	public static String getSeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * 获取播放列表文件的路径
	 * @return
	 */
	public static String getListFilePath() {
		return getBasePath()+getSeparator()+LIST_FILE_NAME;
	}
	
	public static String getSongListPath() {
		return getBasePath()+getSeparator()+SONG_LIST_DIR;
	}
	
	/**
	 * 获取支持的播放格式
	 * @return
	 */
	public static String[] getSupportPlayFormatter() {
		String supportStr = getConfigMap().get("support.play.formatter").toString();
		return supportStr.split(",");
	}
	
	
	/**
	 * 获取显示图片的格式
	 * @return
	 */
	public static String[] getSupportPictureFormatter() {
		String supportStr = getConfigMap().get("support.picture.formatter").toString();
		return supportStr.split(",");
	}
	
	/**
	 * 获取图标路径
	 * @return
	 */
	public static String getIconPath() {
		return getBasePath()+getSeparator()+ICON_PATH;
	}
	
	public int getBottomHeight() {
		return 60;
	}
	
	/**
	 * 歌词所在路径
	 * @return
	 */
	public String getLrcDir() {
		return getBasePath()+"/lyrics";
	}
	
	/**
	 * 歌手图片所在路径
	 * @return
	 */
	public String getSingerPicDir() {
		return getBasePath()+"/download/pics";
	}
	
	public String getDownloadSongPath() {
		return getBasePath()+DOWNLOAD_SONG_PATH;
	}
	
	public static Color getPlayListSelectionBackground() {
		return CommonUtil.getColor("#CA9990");
	}
	
	public static Color getPlayListSelectionForeground() {
		return CommonUtil.getColor("#FFFFFF");
	}
	
	public static Color getSongListSelectionBackground() {
		return CommonUtil.getColor("#ECDBD8");
	}
	
	public static Color getSongListSelectionForeground() {
		return CommonUtil.getColor("#226699");
	}

	public static String getTabSelectedName() {
		return tabSelectedName;
	}

	public static void setTabSelectedName(String tabSelectedName) {
		Music.tabSelectedName = tabSelectedName;
	}

	public static String getPlayListName() {
		return playListName;
	}

	public static void setPlayListName(String playListName) {
		Music.playListName = playListName;
	}

	public static Map<String, Object> getConfigMap() {
		return CONFIG_MAP;
	}

	public static List<BackgroundPicture> getBkPicList() {
		return bkPicList;
	}

	public void addBkPicList(BackgroundPicture bkPic) {
		if(null == bkPicList) {
			bkPicList = new ArrayList<BackgroundPicture>();
		}
		bkPicList.add(bkPic);
	}

}
