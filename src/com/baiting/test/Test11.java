package com.baiting.test;

import java.io.IOException;
import java.util.Vector;

import com.baiting.http.HttpRequest;
import com.baiting.http.HttpResponse;
import com.baiting.util.StringUtil;

public class Test11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpRequest request = new HttpRequest();
		HttpResponse response = null;
		try {
			response = request.sendGet("http://mp3.baidu.com/d?song=%D5%FC%BE%C8&singer=%CB%EF%E9%AA&album=%D4%B5%B7%DD%B5%C4%CC%EC%BF%D5&appendix=&size=5242880&cat=0&attr=0");
			Vector<String> contents = response.getContentCollection();
			//System.out.println(response.getContent());
			for (int i = 0; i < contents.size(); i++) {
				String line1 = contents.get(i).trim();
				if(StringUtil.isContains(line1, "<a id=\"downlink\"")) {
					String ulrTmp = "http://"+response.getHost();
					String songUrl = line1.trim().replaceAll("<a id=\"downlink\" href=(.*)onclick=(.*)", "$1").replaceAll("\"", "");
					System.out.println(ulrTmp+songUrl.trim());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
