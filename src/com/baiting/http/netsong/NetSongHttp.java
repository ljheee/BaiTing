package com.baiting.http.netsong;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.baiting.Music;
import com.baiting.bean.NetSong;
import com.baiting.util.StringUtil;

public class NetSongHttp extends Music implements Runnable {

	protected HttpClient httpclient;
	protected String url;
	
	public NetSongHttp() {
		httpclient = new DefaultHttpClient();
	}
	
	@Override
	public void run() {
		
	}
	
	
	/**
	 * 获取html内容
	 * @param url
	 * @return
	 */
	protected String[] getHtmlContents(String url) {
		String[] contents = null;
		HttpGet httpget = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			httpget = new HttpGet(url);
			if(null == httpclient) {
				 httpclient = new DefaultHttpClient();
			}
			response = httpclient.execute(httpget);
			entity = response.getEntity();
			String content = null;
			if(response.getStatusLine().getStatusCode() == 200) {
				content = EntityUtils.toString(entity);
			}
			if(!StringUtil.isEmpty(content)) {
				String[] contentArray = content.split("\r\n");
				if(null != contentArray && contentArray.length>0) {
					contents = contentArray;
				}
				contentArray = null;
			}
			content = null;
		} catch (Exception e) {
			e.printStackTrace();
			contents = null;
		} finally {
			httpget = null;
			response = null;
			entity = null;
			httpclient.getConnectionManager().shutdown();
			httpclient = null;
		} 
		return contents;
	}
	
	
	protected String getNetSongUrl(String sid,int type) {
		String url = null;
		String typeName = null;
		if(type == NET_SONG_LIST_TOP100) {
			typeName = "%2Ftop%2Fdayhot";
		} else if(type == NET_SONG_LIST_HOT500) {
			typeName = "%2Ftop%2Fnew";
		} else if(type == NET_SONG_LIST_SEARCH) {
			typeName = "%2Fsearch";
		} else if(type == NET_SONG_LIST_OLD) {
			typeName = "%2Ftop%2Foldsong";
		} else if(type == NET_SONG_LIST_NET) {
			typeName = "%2Ftop%2Fnetsong";
		}
		String requestUrl = "http://music.baidu.com/song/"+sid+"/download?__o="+typeName;
		String[] contents = getHtmlContents(requestUrl); 
		if(null != contents && contents.length>0) {
			for (int i = 0; i < contents.length; i++) {
				String line = contents[i].trim().replaceAll("\t|\n|\r", "").trim();
				if(line.startsWith("<a data-btndata=")) {
					String link = line.replaceAll("(.*)href=\"(.*)song_id="+sid+"\"(.*)", "$2").trim();
					if(link.startsWith("/data/music/file")) {
						link = link.replace("/data/music/file?link=", "").trim();
						if(link.startsWith("http://")) {
							link = link+"song_id="+sid;
							url = link;
						}
					}
				}
			}
		}
		contents = null;
		return url;
	}
	
	
	protected NetSong parseJsonData(String jsonData) {
		NetSong netSong = null;
		if(!StringUtil.isEmpty(jsonData)) {
			try {
				JSONObject jsonObj = JSONObject.fromObject(jsonData);
				netSong = new NetSong();
				netSong.setSid(jsonObj.get("sid").toString());
				String sname = jsonObj.get("sname").toString().replaceAll("<em>|</em>", "").trim();
				sname = sname.replaceAll("&#039;", "'");
				sname = sname.replaceAll("&quot;", "\"");
				
				//去掉括号里面的内容
				String regex = "(.*)[\\(|（](.*)[\\)|）](.*)";
				sname = sname.replaceAll(regex, "$1$3");
				
				netSong.setSongName(sname);
				String author = jsonObj.get("author").toString().replaceAll("<em>|</em>", "").trim();
				author = author.replaceAll(regex, "$1$3");
				netSong.setSinger(author);
			} catch (Exception e) {
				e.printStackTrace();
				netSong = null;
			}
		}
		return netSong;
	}

}
