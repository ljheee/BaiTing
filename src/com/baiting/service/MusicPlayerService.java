package com.baiting.service;

import java.awt.BorderLayout;
import java.lang.Thread.State;

import com.baiting.bean.Song;
import com.baiting.http.picture.DownloadPicture;
import com.baiting.layout.LyricDisplayer;
import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.layout.ShowLyricPanel;
import com.baiting.service.lyric.LoadLyricService;
import com.baiting.service.lyric.ShowLyricService;
import com.baiting.util.StringUtil;

/**
 * 
 * @author lev
 *
 */
public class MusicPlayerService extends MusicService {

	private MusicControllerSerivce music;
	
	private Song song;
	
	private static MusicPlayerService instance;
	private Thread musicThread;
	
	private Thread loadLrcThread;
	
	private LoadLyricService loadLrc;
	private ShowLyricService showLrcServ;
	private LyricDisplayer showLyric;
	
	private MusicPlayerService() {}
	
	public static synchronized MusicPlayerService getInstance() {
		if(instance == null) {
			instance = new MusicPlayerService();
		}
		return instance;
	}
	
	/**
	 * 播放
	 * @param song
	 */
	public synchronized void play(Song song) {
		this.song = song;
		if(music == null) {
			if(musicThread != null) {
			    musicThread.interrupt();
			}
		} 
		//初始化显示歌词面板
		if(null == showLyric) {
			showLyric = LyricDisplayer.getInstance();
			showLyric.setBackGroundImagePath(getBasePath()+getSeparator()+getConfigMap().get("lrc.background").toString());
			ShowLyricPanel lrcPanel =  ShowLyricPanel.getInstance();
			lrcPanel.create().add(showLyric,BorderLayout.CENTER);
			lrcPanel = null;
			showLyric.setLocalPic(true);
		}
		
		//播放音乐
		MusicPlayControllerLayout.getInstance().setPlayButtonToPauseIcon();
		music = MusicControllerSerivce.getInstance(song);
		musicThread = new Thread(music);
		music.play();
		musicThread.start();
		
		//启动显示歌词服务线程
		showLrcServ = new ShowLyricService(this,showLyric);
		Thread showLrcThread = new Thread(showLrcServ);
		showLrcThread.start();
		
		//下载歌词
		if((song == null) || (!song.toString().equals(song))) {
			loadLrc = new LoadLyricService(song);
			if(null != loadLrcThread && loadLrcThread.getState() != State.TERMINATED) {
				loadLrcThread.interrupt();
				loadLrcThread = null;
			}
			loadLrcThread = new Thread(loadLrc);
			loadLrcThread.start();
		}
		bkPicList = null;
		
		//载入歌手图片
		Object isSingerLoadPic = getConfigMap().get("singer.picture.auto.loading");
		if(null != isSingerLoadPic && "1".equals(isSingerLoadPic.toString())) {
			Object singerLoadPicClassName = getConfigMap().get("loading.singer.picture.class");
			if(null != singerLoadPicClassName && !StringUtil.isEmpty(singerLoadPicClassName.toString())) {
				try {
					DownloadPicture downPic = (DownloadPicture)Class.forName(singerLoadPicClassName.toString()).newInstance();
				    downPic.setSong(song);
				    Thread downPicThread = new Thread(downPic);
				    downPicThread.start();
				    downPic = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}//end[if]
		}//end[if]
	}//end[play]
	
	
	/**
	 * 停止
	 */
	public synchronized void stop() {
		if(null != music) {
			music.stop();
		}
		if(musicThread != null)
		    musicThread.interrupt();
	}
	
	public void close() {
		music = null;
	}
	
	/**
	 * 暂停
	 */
	public void pause() {
		if(null != music) {
			music.pause();
		}
	}
	
	/**
	 * 暂停后重新播放
	 */
	public void resumePlay() {
		if(null != music) {
			music.resumePlay();
		}
	}
	
	/**
	 * 音量
	 * @param arg0
	 * @param arg1
	 */
	public synchronized void setVolume(double arg0,int arg1) {
		if(null != music)
		   music.setVolume(arg0, arg1);
	}
	
	/**
	 * 拖动播放进度条
	 * @param rate
	 */
	public synchronized void dragPlaySliber(double rate) {
		if(null != music)
		   music.dragPlaySliber(rate);
	}

	/**
	 * 获取状态(播放状态）
	 * 如：暂停、播放、停止等
	 * @return
	 */
	public int getStatus() {
		if(null != music) {
			return music.getStatus();
		} else {
			return 0;
		}
		
	}
	
	/**
	 * 设置状态
	 * @param status
	 */
	public void setStatus(int status) {
		if(null != music) {
			music.setStatus(status);
		}
	}

	/**
	 * 获取当前播放的歌曲
	 * @return
	 */
	public Song getSong() {
		return song;
	}

	/**
	 * 显示歌词(逐句显示歌词)
	 * @param time
	 */
	public void  showLrc(long time) {
		if(null != loadLrc)
		   loadLrc.showLrc(time);
	}
	
	/**
	 * 重新载入指定歌曲的歌词
	 * @param song
	 */
	public void reloadLrc(Song song) {
		if(null != loadLrc)
		   loadLrc.reloadLrc(song);
	}

	public ShowLyricService getShowLrcServ() {
		return showLrcServ;
	}
	
}
