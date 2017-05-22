package com.baiting.service.lyric;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baiting.bean.Lyric;
import com.baiting.bean.LyricStatement;
import com.baiting.service.MusicService;
import com.baiting.util.StringUtil;

/**
 * 解析LRC文件
 * 
 * @author lmq
 * 
 */
public class LyricParseService extends MusicService {

	private Lyric lrc = new Lyric();
	private long currentTime = 0;
	private String currentContent = null;
	private Map<Long, String> maps = new HashMap<Long, String>();

	@SuppressWarnings("deprecation")
	public Lyric readLrcFile(String path, boolean isUrl) {
		InputStream in = null;
		File file = null;
		if (isUrl) {
			try {
				URI uri = new URI(path);
				file = new File(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			file = new File(path);
		}
		if(!file.exists()) {
			log.info(file.getAbsolutePath() + "----文件没有找不到-----");
			return null;
		}
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.info(file.getAbsolutePath() + "----找不到-----");
			e.printStackTrace();
			in = null;
			file = null;
			return null;
		} catch (Exception e) {
			log.info(file.getAbsolutePath() + "----找不到-----");
			e.printStackTrace();
			in = null;
			file = null;
			return null;
		}

		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		try {
			charset = detector.detectCodepage(file.toURL());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info(charset.name());
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in,charset.name()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = null;
			file = null;
			detector = null;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			in = null;
			file = null;
			detector= null;
			reader= null;
			return null;
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
			//lrc.setInfos(mapSortByKey(maps));
			List<LyricStatement> lrcStatementList = new ArrayList<LyricStatement>();
			if(null != lrc) {
				if(!StringUtil.isEmpty(lrc.getTitle())) {
					maps.put(0l, lrc.getTitle());
				}
				if(!StringUtil.isEmpty(lrc.getSinger())) {
					maps.put(1l, "演唱："+lrc.getSinger());
				}
				if(!StringUtil.isEmpty(lrc.getAlbum())) {
					maps.put(2l, "专辑："+lrc.getAlbum());
				}
			}
			if(maps.size()>0) {
				maps = mapSortByKey(maps);
				int index = 0;
				for (Map.Entry<Long, String> entry : maps.entrySet()) {
					index++;
					LyricStatement lrcStatement = new LyricStatement();
					lrcStatement.setIndex(index);
					lrcStatement.setTime(entry.getKey());
					lrcStatement.setStatement(entry.getValue());
					lrcStatementList.add(lrcStatement);
					lrcStatement = null;
				}
				lrc.setLrcInfos(lrcStatementList);
			}
			return lrc;
		} catch (IOException e) {
			e.printStackTrace();
			log.info(file.getAbsolutePath() + "-----文件读取异常-----");
			return null;
		} finally {
			try {
				in.close();
				reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			in = null;
			file = null;
			detector= null;
			reader= null;
		}
	}

	/**
	 * 利用正则表达式解析每行具体语句
	 * 
	 * @param str
	 */
	private void parseLine(String str) {
		currentContent = "";
		// 取得歌曲名信息
		if (str.trim().startsWith("[ti:")) {
			String title = str.replaceAll("\\[ti:", "").replaceAll("\\]", "").trim();
			lrc.setTitle(title.trim());
		} else if (str.trim().startsWith("[ar:")) {
			String singer = str.trim().replaceAll("\\[ar:", "").replaceAll("\\]", "").trim();
			lrc.setSinger(singer.trim());
		} else if (str.trim().startsWith("[al:")) {
			String album = str.trim().replaceAll("\\[al:", "").replaceAll("\\]", "").trim();
			lrc.setAlbum(album.trim());
		} else {
			// 设置正则规则
			String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				// 得到匹配的所有内容
				//String info = matcher.group();
				// 得到这个匹配项开始的索引
				//int start = matcher.start();
				// 得到这个匹配项结束的索引
				//int end = matcher.end();
				int groupCount = matcher.groupCount();
				for (int i = 0; i <= groupCount; i++) {
					String timeStr = matcher.group(i);
					if (i == 1) {
						// 将第二组中的内容设置为当前的一个时间点
						currentTime = strToLong(timeStr);
					}
				}// end for
					// 得到时间点后的内容
				String[] content = pattern.split(str);
				// 输出数组内容
				for (int i = 0; i < content.length; i++) {
					if (i == content.length - 1) {
						// 将内容设置为当前内容
						currentContent = content[i];
					}
				}
				// 设置时间点和内容的映射
				if(!"".equals(currentContent.trim())) {
				  maps.put(currentTime, currentContent);
				}
			}
		}
	}

	/**
	 * 将解析得到的表示时间的字符转化为Long型
	 * @param timeStr
	 * @return
	 */
	private long strToLong(String timeStr) {
		// 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
		// 1:使用：分割 2：使用.分割
		String[] s = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min * 60 * 1000*1000 + sec * 1000*1000 + mill * 1000;
	}
	
	
	/**
	 * 排序
	 * @param unsort_map
	 * @return
	 */
	private SortedMap<Long, String> mapSortByKey(Map<Long, String> unsort_map) {
		TreeMap<Long, String> result = new TreeMap<Long, String>();
		/*Object[] unsort_key = unsort_map.keySet().toArray();
	    Arrays.sort(unsort_key);
	    for (int i = 0; i < unsort_key.length; i++) {
	       result.put((Long) unsort_key[i], unsort_map.get(unsort_key[i]).toString());
	    }*/
		Set<Map.Entry<Long, String>> sets = unsort_map.entrySet();
		for (Iterator<Map.Entry<Long, String>> iterator = sets.iterator(); iterator.hasNext();) {
			Map.Entry<Long, String> entry = iterator.next();
			result.put(entry.getKey(), entry.getValue());
		}
	   //return result.tailMap(result.firstKey());
		return result;
	}

}
