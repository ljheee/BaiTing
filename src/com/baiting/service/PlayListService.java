package com.baiting.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baiting.bean.PlayList;
import com.baiting.util.StringUtil;

public class PlayListService extends MusicService {

	private List<PlayList> playLists;
	
	/**
	 * 添加播放列表
	 * @param listName
	 * @return
	 */
	public boolean addPlayList(PlayList playList) {
		if(playList == null) {
			return false;
		}
		File file = new File(getListFilePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		String content =  playList.getFileName()+ "##" + playList.getPlayListName() + "\n";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			writer.append(content);
			writer.flush();
			writer.close();
			writer = null;
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			file = null;
		}
	}
	
	/**
	 * 批量添加播放列表
	 * @param playList
	 * @return
	 */
	public boolean batchAddPlayList(List<PlayList> playList) {
		File file = new File(getListFilePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		String content = "";
		for (int i = 0; i < playList.size(); i++) {
			PlayList list = playList.get(i);
			content += list.getFileName()+"##"+list.getPlayListName()+"\n";
		}
		try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(content);
				writer.flush();
				writer.close();
				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
	}
	
	
	/**
	 * 获取所有播放列表
	 * @return
	 */
	public List<PlayList> getPlayLists() {
		File file = new File(getListFilePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			playLists = new ArrayList<PlayList>();
			while((line=reader.readLine()) != null) {
				if(StringUtil.isEmpty(line)) {
					continue;
				}
				String content = line.replace("\n", "").trim();
				String[] cols = content.split(SEPARATOR);
				if(cols.length>=2) {
					PlayList list = new PlayList();
					list.setFileName(cols[0]);
					list.setPlayListName(cols[1]);
					playLists.add(list);
				}
			}
			reader.close();
			return playLists;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	
	/**
	 * 通过名称查询播放列表信息
	 * @param name
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PlayList getPlayListByName(String name,int type) {
		List<PlayList> playListsTmp = getPlayLists();
		if(playListsTmp != null && playListsTmp.size()>0) {
			for (Iterator iterator = playListsTmp.iterator(); iterator.hasNext();) {
				PlayList playList = (PlayList) iterator.next();
				if(type == PlayList.PLAYLIST_FILE) {
					if(playList.getFileName().equals(name)) {
						return playList;
					}
				} else if(type == PlayList.PLAYLIST_NAME) {
					if(playList.getPlayListName().equals(name)) {
						return playList;
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 删除播放列表
	 * @param name
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean deletePlayList(String name,int type) {
		List<PlayList> playListsTmp = getPlayLists();
		PlayList playListTemp = null;
		if(playListsTmp != null && playListsTmp.size()>0) {
			for (Iterator iterator = playListsTmp.iterator(); iterator.hasNext();) {
				PlayList playList = (PlayList) iterator.next();
				if(type == PlayList.PLAYLIST_FILE) {
					if(playList.getFileName().equals(name)) {
						playListTemp = playList;
						break;
					}
				} else if(type == PlayList.PLAYLIST_NAME) {
					if(playList.getPlayListName().equals(name)) {
						playListTemp = playList;
						break;
					}
				}
			}
		}
		if(playListTemp != null) {
			playListsTmp.remove(playListTemp);
			String path = getSongListPath()+getSeparator()+playListTemp.getFileName()+SONG_LIST_FILE_EXT;
			deleteFile(path);
			if(batchAddPlayList(playListsTmp)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 重命名播放列表
	 * @param name
	 * @param rename
	 * @return
	 */
	public boolean renamePlayList(String name,String rename) {
		List<PlayList> playListsTmp = getPlayLists();
		if(playListsTmp != null && playListsTmp.size()>0) {
			for (int i = 0; i < playListsTmp.size(); i++) {
				if(playListsTmp.get(i).getPlayListName().equals(name)) {
					playListsTmp.get(i).setPlayListName(rename);
					break;
				}	
			}
			batchAddPlayList(playListsTmp);
			return true;
		}
		return false;
	}
	
	private void deleteFile(String path) {
		File file = new File(path);
		log.info("删除文件["+path+"]----");
		if(file.exists()) {
			if(file.delete()) {
				log.info("["+path+"]文件删除--[成功]--");
			} else {
				log.info("["+path+"]文件删除--[失败]--");
			}
		}
	}
	
}
