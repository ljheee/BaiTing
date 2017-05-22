package com.baiting.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class UrlFileUtil extends Util {

	/**
	 * 获取URL文件大小
	 * @param strUrl
	 * @return
	 */
	public static long getUrlFileSize(String strUrl) {
		URL url = null;
		long size = 0;
		try {
			url = new URL(strUrl);
			URLConnection urlCon = url.openConnection();
			size = urlCon.getContentLength();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}
	
	/**
	 * 计算MP3播放时长
	 * CBR:播放时间 = 文件大小 ÷ 比特率 ×8
	 * VBR:文件播放总时间 ＝ 总的帧数 ×每一帧的采样个数÷比特率  
	 * @param fileSize
	 * @param props
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static long getPlayTimeLength(long fileSize,Map props) {
		long headerPos = Long.parseLong(props.get("mp3.header.pos").toString().trim());
		long bitRate = Long.parseLong(props.get("mp3.bitrate.nominal.bps").toString().trim());
		boolean isVBR = Boolean.valueOf(props.get("mp3.vbr").toString().trim());
		long time = 0l;
		if(!isVBR) {
			double dtime = (fileSize-headerPos)/(double)bitRate;
			dtime = dtime*8;
			time = (long)Math.round(dtime*1000*1000);
		}
		return time;
	}
	
}
