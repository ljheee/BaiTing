package com.baiting.http.picture;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.baiting.bean.BackgroundPicture;
import com.baiting.bean.Song;
import com.baiting.util.JsonUtil;
import com.baiting.util.StringUtil;

/**
 * 图片下载(资源来自百度)
 * 
 * @author lmq
 * 
 */
public class DownloadPictureSogou extends DownloadPicture {
	
	public DownloadPictureSogou() {
		super();
	}
	
	public DownloadPictureSogou(Song song) {
		super(song);
	}

	public DownloadPictureSogou(Song song, String url) {
		super(song, url);
	}

	@Override
	public boolean startDownload() {
		boolean is = false;
		//String name = song.getName();
		String artist = song.getSinger();
		log.info("正在下载【" + artist + "】的图片...");
		String key = artist;
		String encodeKey = "";

		try {
			encodeKey = URLEncoder.encode(key, "gbk");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		url = "http://pic.sogou.com/";
		//String urlSearch = url
		//		+ "pics?query="+encodeKey+"&mood=0&picformat=0&mode=1&di=2&w=05009900&dr=1&_asf=pic.sogou.com&_ast=1383038190&start=48&reqType=ajax&tn=0";
		String urlSearch = url +"pics?query="+encodeKey+"&w=05009900&p=40030500&_asf=pic.sogou.com&_ast=1383230869&sut=1643&sst0=1383230869402";
		String rexName = "imgTempData={\"isForbiden\":";
		log.info("搜索图片:"+urlSearch);
		String[] lines = null;
		try {
			String jsonData = null;
			lines = getHtmlContents(urlSearch);
			for (int i = 0; i < lines.length; i++) {
				if(lines[i].trim().startsWith(rexName)) {
					jsonData = lines[i].trim().replaceAll("imgTempData=|;", "");
					//System.out.println(jsonData);
			      break;
				}
			}
			if(!StringUtil.isEmpty(jsonData)) {
				String[] itemKeys = new String[]{"pic_url"};
				List<Map<String,String>> lists = JsonUtil.parseJsonToList(jsonData, "items", itemKeys);
				if(null != lists && lists.size()>0) {
					BackgroundPicture bkPic = null;
					for(Map<String,String> map : lists) {
						bkPic = new BackgroundPicture();
						bkPic.setLocal(false);
						bkPic.setPath(map.get(itemKeys[0]));
						if(StringUtil.isSuffixContains(bkPic.getPath(), getSupportPictureFormatter())) {
							addBkPicList(bkPic);
						}
					}
					bkPic = null;
				}
				itemKeys = null;
				lists  = null;
			}
			jsonData = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lines = null;
		}
		return is;
	}

}
