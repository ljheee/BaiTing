package com.baiting.service.lyric;

import java.io.File;
import java.lang.Thread.State;
import java.util.Iterator;
import java.util.List;

import com.baiting.bean.Lyric;
import com.baiting.bean.LyricStatement;
import com.baiting.bean.Song;
import com.baiting.http.lyric.DownloadLyric;
import com.baiting.layout.LyricDisplayer;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.MusicService;

/**
 * 线程载入歌词(未使用)
 * @author lev
 *
 */
public class LoadLyricService extends MusicService implements Runnable {
	
	private Song song;
	
	private static LyricDisplayer showLyric;
	private Lyric lrc;
	private static int downing = 0;
	private static int downfail = 0;
	
	private Thread downLrcThread;
	
	private LyricStatement currentLyricStatement;
	
	private MusicPlayerService palyer;
	
	private ShowLyricService showLrcServ;
	
	public LoadLyricService(Song song) {
		this.song = song;
	}

	@Override
	public void run() {
		loadLyric();
		
	}
	
	/**
	 * 重新载入歌词
	 * @param song
	 */
	public void reloadLrc(Song song) {
		this.song = song;
		boolean isExist = true;
		if (song != null) {
			String path = getLrcDir() + "/" + song.getSinger() + "-" + song.getName() + LRC_EXT;
			File file = new File(path);
			if (!file.exists()) {
				path = getLrcDir() + "/" + song.getName() + LRC_EXT;
				file = new File(path);
				if(!file.exists()) {
					isExist = false;
				}
			}
			file = null;
			if(isExist) {
			   song.setLrcState(1);
			   LyricParseService lyricParse = new LyricParseService();
				lrc = lyricParse.readLrcFile(path, false);
				lyricParse = null;
				showLyric.prepareDisplay(lrc.getLrcInfos());
				
			} else {
				song.setLrcState(-1);
				lrc = null;
				LyricDisplayer.setMessage(getConfigMap().get("not.find.lrc.msg").toString());
				showLyric.displayLyric(-1);
			}
			
		}
	}
	
	
	/**
	 * 载入歌词
	 * @param song
	 */
	private void loadLyric() {
		boolean isExist = true;
		if (song != null) {
			String path = getLrcDir() + "/" + song.getSinger() + "-" + song.getName() + LRC_EXT;
			File file = new File(path);
			if (!file.exists()) {
				path = getLrcDir() + "/" + song.getName() + LRC_EXT;
				file = new File(path);
				if(!file.exists()) {
					isExist = false;
				}
			}
			file = null;
			if(isExist) {
			   song.setLrcState(1);
			} else {
				song.setLrcState(0);
			}
			LyricParseService lyricParse = new LyricParseService();
			lrc = lyricParse.readLrcFile(path, false);
			lyricParse = null;
			showLyric = LyricDisplayer.getInstance();
			if(lrc != null) {
				showLyric.prepareDisplay(lrc.getLrcInfos());
			} else {
				LyricDisplayer.setMessage(getConfigMap().get("searching.lrc.msg").toString());
				showLyric.prepareDisplay(null);
				String downLRCClassName = getConfigMap().get("down.lrc.class").toString();
				try {
					DownloadLyric downlrc = (DownloadLyric)Class.forName(downLRCClassName).newInstance();
					downlrc.setSong(song);
					downLrcThread = new Thread(downlrc);
					downLrcThread.start();
					downlrc = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(null != downLrcThread) {
				while(downLrcThread.getState() != State.TERMINATED) {
					
				}
			}
		}
	}

	
	/**
	 * 显示歌词
	 * @param time
	 */
	public void showLrc(long time) {
		if (lrc != null && "lrcLabel".equals(tabSelectedName)) {
				LyricStatement lrcState = null;
				lrcState = getLyricStatementByTime(time);
				if(lrcState != null) {
					if(null == palyer) {
						palyer = MusicPlayerService.getInstance();
					}
					if(null == showLrcServ) {
						showLrcServ = palyer.getShowLrcServ();
					}
					showLrcServ.setIndex(lrcState.getIndex()-1);
				}
				lrcState = null;
		} else if("lrcLabel".equals(tabSelectedName) && song.getLrcState()==0) {
				if(downing == 0) {
					if(null == showLyric) {
						showLyric = LyricDisplayer.getInstance();
					}
					LyricDisplayer.setMessage(getConfigMap().get("downing.lrc.msg").toString());
					showLyric.displayLyric(-1);
					downing = 1;
				}
		} else if("lrcLabel".equals(tabSelectedName) && song.getLrcState()==-1) {
				if(downfail == 0) {
					if(null == showLyric) {
						showLyric = LyricDisplayer.getInstance();
					}
				   LyricDisplayer.setMessage(getConfigMap().get("not.find.lrc.msg").toString());
				   showLyric.displayLyric(-1);
				   downfail = 1;
				}
		}
	}
	
	
	/**
	 * 分解歌词
	 * @param time
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private LyricStatement getLyricStatementByTime(long time) {
		LyricStatement preLyricStatement = null;
		LyricStatement returnLyricStatement = null;
		List<LyricStatement> list = lrc.getLrcInfos();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			LyricStatement lyricStatement = (LyricStatement) iterator.next();
			if(time == lyricStatement.getTime()){
				returnLyricStatement = lyricStatement;
				break;
			} else if(preLyricStatement != null) {
				if(time < lyricStatement.getTime() && time >= preLyricStatement.getTime()) {
					returnLyricStatement = preLyricStatement;
					break;
				}
			}
			preLyricStatement = lyricStatement;
		}
		preLyricStatement = null;
		if(null != currentLyricStatement && returnLyricStatement == null) {
			returnLyricStatement = list.get(list.size()-1);
		}
		list = null;
		if(null != returnLyricStatement && null != currentLyricStatement 
				&& currentLyricStatement.getTime() == returnLyricStatement.getTime()) {
			returnLyricStatement = null;
		} else {
			currentLyricStatement = returnLyricStatement;
		}
		return returnLyricStatement;
	}

}
