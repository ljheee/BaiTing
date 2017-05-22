package com.baiting.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baiting.Music;
import com.baiting.bean.DownFailSong;
import com.baiting.bean.DownedSong;
import com.baiting.bean.Song;
import com.baiting.layout.DownFailLayout;
import com.baiting.layout.DownedLayout;
import com.baiting.layout.DowningLayout;
import com.baiting.layout.MusicListLayout;
import com.baiting.service.DownloadSongService;
import com.baiting.service.SongListService;
import com.baiting.util.CommonUtil;
import com.baiting.util.UrlFileUtil;

/**
 * 下载歌曲
 * @author lmq
 * 
 */
public class DownloadSong extends Music implements Runnable {
	 private static final int BUFF_LENGTH = 1024;
	@Override
	public void run() {
		if(null != downingNetSongList && downingNetSongList.size()>0) {
			int i = 0;
			while(downingNetSongList.size()>0) {
				i = 0;
				String status = getConfigMap().get("down.status.ready").toString();
				MusicListLayout.showMsg(status+"《"+downingNetSongList.get(i).getSongName()+"》");
				downingNetSongList.get(i).setStatus(status);
				DowningLayout.getDowningTable().setValueAt(status, i, 0);
				long fileSize = UrlFileUtil.getUrlFileSize(downingNetSongList.get(i).getUrl());
				long downedSize = 0;
				String singer = downingNetSongList.get(i).getSinger();
				String name = downingNetSongList.get(i).getSongName();
				File downSongFile = new File(getDownloadSongPath()+singer+"-"+name+".mp3");
				int disTimes=1;
				try {
					while(downSongFile.exists()) {
						downSongFile = new File(getDownloadSongPath()+singer+"-"+name+"("+disTimes+").mp3");
						disTimes++;
					}
					downSongFile.createNewFile();
					URL url = new URL(downingNetSongList.get(i).getUrl());
					byte[] buff = new byte[BUFF_LENGTH];
					int length = -1;
					long startTime = System.currentTimeMillis();
					long preSpeed = 0;
					long currentLength = 0;
					int exeTimes = 1;
					status = getConfigMap().get("downing.info.label").toString();
					MusicListLayout.showMsg(status+"《"+downingNetSongList.get(i).getSongName()+"》");
					downingNetSongList.get(i).setStatus(status);
					DowningLayout.getDowningTable().setValueAt(status, i, 0);
					//setHeader(urlConnection);
					HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
					OutputStream out = new FileOutputStream(downSongFile,true);
					InputStream in = urlConnection.getInputStream();
					while((length = in.read(buff))>0) {
						out.write(buff,0,length);
						downedSize += length;
						long endTime = System.currentTimeMillis();
						long intervalTime = endTime - startTime;
						if(exeTimes == 1) {
							int complete = (int)(((double)downedSize/fileSize)*100);
							String completeStr = complete+"%";
							downingNetSongList.get(i).setProgress(completeStr);
							DowningLayout.getDowningTable().setValueAt(completeStr, i, 3);
							if(intervalTime==0) {
								intervalTime = 1;
							}
							int speed = (int)((currentLength+length)/intervalTime);
							currentLength = 0;
							if(preSpeed != speed) {
								String speedStr = speed+"kb/s";
								downingNetSongList.get(i).setSpeed(speedStr);
								DowningLayout.getDowningTable().setValueAt(speedStr, i, 4);
							}
							preSpeed = speed;
							long remainTime = 0;
							if(speed < 1) {
						    	remainTime = (long)((fileSize-downedSize)/0.01);
						    } else {
						    	remainTime = (fileSize-downedSize)/(speed);
						    }
							String remainTimeStr = CommonUtil.millisecondsToHHMMSS(remainTime);
							downingNetSongList.get(i).setRemainTime(remainTimeStr);
							DowningLayout.getDowningTable().setValueAt(remainTimeStr, i, 5);
						} else {
							if(intervalTime%100==0 && intervalTime != 0) {
								int complete = (int)(((double)downedSize/fileSize)*100);
								String completeStr = complete+"%";
								downingNetSongList.get(i).setProgress(completeStr);
								DowningLayout.getDowningTable().setValueAt(completeStr, i, 3);
								int speed = (int)((currentLength+length)/intervalTime);
								currentLength = 0;
								if(preSpeed != speed) {
									String speedStr = speed+"kb/s";
									downingNetSongList.get(i).setSpeed(speedStr);
									DowningLayout.getDowningTable().setValueAt(speedStr, i, 4);
								}
								preSpeed = speed;
								long remainTime = 0;
								if(speed < 1) {
							    	remainTime = (long)((fileSize-downedSize)/0.01);
							    } else {
							    	remainTime = (fileSize-downedSize)/(speed);
							    }
								String remainTimeStr = CommonUtil.millisecondsToHHMMSS(remainTime);
								downingNetSongList.get(i).setRemainTime(remainTimeStr);
								DowningLayout.getDowningTable().setValueAt(remainTimeStr, i, 5);
								startTime = System.currentTimeMillis();
							} else {
								currentLength = currentLength+length;
							}
						}
						exeTimes = 2;
					}
					in.close();
					out.close();
					urlConnection.disconnect();
					status = getConfigMap().get("downed.status").toString();
					downingNetSongList.get(i).setStatus(status);
					DowningLayout.getDowningTable().setValueAt(status, i, 0);
					MusicListLayout.showMsg("《"+downingNetSongList.get(i).getSongName()+"》已下载完");
					log.info("【"+singer+"-"+name+"】的歌曲下载完成...");
					
					//添加到已下载列表
					DownedSong downedSong = new DownedSong();
					downedSong.setFileName(downSongFile.getName());
					DecimalFormat formatter = new DecimalFormat("0.00");
					downedSong.setFileSize(Double.parseDouble(formatter.format((double)fileSize/1024/1024)));
					downedSong.setSongName(downingNetSongList.get(i).getSongName());
					downedSong.setSinger(downingNetSongList.get(i).getSinger());
					downedSong.setPath(downSongFile.getAbsolutePath());
					SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					downedSong.setCreateTime(dateFormater.format(new Date()));
					int row = DownloadSongService.getInstance().addDownedSong(downedSong);
					DownedLayout.addRows(downedSong,row);
					downedSong = null;
					//更新播放列表
					if(downingNetSongList.get(i).getListNo() > 0) {
						Song songTmp = new Song();
						songTmp.setNo(downingNetSongList.get(i).getListNo());
						songTmp.setPath(downSongFile.getAbsolutePath());
						songTmp.setPlayListName(downingNetSongList.get(i).getPlayListName());
						SongListService listService = new SongListService();
						listService.updateSongPath(songTmp);
						if(playListName.equals(downingNetSongList.get(i).getPlayListName())) {
							MusicListLayout.refreshSongList(playListName);
						}
						songTmp = null;
					}
					
					//删除一行
					downingNetSongList.remove(downingNetSongList.get(i));
					DowningLayout.removeRow(i);
				} catch (IOException e) {
					if(downSongFile.exists()) {
						downSongFile.delete();
					}
					log.info(singer+"-"+name+"下载失败");
					MusicListLayout.showMsg("《"+downingNetSongList.get(i).getSongName()+"》下载失败");
					
					DownFailSong dfs = new DownFailSong();
					dfs.setSongName(downingNetSongList.get(i).getSongName());
					dfs.setSinger(downingNetSongList.get(i).getSinger());
					dfs.setFormat("mp3");
					SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					dfs.setFailTime(dateFormater.format(new Date()));
					int row = DownloadSongService.getInstance().addDownFailSong(dfs);
					DownFailLayout.addRows(dfs,row);
					dfs = null;

					e.printStackTrace();
					downingNetSongList.remove(downingNetSongList.get(i));
					DowningLayout.removeRow(i);
					continue;
				}
				downSongFile = null;
			}
		}
	}
	
	
	public static void setHeader(URLConnection conn) {
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
        conn.setRequestProperty("Accept-Encoding", "utf-8");
        conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        conn.setRequestProperty("Keep-Alive", "300");
        conn.setRequestProperty("connnection", "keep-alive");
        conn.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
        conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
        conn.setRequestProperty("Cache-conntrol", "max-age=0");
        conn.setRequestProperty("Referer", "http://www.baidu.com");
    }

}
