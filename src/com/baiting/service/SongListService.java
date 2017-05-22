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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.baiting.bean.NetSong;
import com.baiting.bean.PlayList;
import com.baiting.bean.Song;
import com.baiting.util.StringUtil;

public class SongListService extends MusicService {
	
	private List<Song> songList;
	
	private String[] removeStr = {"(","www.","（"};
	
	public SongListService() {
		
	}
	
	/**
	 * 添加歌曲到列表
	 * @param playListName
	 * @param songPath
	 * @return
	 */
	public boolean addSong(String playListName,String songPath) {
		if(StringUtil.isEmpty(playListName) || StringUtil.isEmpty(songPath)) {
			return false;
		}
		File file = new File(songPath);
		if(!file.exists()) {
			return false;
		}
		File songFile = new File(songPath);
		String fileName = songFile.getName();
		int startPostion = fileName.lastIndexOf(".");
		String contents = "";
		if(startPostion>0) {
			String suffix = fileName.substring(startPostion+1, fileName.length()).trim();
			if(StringUtil.isExistIgnoreCase(suffix, getSupportPlayFormatter())) {
				String title = "";
				String artist = "";
				boolean isAudioClass = false;
				if("MP3".equalsIgnoreCase(suffix)) {
					try {
						AudioFileFormat aff = AudioSystem.getAudioFileFormat(songFile);
						if(null != aff.getType() &&  "MP3".equalsIgnoreCase(aff.getType().toString())) {
							Map<String,Object> maps = aff.properties();
							Object mp3Title = (String) maps.get("title");
							Object mp3Author = (String) maps.get("author");
							if(null != mp3Title && !StringUtil.isEmpty(mp3Title.toString().trim())
									&& null != mp3Author && !StringUtil.isEmpty(mp3Author.toString().trim())) {
								title = mp3Title.toString();
								artist = mp3Author.toString();
								isAudioClass = true;
							}
							maps = null;
							mp3Title = null;
							mp3Author = null;
							aff = null;
						}
					
					} catch (UnsupportedAudioFileException e) {
						//e.printStackTrace();
					} catch (IOException e) {
						//e.printStackTrace();
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
				Song handerSong = new Song();
				handerSong.setName(title.trim());
				handerSong.setSinger(artist.trim());
				handerSong.setPath(songFile.getAbsolutePath().trim());
				
				if(!isAudioClass) {
					String fileNameNotExt = fileName.substring(0,startPostion);
					handerSong = handerSongInfo(handerSong,fileNameNotExt);
				}
				String content = playListName+SEPARATOR+handerSong.getName()+SEPARATOR+handerSong.getSinger()+SEPARATOR+songFile.getAbsolutePath()+SEPARATOR+"";
				contents +=content+"\n";
				handerSong = null;
				songFile = null;
			}
		}
		if(addContentToFile(playListName, contents)) {
			contents = null;
			return true;
		}
		contents = null;
		return false;
	}
	
	/**
	 * 
	 * @param song
	 * @param fileName（没有后缀的文件名）
	 * @return
	 */
	private Song handerSongInfo(Song song,String fileName) {
		Song songTmp = new Song();
		String title = song.getName();
		String artist = song.getSinger();
		
		for (int i = 0; i < removeStr.length; i++) {
			int p = title.indexOf(removeStr[i]);
			if(p>0) {
				title = title.substring(0,p); 
			}
		}
		for (int i = 0; i < removeStr.length; i++) {
			int p = artist.indexOf(removeStr[i]);
			if(p>0) {
				artist = artist.substring(0,p); 
			}
		}
		
		String[] cols = parseFileName(fileName);
		String tmp = "";
        if(cols[1].equals(artist) && cols[0].equals(title)) {
			tmp = cols[0];
			cols[0] = cols[1];
			cols[1] = tmp;
		}
		if(cols[0].equals(artist) && cols[1].equals(title)) {
			song.setName(title);
			song.setSinger(artist);
			return song;
		}
		if(!artist.equals(cols[0]) && !"".equals(cols[0])) {
			songTmp.setSinger(cols[0]);
		} else {
			songTmp.setSinger(artist);
		}
		
		log.info(title);
		String regex = "([A-Z|a-z|\\s]){1,}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(title);
		if(!title.equals(cols[1]) && !matcher.matches()) {
			songTmp.setName(cols[1]);
		} else {
			songTmp.setName(title);
		}
		return songTmp;
	}
	
	private String[] parseFileName(String fileName) {
		String[] str = new String[2];
		String[] reg = {"\\[(.*)\\](.*)-(.*)","(.*)-(.*)"};
		int index = -1;
		for (int i = 0; i < reg.length; i++) {
			Pattern pattern = Pattern.compile(reg[i]);
			Matcher matcher = pattern.matcher(fileName);
			if(matcher.matches()) {
				index = i;
				break;
			}
		}
		if(index == 0) {
			String[] tmp = new String[3];
			String[] tmp2 = fileName.split("-");
			tmp[1] = tmp2[1].trim();
			String[] tmp3 = tmp2[0].split("]");
			tmp[0] = tmp3[1].trim();
			tmp[2] = tmp3[0].replace("[", "").trim();
			if(tmp[2].equals(tmp[1])) {
				str[1] = tmp[0];
			} else {
				str[1] = tmp[1];
			}
			str[0] = tmp[2];
		} else if(index == 1) {
			String[] tmp = fileName.split("-");
			str[0] = tmp[0].trim();
			str[1] = tmp[1].trim();
		} else {
			str[0] = "";
			str[1] = fileName;
		}
		
		for (int i = 0; i < removeStr.length; i++) {
			int p = str[0].indexOf(removeStr[i]);
			if(p>0) {
				str[0] = str[0].substring(0,p); 
			}
		}
		for (int i = 0; i < removeStr.length; i++) {
			int p = str[1].indexOf(removeStr[i]);
			if(p>0) {
				str[1] = str[1].substring(0,p); 
			}
		}
		return str;
	}
	
	/**
	 * 清空歌曲列表
	 * @param playListName
	 * @return
	 */
	public boolean clearSongList(String playListName) {
		if(StringUtil.isEmpty(playListName)) {
			return false;
		}
		String contents = "";
		return addContentsToFile(playListName,contents);
	}
	
	/**
	 * 批量添加歌曲到播放列表
	 * @param playListName
	 * @param songPaths
	 * @return
	 */
	public boolean betchAddSong(String playListName,List<String> songPaths) {
    	if(StringUtil.isEmpty(playListName) || null == songPaths || songPaths.size()<1) {
			return false;
		}
		StringBuffer contents = null;
		for (String songPath : songPaths) {
			if(null == songPath) {
				continue;
			}
			File songFile = new File(songPath);
			if(songFile.exists()) {
				String fileName = songFile.getName();
				int startPostion = fileName.lastIndexOf(".");
				if(startPostion>0) {
					String suffix = fileName.substring(startPostion+1, fileName.length()).trim();
					if(StringUtil.isExistIgnoreCase(suffix, getSupportPlayFormatter())) {
						String title = "";
						String artist = "";
						boolean isAudioClass = false;
						if("MP3".equalsIgnoreCase(suffix)) {
							try {
								AudioFileFormat aff = AudioSystem.getAudioFileFormat(songFile);
								if(null != aff.getType() &&  "MP3".equalsIgnoreCase(aff.getType().toString())) {
									Map<String,Object> maps = aff.properties();
									Object mp3Title = (String) maps.get("title");
									Object mp3Author = (String) maps.get("author");
									if(null != mp3Title && !StringUtil.isEmpty(mp3Title.toString().trim())
											&& null != mp3Author && !StringUtil.isEmpty(mp3Author.toString().trim())) {
										title = mp3Title.toString();
										artist = mp3Author.toString();
										isAudioClass = true;
									}
									maps = null;
									mp3Title = null;
									mp3Author = null;
									aff = null;
								}
							
							} catch (UnsupportedAudioFileException e) {
								//e.printStackTrace();
							} catch (IOException e) {
								//e.printStackTrace();
							} catch (Exception e) {
								//e.printStackTrace();
							}
						}
						Song handerSong = new Song();
						handerSong.setName(title.trim());
						handerSong.setSinger(artist.trim());
						handerSong.setPath(songFile.getAbsolutePath().trim());
							
						if(!isAudioClass) {
							String fileNameNotExt = fileName.substring(0,startPostion);
							handerSong = handerSongInfo(handerSong,fileNameNotExt);
						}
						String content = playListName+SEPARATOR+handerSong.getName()+SEPARATOR+handerSong.getSinger()+SEPARATOR+songFile.getAbsolutePath()+SEPARATOR+"";
						if(null == contents) {
							contents = new StringBuffer();
						}
						contents.append(content+"\n");
					}
				}
			} //end exists
		}
		if(null != contents) {
			if(addContentToFile(playListName, contents.toString())) {
				return true;
			}
		}
		return false;
	}
    
    /**
     * 从歌曲所在目录读取歌曲添加到播放列表
     * @param playListName
     * @param songDir
     * @return
     */
	public boolean readSongDir(String playListName,String songDir) {
    	if(StringUtil.isEmpty(playListName) || StringUtil.isEmpty(songDir)) {
			return false;
		}
    	File file = new File(songDir);
    	if(!file.isDirectory()) {
    		return false;
    	}
    	String[] fileList = file.list();
    	List<String> filePaths = new ArrayList<String>();
    	for (int i = 0; i < fileList.length; i++) {
			File songFile = new File(songDir+getSeparator()+fileList[i]);
			if(!songFile.isDirectory()) {
				String filePath = songFile.getAbsolutePath();
				filePaths.add(filePath);
			} //end isDirectory
		} //end for
    	fileList = null;
    	file = null;
    	if(filePaths.size()>0) {
    		return betchAddSong(playListName, filePaths);
    	}
    	return false;
    }
	
	
    /**
     * 追加到文件末尾
     * @param playListName
     * @param contents
     * @return
     */
	private boolean addContentToFile(String playListName,String contents) {
		String playListFileName = "";
		if(getConfigMap().get("play.list.default.name").toString().equals(playListName)) {
			playListFileName=DEFAULT_PLAYLIST_FILE_NAME;
		} else {
			PlayListService playListService = new PlayListService();
			PlayList playList = playListService.getPlayListByName(playListName, PlayList.PLAYLIST_NAME);
			if(playList != null) {
				playListFileName = playList.getFileName();
			}
			playListService = null;
			playList = null;
		}
		if(!StringUtil.isEmpty(playListFileName)) {
			String path = getSongListPath()+getSeparator()+playListFileName+SONG_LIST_FILE_EXT;
			File slFile = new File(path);
			try {
				if(!slFile.exists()) {
					slFile.createNewFile();
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(slFile,true));
				writer.write(contents);
				writer.flush();
				writer.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				slFile = null;
			}
		}
		return false;
	}
	
	/**
	 * 重新写入文件(覆盖已有的内容)
	 * @param playListName
	 * @param contents
	 * @return
	 */
	private boolean addContentsToFile(String playListName,String contents) {
		String playListFileName = "";
		if(getConfigMap().get("play.list.default.name").toString().equals(playListName)) {
			playListFileName=DEFAULT_PLAYLIST_FILE_NAME;
		} else {
			PlayListService playListService = new PlayListService();
			PlayList playList = playListService.getPlayListByName(playListName, PlayList.PLAYLIST_NAME);
			if(playList != null) {
				playListFileName = playList.getFileName();
			}
			playListService = null;
			playList = null;
		}
		if(!StringUtil.isEmpty(playListFileName)) {
			String path = getSongListPath()+getSeparator()+playListFileName+SONG_LIST_FILE_EXT;
			File slFile = new File(path);
			try {
				if(!slFile.exists()) {
					slFile.createNewFile();
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(slFile));
				writer.write(contents);
				writer.flush();
				writer.close();
				writer = null;
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				slFile = null;
			}
		}
		return false;
	}
	
	
	/**
	 * 读取指定播放列表对应的歌曲列表
	 * @param playListName
	 * @return
	 */
	public List<Song> getSongList(String playListName) {
		String playListFileName = "";
		if(StringUtil.isEmpty(playListName)) {
			playListName = getConfigMap().get("play.list.default.name").toString();
		}
		if(getConfigMap().get("play.list.default.name").toString().equals(playListName)) {
			playListFileName=DEFAULT_PLAYLIST_FILE_NAME;
		} else {
			PlayListService playListService = new PlayListService();
			PlayList playList = playListService.getPlayListByName(playListName, PlayList.PLAYLIST_NAME);
			if(playList != null) {
				playListFileName = playList.getFileName();
			}
			playListService = null;
			playList = null;
		}
		if(!StringUtil.isEmpty(playListFileName)) {
			String path = getSongListPath()+getSeparator()+playListFileName+SONG_LIST_FILE_EXT;
			File file = new File(path);
			if(!file.exists()) {
				return null;
			} 
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "";
				songList = new ArrayList<Song>();
				int count = 1;
				while((line = reader.readLine()) != null) {
					if(StringUtil.isEmpty(line)) {
						continue;
					}
					String content = line.replace("\n", "").trim();
					String[] cols = content.split(SEPARATOR);
					if(cols.length>3) {
						Song song = new Song();
						song.setNo(count);
						song.setPlayListName(cols[0]);
						song.setName(cols[1]);
						song.setSinger(cols[2]);
						song.setPath(cols[3]);
						if(cols.length==5) {
							song.setUrl(cols[4].trim());
						} else {
							song.setUrl("");
						}
						songList.add(song);
						count++;
					}
					cols = null;
				}//end while
				if(songList.size()>0) {
					return songList;
				}
				
				reader.close();
				reader = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				file = null;
			}
		}
		return null;
	}
	
	
	/**
	 * 通过序号获取歌曲信息
	 * @param no
	 * @return
	 */
	public Song getSong(int no) {
		if(songList != null && songList.size()>(no-1)) {
			return songList.get(no-1);
		}
		return null;
	}
	
	/**
	 * 通过序号获取歌曲信息
	 * @param no
	 * @return
	 */
	public Song getSong(String playListName, int no) {
		songList = getSongList(playListName);
		if(songList != null && songList.size()>(no-1) && no>0) {
			return songList.get(no-1);
		} else {
			return null;
		}
	}
	
	public boolean delSongList(Song song) {
		songList = getSongList(song.getPlayListName());
		if(songList != null) {
			songList.remove(song.getNo()-1);
			String contents = "";
			for (int i = 0; i < songList.size(); i++) {
				String content = songList.get(i).getPlayListName()+SEPARATOR+songList.get(i).getName()+SEPARATOR+songList.get(i).getSinger()+SEPARATOR+songList.get(i).getPath()+SEPARATOR+songList.get(i).getUrl();
				contents +=content+"\n";
			}
			if(addContentsToFile(song.getPlayListName(), contents)) {
	    			return true;
	    		} else {
	    			return false;
	    		}
	    	}
		return false;
	}
	
	public boolean delSongList(List<Song> songs) {
		String playListName = songs.get(0).getPlayListName();
		songList = getSongList(playListName);
		
		if(songList != null) {
			for (int i = 0; i < songs.size(); i++) {
				songList.remove(songs.get(i).getNo()-(i+1));
			}
			String contents = "";
			for (int i = 0; i < songList.size(); i++) {
				String content = songList.get(i).getPlayListName()+SEPARATOR+songList.get(i).getName()+SEPARATOR+songList.get(i).getSinger()+SEPARATOR+songList.get(i).getPath()+SEPARATOR+songList.get(i).getUrl();
				contents +=content+"\n";
			}
			if(addContentsToFile(playListName, contents)) {
	    			return true;
	    		} else {
	    			return false;
	    		}
	    	}
		return false;
	}
	
	/*
	 * 添加网络歌曲到播放列表
	 */
	public int addNetSong(NetSong netSong,String playListName) {
		String content = playListName+SEPARATOR+netSong.getSongName()+SEPARATOR+netSong.getSinger()+SEPARATOR+netSong.getLocalPath()+SEPARATOR+netSong.getUrl();
		String playListFileName = "";
		if(StringUtil.isEmpty(playListName)) {
			playListName = getConfigMap().get("play.list.default.name").toString();
		}
		if(getConfigMap().get("play.list.default.name").toString().equals(playListName)) {
			playListFileName=DEFAULT_PLAYLIST_FILE_NAME;
		} else {
			PlayListService playListService = new PlayListService();
			PlayList playList = playListService.getPlayListByName(playListName, PlayList.PLAYLIST_NAME);
			if(playList != null) {
				playListFileName = playList.getFileName();
			}
		}
		addContentToFile(playListName,content+"\n");
		String path = getSongListPath()+getSeparator()+playListFileName+SONG_LIST_FILE_EXT;
		File file = new File(path); 
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			int count = 0;
			while((reader.readLine()) != null) {
				count++;
			}
			reader.close();
			return count;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/*
	 * 添加歌曲到播放列表
	 */
	public int addSong(Song songTmp) {
		String content = songTmp.getPlayListName()+SEPARATOR+songTmp.getName()+SEPARATOR+songTmp.getSinger()+SEPARATOR+songTmp.getPath()+SEPARATOR+songTmp.getUrl();
		String playListFileName = "";
		if(StringUtil.isEmpty(playListName)) {
			playListName = getConfigMap().get("play.list.default.name").toString();
		}
		if(getConfigMap().get("play.list.default.name").toString().equals(playListName)) {
			playListFileName=DEFAULT_PLAYLIST_FILE_NAME;
		} else {
			PlayListService playListService = new PlayListService();
			PlayList playList = playListService.getPlayListByName(playListName, PlayList.PLAYLIST_NAME);
			if(playList != null) {
				playListFileName = playList.getFileName();
			}
		}
		addContentToFile(playListName,content+"\n");
		String path = getSongListPath()+getSeparator()+playListFileName+SONG_LIST_FILE_EXT;
		File file = new File(path); 
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			int count = 0;
			while((reader.readLine()) != null) {
				count++;
			}
			reader.close();
			return count;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	/**
	 * 判断歌曲是否已经在播放列表
	 * @param songTmp
	 * @return
	 */
	public int existSongList(Song songTmp) {
		if(null == songTmp){
			return -1;
		} 
		songList = getSongList(songTmp.getPlayListName());
		if(songList != null && songList.size()>0) {
			for (int i = 0; i < songList.size(); i++) {
				Song tmp = songList.get(i);
				if(songTmp.getName().equals(tmp.getName()) && songTmp.getSinger().equals(tmp.getSinger()) && 
						songTmp.getPath().equals(tmp.getPath())) {
					return i+1;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 判断歌曲是否已经在播放列表
	 * @param songTmp
	 * @return
	 */
    public int existSongList(NetSong netSong,String palyListName) {
    	if(null == netSong){
			return -1;
		} 
		songList = getSongList(palyListName);
		if(songList != null && songList.size()>0) {
			for (int i = 0; i < songList.size(); i++) {
				Song tmp = songList.get(i);
				if(netSong.getSongName().equals(tmp.getName()) && netSong.getSinger().equals(tmp.getSinger()) && 
						netSong.getUrl().equals(tmp.getUrl())) {
					return i+1;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 更新播放路径
	 * @param songTmp
	 * @return
	 */
	public boolean updateSongPath(Song songTmp) {
		songList = getSongList(songTmp.getPlayListName());
		if(songList != null) {
			songList.get(songTmp.getNo()-1).setPath(songTmp.getPath());
			String contents = "";
			for (int i = 0; i < songList.size(); i++) {
				String content = songList.get(i).getPlayListName()+SEPARATOR+songList.get(i).getName()+SEPARATOR+songList.get(i).getSinger()+SEPARATOR+songList.get(i).getPath()+SEPARATOR+songList.get(i).getUrl();
				contents +=content+"\n";
			}
			if(addContentsToFile(songTmp.getPlayListName(), contents)) {
	    			return true;
	    		} else {
	    			return false;
	    		}
	    	}
		return false;
	}
	
	
	/**
	 * 重命名歌曲名称
	 * @param songTmp
	 * @param newName
	 * @return
	 */
	public boolean renameSongName(Song songTmp,String newName) {
		log.info("重命名歌曲名称---["+songTmp.getName()+"]--->["+newName+"]------");
		boolean isSuccess = false;
		songList = getSongList(songTmp.getPlayListName());
		if(songList != null) {
			String[] values = newName.split("-");
			if(values.length>1) {
				songList.get(songTmp.getNo()-1).setName(values[0].trim());
				songList.get(songTmp.getNo()-1).setSinger(values[1].trim());
			} else {
				songList.get(songTmp.getNo()-1).setName(newName.trim());
			}
			
			String songPath = songTmp.getPath();
			if(!StringUtil.isEmpty(songPath)) {
				File file = new File(songPath);
				if(file.exists()) {
					String name = file.getName();
					String[] re = name.split("\\.");
					String distPath = file.getParent()+getSeparator()+songTmp.getSinger()+"-"+newName+"."+re[re.length-1];
					File distFile = new File(distPath);
					log.info("重命名歌曲对应的文件名称---["+songTmp.getPath()+"]--->["+distPath+"]------");
					if(file.renameTo(distFile)) {
						songList.get(songTmp.getNo()-1).setPath(distPath);
						log.info("重命名歌曲对应的文件名称---[成功]------");
					} else {
						log.info("重命名歌曲对应的文件名称---[失败]------");
					}
					distFile = null;
				} else {
					log.info("["+songPath+"]文件不存在-----");
				}
				file = null;
				
			}
			String contents = "";
			for (int i = 0; i < songList.size(); i++) {
				String content = songList.get(i).getPlayListName()+SEPARATOR+songList.get(i).getName()+SEPARATOR+songList.get(i).getSinger()+SEPARATOR+songList.get(i).getPath()+SEPARATOR+songList.get(i).getUrl();
				contents +=content+"\n";
			}
			if(addContentsToFile(songTmp.getPlayListName(), contents)) {
				isSuccess = true;
	    	} else {
	    		isSuccess = false;
	    	}
	    }
		return isSuccess;
	}
	
	
	/**
	 * 歌名和歌手互换
	 * @param songTmp
	 * @param newName
	 * @return
	 */
	public boolean nameSingerExchange(Song songTmp) {
		log.info("歌名和歌手互换---["+songTmp.getName()+"-"+songTmp.getSinger()+"]--->["+songTmp.getSinger()+"-"+songTmp.getName()+"]------");
		boolean isSuccess = false;
		songList = getSongList(songTmp.getPlayListName());
		if(songList != null) {
			songList.get(songTmp.getNo()-1).setName(songTmp.getSinger());
			songList.get(songTmp.getNo()-1).setSinger(songTmp.getName());
			String songPath = songTmp.getPath();
			if(!StringUtil.isEmpty(songPath)) {
				File file = new File(songPath);
				if(file.exists()) {
					String name = file.getName();
					String[] re = name.split("\\.");
					String distPath = file.getParent()+getSeparator()+songTmp.getName()+"-"+songTmp.getSinger()+"."+re[re.length-1];
					File distFile = new File(distPath);
					log.info("重命名歌曲对应的文件名称---["+songTmp.getPath()+"]--->["+distPath+"]------");
					if(file.renameTo(distFile)) {
						songList.get(songTmp.getNo()-1).setPath(distPath);
						log.info("重命名歌曲对应的文件名称---[成功]------");
					} else {
						log.info("重命名歌曲对应的文件名称---[失败]------");
					}
					distFile = null;
				} else {
					log.info("["+songPath+"]文件不存在-----");
				}
				file = null;
				
			}
			String contents = "";
			for (int i = 0; i < songList.size(); i++) {
				String content = songList.get(i).getPlayListName()+SEPARATOR+songList.get(i).getName()+SEPARATOR+songList.get(i).getSinger()+SEPARATOR+songList.get(i).getPath()+SEPARATOR+songList.get(i).getUrl();
				contents +=content+"\n";
			}
			if(addContentsToFile(songTmp.getPlayListName(), contents)) {
				isSuccess = true;
	    	} else {
	    		isSuccess = false;
	    	}
	    }
		return isSuccess;
	}
	
}
