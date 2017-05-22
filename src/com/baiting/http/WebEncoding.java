package com.baiting.http;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebEncoding {

	private static CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();


	/**
	 * @param strurl  页面url地址,需要以 http://开始，例：http://www.pujia.com
	 * @return
	 * @throws IOException
	 */
	public String getCharset(String strurl) throws IOException {
		// 定义URL对象
	  URL url = new URL(strurl);
		// 获取http连接对象
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.connect();
		// 网页编码
		String strencoding = null;

		/**
		 * 首先根据header信息，判断页面编码
		 */
		// map存放的是header信息(url页面的头信息)
		Map<String, List<String>> map = urlConnection.getHeaderFields();
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();

		// 遍历,查找字符编码
		String key = null;
		String tmp = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			tmp = map.get(key).toString().toLowerCase();
			// 获取content-type charset
			if (key != null && key.equals("Content-Type")) {
				int m = tmp.indexOf("charset=");
				if (m != -1) {
					strencoding = tmp.substring(m + 8).replace("]", "");
					return strencoding;
				}
			}
		}

		/**
		 * 通过解析meta得到网页编码
		 */
		// 获取网页源码(英文字符和数字不会乱码，所以可以得到正确<meta/>区域)
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((line = in.readLine()) != null) {
			  sb.append(line);
			}
			in.close();
		} catch (Exception e) { // Report any errors that arise
			System.err.println(e);
			System.err.println("Usage:   java   HttpClient   <URL>   [<filename>]");
		}
		String htmlcode = sb.toString();
		sb =  null;
		// 解析html源码，取出<meta />区域，并取出charset
		String strbegin = "<meta";
		String strend = ">";
		String strtmp = "";
		int begin = htmlcode.indexOf(strbegin);
		int end = -1;
		int inttmp;
		while (begin > -1) {
			end = htmlcode.substring(begin).indexOf(strend);
			if (begin > -1 && end > -1) {
				strtmp = htmlcode.substring(begin, begin + end).toLowerCase();
				inttmp = strtmp.indexOf("charset");
				if (inttmp > -1) {
					strencoding = strtmp.substring(inttmp + 7, end)
							.replace("=", "").replace("/", "")
							.replace("\"", "").replace("\'", "")
							.replace(" ", "");
					return strencoding;
				}
			}
			htmlcode = htmlcode.replace(strtmp, "").trim();
			htmlcode = htmlcode.substring(begin);
			begin = htmlcode.indexOf(strbegin);
		}

		/**
		 * 分析字节得到网页编码
		 */
		strencoding = getFileEncoding(url);

		// 设置默认网页字符编码
		if (strencoding == null) {
			strencoding = "GBK";
		}
		return strencoding;
	}

	/**
	 * 
	 * <br>
	 * 方法说明：通过网页内容识别网页编码
	 * 
	 * <br>
	 * 输入参数：strUrl 网页链接; timeout 超时设置
	 * 
	 * <br>
	 * 返回类型：网页编码
	 */
	public static String getFileEncoding(URL url) {

		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(url);
		} catch (Exception e) {
			System.out.println(e.getClass() + "分析" + "编码失败");
		}
		if (charset != null)
			return charset.name();
		return null;

	}

}
