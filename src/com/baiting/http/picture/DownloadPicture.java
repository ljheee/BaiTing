package com.baiting.http.picture;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.baiting.bean.Song;
import com.baiting.http.Download;
import com.baiting.util.StringUtil;

/**
 * 图片下载
 * @author lmq
 *
 */
public class DownloadPicture extends Download {
	
	protected HttpClient httpclient;
	
	protected String url;
	
	public DownloadPicture() {
		super();
		httpclient = new DefaultHttpClient();
	}
	
	public DownloadPicture(Song song) {
		super(song);
		httpclient = new DefaultHttpClient();
	}
	
	public DownloadPicture(Song song,String url) {
		super(song, url);
		httpclient = new DefaultHttpClient();
	}

	public boolean startDownload() {
		
		return false;
	}
	
	@Override
	public void run() {
		startDownload();
	}

	public void setSong(Song song) {
		this.song = song;
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
	
	
	
	/**
	 * 
	 * @param url
	 * @param charsetName
	 * @return
	 */
	protected String getHtmlContent(String url,String charsetName) {
		String content = null;
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
			if(response.getStatusLine().getStatusCode() == 200) {
				content = EntityUtils.toString(entity,charsetName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpget = null;
			response = null;
			entity = null;
		}
		return content;
	}
}
