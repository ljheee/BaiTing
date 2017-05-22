package com.baiting.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLEncoderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String result = "Summer Time"+" "+"吉克隽逸,Snoop Dogg";
		try {
		result = URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String parseUrl = "http://music.baidu.com/search?key="+result;
		System.out.println(parseUrl);
	}

}
