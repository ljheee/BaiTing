package com.baiting.http.lyric;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baiting.bean.Song;
import com.baiting.http.HttpResponse;
import com.baiting.util.StringUtil;

public class DownloadLyricBaidu extends DownloadLyric implements Runnable{
	
	public DownloadLyricBaidu() {
		super();
	}
	
	public DownloadLyricBaidu(Song songTmp) {
		super(songTmp);
	}
	
	public DownloadLyricBaidu(Song songTmp,String url) {
		super(songTmp,url);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean startDownload() {
		song.setLrcState(2);
		song.setLrcState(2);
		String name = song.getName();
		String artist = song.getSinger();
		log.info("正在下载【"+name+"】的歌词...");
		String lryKey = name+" "+artist;
		String encodeLryKey = "";
	
		try {
			encodeLryKey = URLEncoder.encode(lryKey, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		url = "http://music.baidu.com";
		String urlSearch = url+"/search/lrc?key="+encodeLryKey;
		//String rexName = "<font color='red'>"+name+"</font></a></div>";
		String rexName = "class=\"down-lrc-btn(.*)";
		log.info(urlSearch);
		Vector<String> content = null;
		try {
			HttpResponse response = request.sendGet(urlSearch);
			content = response.getContentCollection();
			response = null;
			String lrcUrl = "";
			for (Iterator iterator = content.iterator(); iterator.hasNext();) {
					String string = (String) iterator.next();
					Pattern pattern = Pattern.compile(rexName);
					Matcher matcher = pattern.matcher(string);
					if(matcher.find()) {
						log.info(string);
						lrcUrl = string.replaceAll("(.*)\\'href\\':\\'(.*)\\' \\}(.*)", "$2");
						log.info("[歌词URL]："+lrcUrl);
						break;
					}
			}//end for
			if(!"".equals(lrcUrl.trim())) {
					StringBuffer strBuff = new StringBuffer();
					CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
					detector.add(JChardetFacade.getInstance());
					detector.add(ASCIIDetector.getInstance());
					detector.add(UnicodeDetector.getInstance());
					Charset charset = null;
					URL urlTmp = new URL(url+lrcUrl);
					charset = detector.detectCodepage(urlTmp);
					detector = null;
					String charsetName = charset.name();
					charset = null;
					log.info("《"+name+"》【下载歌词】:"+url+lrcUrl);
					HttpResponse response2 = request.sendGet(url+lrcUrl,charsetName);
					Vector<String> contentVector = response2.getContentCollection();
					response2 = null;
					//log.info(response2.getContent());
					for (Iterator iterator = contentVector.iterator(); iterator.hasNext();) {
						String string = (String) iterator.next();
						strBuff.append(string+"\r\n");
					}
					contentVector = null;
					if(!StringUtil.isEmpty(strBuff.toString())) {
						String lrcPath = getLrcDir()+"/"+artist+"-"+name+LRC_EXT;
						File lrcFile = new File(lrcPath);
						if(!lrcFile.exists()) {
							log.info("正在创建["+lrcPath+"]文件......");
							lrcFile.createNewFile();
						}
						BufferedWriter writer = new BufferedWriter(new FileWriter(lrcFile));
						writer.write(strBuff.toString());
						writer.close();
						writer = null;
						strBuff = null;
						song.setLrcState(1);
						song.setLrcState(1);
						log.info("【"+name+"】的歌词下载完成...");
						return true;
					}
					
				}
				song.setLrcState(-1);
				song.setLrcState(-1);
				log.info("【"+name+"】的歌词下载失败....");
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				song.setLrcState(-1);
				song.setLrcState(-1);
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				song.setLrcState(-1);
				song.setLrcState(-1);
				return false;
			} finally {
				content = null;
			}
	}
	
	public void setSongTmp(Song song) {
		this.song = song;
	}
	
}
