package com.baiting.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baiting.bean.DownFailSong;
import com.baiting.bean.DownedSong;
import com.baiting.http.DownloadSong;
import com.baiting.util.StringUtil;

public class DownloadSongService extends MusicService {

	private static DownloadSongService instance;
	private static Thread downloadThread;
	
	private DownloadSongService() {
		
	}
	
	public static DownloadSongService getInstance() {
		if(null == instance) {
			instance = new DownloadSongService();
		}
		return instance;
	}
	
	public void startDownload() {
		if(null == downloadThread) {
			downloadThread = new Thread(new DownloadSong());
			downloadThread.start();
		} else {
			if(!downloadThread.isAlive()) {
				downloadThread.interrupt();
				downloadThread = null;
				downloadThread = new Thread(new DownloadSong());
				downloadThread.start();
			}
		}
	}
	
	
	public List<DownedSong> getDownedSongAll() {
		File downedListFile = new File(getBasePath()+DOWNLOAD_PATH+"downed.list");
		if(!downedListFile.exists()) {
			try {
				log.info("downed.list文件不存在，正在创建....");
				downedListFile.createNewFile();
				log.info("downed.list文件创建[成功]....");
			} catch (IOException e) {
				log.info("downed.list文件创建[失败--异常]....");
				e.printStackTrace();
				downedListFile = null;
				return null;
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(downedListFile));
		    String line = reader.readLine();
		    List<DownedSong> list = new ArrayList<DownedSong>();
		    int count = 0;
		    if(!StringUtil.isEmpty(line)) {
		    	while(line != null) {
		    		count++;
		    		String content = line.replace("\n", "").trim();
					String[] cols = content.split(SEPARATOR);
					if(cols.length>5) {
						DownedSong downedSong = new DownedSong();
						downedSong.setNo(count);
						downedSong.setFileName(cols[0].trim());
						downedSong.setSongName(cols[1].trim());
						downedSong.setSinger(cols[2].trim());
						downedSong.setFileSize(Double.parseDouble(cols[3].trim()));
						downedSong.setPath(cols[4].trim());
						downedSong.setCreateTime(cols[5].trim());
						list.add(downedSong);
						downedSong = null;
					}
					line = reader.readLine();
		    	}
		    }
		    reader.close();
		    reader = null;
		    if(list.size()>0) {
		    	return list;
		    }
		    return null;
		} catch (FileNotFoundException e) {
			log.info("文件不存在...");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			downedListFile = null;
		}
	}
	
	
	/**
	 * 扫描目录---未完成
	 * @return
	 */
	public List<DownedSong> getDownedSongByDir() {
		String downedSongDir = getDownloadSongPath();
		File downedDir = new File(downedSongDir);
		if(downedDir.exists() && downedDir.isDirectory()) {
			String[] fileList = downedDir.list();
			for (int i = 0; i < fileList.length; i++) {
				
			}
		}
		return null;
	}
	
	
	/**
	 * 判断歌曲是否存在(通过歌曲名和歌手)
	 * @param songName
	 * @param singer
	 * @return
	 */
	public boolean existSongByInfo(String songName,String singer) {
		List<DownedSong> list = getDownedSongAll();
		if(null == list || list.size()<1) {
			return false;
		} 
		boolean flag = false;
		for(DownedSong downedSong : list) {
			if(downedSong.getSongName().equals(songName) && downedSong.getSinger().equals(singer)) {
				flag = true;
				break;
			}
		}
		list = null;
		return flag;
	}
	
	/**
	 * 已下载列表中加入新数据
	 * @param downedSong
	 * @return
	 */
	public int addDownedSong(DownedSong downedSong) {
		File downedListFile = new File(getBasePath()+DOWNLOAD_PATH+"downed.list");
		if(!downedListFile.exists()) {
			try {
				log.info("downed.list文件不存在，正在创建....");
				downedListFile.createNewFile();
				log.info("downed.list文件创建[成功]....");
			} catch (IOException e) {
				log.info("downed.list文件创建[失败--异常]....");
				e.printStackTrace();
				downedListFile = null;
				return -1;
			}
		}
		if(null != downedSong) {
			String contents = downedSong.getFileName()+SEPARATOR+
			downedSong.getSongName()+SEPARATOR+downedSong.getSinger()+SEPARATOR+
			downedSong.getFileSize()+SEPARATOR+downedSong.getPath()+SEPARATOR+
			downedSong.getCreateTime()+"\n";
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(downedListFile,true));
				writer.write(contents);
				writer.flush();
				writer.close();
				writer = null;
				List<DownedSong> lists = getDownedSongAll();
				int count = lists.size();
				lists = null;
				return count;
			} catch (IOException e) {
				log.info(downedListFile.getName()+"文件信息写入[失败---异常]");
				e.printStackTrace();
				return -1;
			} finally {
				downedListFile = null;
				downedSong = null; 
			}
		}
		return -1;
	}
	
	
	
	
	/**
	 * 获取所有下载失败的歌曲
	 * @return
	 */
	public List<DownFailSong> getDownFailSongAll() {
		File downedListFile = new File(getBasePath()+DOWNLOAD_PATH+"downFail.list");
		if(!downedListFile.exists()) {
			try {
				log.info("downFail.list文件不存在，正在创建....");
				downedListFile.createNewFile();
				log.info("downFail.list文件创建[成功]....");
			} catch (IOException e) {
				log.info("downFail.list文件创建[失败--异常]....");
				e.printStackTrace();
				downedListFile = null;
				return null;
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(downedListFile));
		    String line = reader.readLine();
		    List<DownFailSong> list = new ArrayList<DownFailSong>();
		    int count = 0;
		    if(!StringUtil.isEmpty(line)) {
		    	while(line != null) {
		    		count++;
		    		String content = line.replace("\n", "").trim();
					String[] cols = content.split(SEPARATOR);
					if(cols.length>3) {
						DownFailSong failSong = new DownFailSong();
						failSong.setNo(count);
						failSong.setSongName(cols[0].trim());
						failSong.setSinger(cols[1].trim());
						failSong.setFormat(cols[2].trim());
						failSong.setFailTime(cols[3].trim());
						list.add(failSong);
					}
					line = reader.readLine();
		    	}
		    }
		    reader.close();
		    reader = null;
		    if(list.size()>0) {
		    	return list;
		    }
		    return null;
		} catch (FileNotFoundException e) {
			log.info("文件不存在...");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			downedListFile = null;
		}
	}
	
	
	/**
	 * 已下载列表中加入新数据
	 * @param downedSong
	 * @return
	 */
	public int addDownFailSong(DownFailSong downFailSong) {
		File downFailListFile = new File(getBasePath()+DOWNLOAD_PATH+"downFail.list");
		if(!downFailListFile.exists()) {
			try {
				log.info("downFail.list文件不存在，正在创建....");
				downFailListFile.createNewFile();
				log.info("downFail.list文件创建[成功]....");
			} catch (IOException e) {
				log.info("downFail.list文件创建[失败--异常]....");
				e.printStackTrace();
				downFailSong = null;
				return -1;
			}
		}
		if(null != downFailSong) {
			String contents = downFailSong.getSongName()+SEPARATOR+downFailSong.getSinger()+SEPARATOR+
			downFailSong.getFormat()+SEPARATOR+downFailSong.getFailTime()+"\n";
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(downFailListFile,true));
				writer.write(contents);
				writer.flush();
				writer.close();
				List<DownFailSong> lists = getDownFailSongAll();
				return lists.size();
			} catch (IOException e) {
				log.info(downFailListFile.getName()+"文件信息写入[失败---异常]");
				e.printStackTrace();
				return -1;
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			} finally {
				downFailSong = null;
				contents = null;
			}
		}
		return -1;
	}
	
	
	/**
	 * 删除下载失败的歌曲列表
	 * @param no
	 * @return
	 */
	public boolean delDownFailSong(int no) {
		List<DownFailSong> lists = getDownFailSongAll();
		if(null != lists && lists.size()>0 && lists.size()>=no && no>0) {
			DownFailSong downFailSong = lists.get(no-1);
			log.info("删除下载失败的歌曲《"+downFailSong.getSongName()+"》");
			lists.remove(downFailSong);
			
			StringBuffer strBuff = new StringBuffer();
			if(null != lists && lists.size()>0) {
				for(DownFailSong fs : lists) {
					String contents = fs.getSongName()+SEPARATOR+fs.getSinger()+SEPARATOR+
					fs.getFormat()+SEPARATOR+fs.getFailTime()+"\n";
					strBuff.append(contents);
				}
			} else {
				strBuff.append("");
			}
			File downFailListFile = new File(getBasePath()+DOWNLOAD_PATH+"downFail.list");
			if(!downFailListFile.exists()) {
				try {
					log.info("downFail.list文件不存在，正在创建....");
					downFailListFile.createNewFile();
					log.info("downFail.list文件创建[成功]....");
				} catch (IOException e) {
					log.info("downFail.list文件创建[失败--异常]....");
					e.printStackTrace();
					return false;
				} finally {
					lists = null;
				}
			}
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(downFailListFile,false));
				writer.write(strBuff.toString());
				writer.flush();
				writer.close();
				log.info("删除下载失败的歌曲《"+downFailSong.getSongName()+"》--成功---");
				return true;
			} catch (IOException e) {
				log.info(downFailListFile.getName()+"文件信息写入[失败---异常]");
				e.printStackTrace();
				return false;
			} finally {
				lists = null;
				downFailListFile = null;
				downFailSong = null;
			}
		}
		return false;
	}
}
