package com.baiting.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.baiting.bean.Song;
import com.baiting.layout.MusicListLayout;
import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.util.StringUtil;
import com.baiting.util.UrlFileUtil;

public class MusicControllerSerivce extends MusicService implements Runnable{

	private Song playSong;
	private SourceDataLine line;
	private AudioInputStream in,din;
	private AudioFormat decodedFormat;
	private static MusicControllerSerivce instance;
	private long total,skeepedTime;
	private long currentPlayTime;
	private DataLine.Info info;
	private boolean isFile = true;
	private MusicPlayControllerLayout playLayout;
	private File file;
	private int playSliderValue;
	private int status;
	
	private FloatControl gainControl;
	private FloatControl panControl;
	private double volume = 50;
	private AudioFileFormat aff;
	private boolean isPlay = false;
	private Thread m_thread;
	private URL url;
	private boolean isNetPlay = false;
	

	private MusicControllerSerivce(Song playSong) {
		this.playSong = playSong;
		init();
	}
	
	public synchronized static MusicControllerSerivce getInstance(Song song) {
		if(instance == null) {
			instance = new MusicControllerSerivce(song);
		} else {
	        instance.setPlaySong(song);
		}
		return instance;
	}
	
	public Song getPlaySong() {
		return playSong;
	}

	public void setPlaySong(Song playSong) {
		this.playSong = playSong;
	}

	private synchronized void init() {
		if(!StringUtil.isEmpty(playSong.getPath())) {
			file = new File(playSong.getPath());
			if(!file.exists()) {
				if(StringUtil.isEmpty(playSong.getUrl())) {
					isFile = false;
					MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
					return;
				}
				try {
					url = new URL(playSong.getUrl());
					isNetPlay = true;
				} catch (MalformedURLException e) {
					isFile = false;
					MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
					e.printStackTrace();
					return;
				}
			}
		} else {
			if(StringUtil.isEmpty(playSong.getUrl())) {
				isFile = false;
				MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
				return;
			}
			try {
				url = new URL(playSong.getUrl());
				isNetPlay = true;
			} catch (MalformedURLException e) {
				isFile = false;
				MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
				e.printStackTrace();
				return;
			}
		}
		try {
			long fileSize = 0l;
			if(isNetPlay) {
				aff = AudioSystem.getAudioFileFormat(url);
				in = AudioSystem.getAudioInputStream(url);
				fileSize = UrlFileUtil.getUrlFileSize(playSong.getUrl());
			} else {
				aff = AudioSystem.getAudioFileFormat(file);
				in = AudioSystem.getAudioInputStream(file);
			}
			Map<?, ?> props = aff.properties();
		  if (props.containsKey("duration")){
		        total = (long) Math.round((((Long) props.get("duration")).longValue()));    
		    }
		  if(isNetPlay) {
		    	total = UrlFileUtil.getPlayTimeLength(fileSize, props);
		    }
		   if(total<(1000*1000)) {
			   total = total*1000;
		    }
		  props = null;
			playLayout = MusicPlayControllerLayout.getInstance();
			playLayout.getSongInfoLabel().setText(playSong.getName()+"  "+playSong.getSinger());
			playLayout.getTotalTimeLabel().setText(getTotalLongTime(total));
			if (in != null) {
				AudioFormat baseFormat = in.getFormat();
				decodedFormat = new AudioFormat(
							AudioFormat.Encoding.PCM_SIGNED,
							baseFormat.getSampleRate(), 16,
							baseFormat.getChannels(), baseFormat.getChannels() * 2,
							baseFormat.getSampleRate(), false);
				log.info("Target Format : "+ decodedFormat.toString());
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				baseFormat = null;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
			MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
		} catch (Exception e) {
			e.printStackTrace();
			MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
		}
	}

	private synchronized SourceDataLine getLine() {
		SourceDataLine res = null;
		info = new DataLine.Info(SourceDataLine.class,decodedFormat);
		try {
			res = (SourceDataLine) AudioSystem.getLine(info);
			res.open(decodedFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	public void dragPlaySliber(double rate) {
		long bytes = 0;
		long totalSkipped = 0;
		skeepedTime = 0;
		long len = 0l;
		if(aff != null && din != null) {
			len = aff.getByteLength();
			log.info("文件长度:"+len);
			bytes = (int)Math.round((len*rate));
			skeepedTime = Math.round(rate*total);
		}
		setStatus(PLAY_DRAG_SLIDER_STATUS);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long skipped = 0;
		initAudioInputStream();
		
		if(din != null) {
			while (totalSkipped < (bytes - SKIP_INACCURACY_SIZE)) {
          try {
					skipped = din.skip(bytes - totalSkipped);
				} catch (IOException e) {
					e.printStackTrace();
				}
         if (skipped == 0) {
           break;
                }
         totalSkipped = totalSkipped + skipped;
         log.fine("Skipped : " + totalSkipped + "/" + bytes);
            }
			double actualRate = (double)totalSkipped/(double)len;
			skeepedTime = Math.round(actualRate*total);
			actualRate = 0l;
		}
		setStatus(PLAY_STATUS);
		log.info("跳转字节:"+bytes);
		log.info("跳转到:"+getTotalLongTime(skeepedTime));
		startPlayback();
	}
	
	
	/**
	 * 跳转播放（当拖动播放进度条时调用该方法）
	 */
	protected void startPlayback() {
		if(status == PLAY_STATUS) {
			if(m_thread != null) {
				m_thread.interrupt();
				m_thread = null;
			}
			m_thread = new Thread(this,"MusicControllerService");
			MusicPlayControllerLayout.getInstance().setPlayButtonToPauseIcon();
			setStatus(OPENED);
			m_thread.start();
		}
	}
	
	private void initAudioInputStream() {
		reset();
		init();
	}
	
	private void reset() {
		if(in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(din != null) {
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		in = null;
		din = null;
		decodedFormat = null;
		info = null;
		if(line != null) {
			line.drain();
			line.stop();
			line.close();
		}
		line = null;
	}
	
	/**
	 * 暂停
	 */
	public void stop() {
		if(status == PLAY_STATUS || status == PAUSE_STATUS) {
			setStatus(STOP_STATUS);
			MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
			if(null != line) {
				line.flush();
				line.stop();
			}
			synchronized (din) {
                din.notifyAll();
            }
			 if(null != din) {
				 try {
					din.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
			instance = null;
		}
	}
	
	/**
	 * 播放
	 */
	public synchronized void play() {
		if(status == PAUSE_STATUS) {
			resumePlay();
		} else {
			MusicPlayControllerLayout mpcl = MusicPlayControllerLayout.getInstance();
			mpcl.setPlayButtonToPauseIcon();
			setStatus(PLAY_STATUS);
			mpcl.getPlaySlider().setValue(0);
			if(null != playSong) {
				log.info("正在播放["+playSong.getPath()+"]");
			}
			setStatus(OPENED);
		}
	}
	
	/**
	 * 暂停
	 */
	public void pause() {
		if(status == PLAY_STATUS) {
			if(null != line) {
				line.stop();
			}
			setStatus(PAUSE_STATUS);
			MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
		}
	}
	
	/**
	 * 恢复播放(由暂停恢复播放)
	 */
	public void resumePlay() {
		if(status == PAUSE_STATUS){
			MusicPlayControllerLayout.getInstance().setPlayButtonToPauseIcon();
			setStatus(PLAY_STATUS);
			log.info("继续播放----");
			line.start();
		    synchronized (din) {
               din.notifyAll();
            }
	    }
	}

	@SuppressWarnings("unused")
	@Override
	public synchronized void run() {
		if(!isFile) {
			return;
		}
		byte[] data = new byte[4096];
		if(status == OPENED){
		   line = getLine();
	     } 
		if (line != null) {
			isPlay = true;
			try {
				changeVolume(volume);
				if(status == OPENED){
				    line.start();
				    setStatus(PLAY_STATUS);
				}
				log.info("---正在播放---"+playSong.getPath());
				synchronized (din) {
					int nBytesRead = 0, nBytesWritten = 0;
					while (nBytesRead != -1 && status != PLAY_DRAG_SLIDER_STATUS) {
						if(status == PAUSE_STATUS) {
							din.wait();
						} else {
							nBytesRead = din.read(data, 0, data.length);
							if(status == ADJUST_VOLUME_STATUS) {
								changeVolume(volume);
								setStatus(PLAY_STATUS);
							}
							if(status == STOP_STATUS) {
								playLayout.getPlaySlider().setValue(0);
								playLayout.getCurrentTimeLabel().setText(" 00:00");
								close();
								nBytesRead = -1;
								currentPlayTime = 0;
								skeepedTime = 0;
								isPlay = false;
								return;
							}
							if(status == SKIP_PLAY_STATUS) {
								close();
								nBytesRead = -1;
								return;
							}
							if (nBytesRead != -1)
								nBytesWritten = line.write(data, 0, nBytesRead);
							
							if(status != PLAY_DRAG_SLIDER_STATUS) {
								long microsecondPosition = line.getMicrosecondPosition();
								currentPlayTime = microsecondPosition+skeepedTime;
								if(total == 0) {
									total = 1;
								}
								playSliderValue = (int)(((double)currentPlayTime/(double)total)*100);
								if(status != DRAGING_PLAY_SLIDER && status != PLAY_DRAG_SLIDER_STATUS) {
									playLayout.getPlaySlider().setValue(playSliderValue);
								}
								MusicPlayerService.getInstance().showLrc(currentPlayTime);
								playLayout.getCurrentTimeLabel().setText(" "+getTotalLongTime(currentPlayTime));
							}
						}
					}
				}
				close();
				if(status == PLAY_STATUS) {
					MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
					log.info(playSong.getPath()+"---播放结束---");
					playLayout.getPlaySlider().setValue(0);
					playLayout.getCurrentTimeLabel().setText(" 00:00");
					currentPlayTime = 0;
					skeepedTime = 0;
					instance = null;
					if(playSong != null) {
						SongListService listService = new SongListService();
						Song songTmp = listService.getSong(playSong.getPlayListName(),playSong.getNo()+1 );
						if(songTmp == null) {
							songTmp = listService.getSong(playSong.getPlayListName(),1);
						}
						listService = null;
						if(songTmp != null) {
							MusicPlayerService.getInstance().close();
							Thread.sleep(1000);
							MusicPlayControllerLayout.getInstance().setPlayButtonToPlayIcon();
								
							MusicListLayout.getInstance().getSongList().setSelectedIndex(playSong.getNo());
							MusicListLayout.getInstance().getSongList().setSelectionBackground(getSongListSelectionBackground());
							MusicListLayout.getInstance().getSongList().setSelectionForeground(getSongListSelectionForeground());
							MusicPlayerService.getInstance().play(songTmp);
						}
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	private void close() {
		if(line != null) {
			line.drain();
			line.stop();
			if(line != null)
			   line.close();
		}
		if(din != null) {
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 时间转换（把微秒转换为mm:ss时间格式）
	 * @param microsecond
	 * @return
	 */
	public String getTotalLongTime(long microsecond) {
		long second = (long)Math.round((microsecond/(1000*1000)));
		int mm = (int)(second/60);
		int ss = (int)second%60;
		String mmStr = (mm<10?("0"+mm):mm)+"";
		String ssStr = (ss<10?("0"+ss):ss)+"";
		return mmStr+":"+ssStr;
	}
	
	
	/**
	 * 调整音量
	 * @param value
	 */
	private void changeVolume(double value) {
		if(line != null) {
			if(line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				gainControl = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
				double minGainDB = gainControl.getMinimum();
	            double ampGainDB = ((10.0f / 20.0f) * gainControl.getMaximum()) - gainControl.getMinimum();
	            double cste = Math.log(10.0) / 20;
	            value = value * 0.01414;
	            double valueDB = minGainDB + (1 / cste) * Math.log(1 + (Math.exp(cste * ampGainDB) - 1) * value);
	            log.finest("Gain : " + valueDB);
	            gainControl.setValue((float) valueDB);
			} else if(line.isControlSupported(FloatControl.Type.PAN)) {
				panControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
				value = (-1)+(0.02*value);
				log.info("Pan:"+value);
				panControl.setValue((float)value);
			}
		}
	}
	
	public void setVolume(double value,int stats) {
		if(stats == MUSIC_MUTE) {
			volume = 0;
		} else if(stats == MUSIC_NOT_MUTE) {
			volume = value;
		}
		setStatus(ADJUST_VOLUME_STATUS);
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		synchronized (this) {
			this.isPlay = isPlay;
		}
	}

}