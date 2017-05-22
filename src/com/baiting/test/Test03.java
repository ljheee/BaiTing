package com.baiting.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baiting.http.HttpRequest;
import com.baiting.http.HttpResponse;

public class Test03 {

	private static String url = "http://www.mtvzz.com";
	
	/**
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] args) {
		HttpRequest request = new HttpRequest();
		Map<String,String> params = new HashMap<String, String>();
		String gm = "";
		String gs = "";
		String name = "让我欢喜让我忧";
		String artist="周华健";
		
		try {
			gm = URLEncoder.encode(name,"GBK");
		    gs = URLEncoder.encode(artist, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String urls = "http://www.mtvzz.com/s/?key="+name+"&mf=1&qf=1";
		HttpResponse response = null;
		try {
			response = request.sendGet(urls,params);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//HttpResponse response = request.sendPost("http://127.0.0.1/action.asp",params);
		Vector<String> content = response.getContentCollection();
		int count=0;
		String lrcUrl1 = "";
		
		for (Iterator iterator = content.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String rexName = "<font color='red'>"+name+"</font></a></div>";
			Pattern pattern = Pattern.compile(rexName);
			Matcher matcher = pattern.matcher(string);
			if(matcher.find()) {
				//
				System.out.println(string);
				Pattern pattern2 = Pattern.compile(artist);
				Matcher matcher2 = pattern2.matcher(content.get(count+1));
				if(matcher2.find()) {
					//System.out.println(content.get(count+1));
					String tmp = content.get(count+7);
					Pattern pattern3 = Pattern.compile("/fso/(.*)\\.lrc");
					Matcher matcher3 = pattern3.matcher(tmp);
					if(matcher3.find()) {
						lrcUrl1 = matcher3.group();
						System.out.println(lrcUrl1);
					}
					break;
				}
			}
			count++;
		}
		/*
		String idGc = "";
		String gmDown = gm;
		if(!"".equals(lrcUrl1.trim())) {
		    HttpResponse response2 = request.sendGet(url+"/"+lrcUrl1);
		    Vector<String> content2 = response2.getContentCollection();
		   // System.out.println(content2.toString());
		    int count2 = 0;
		    for (Iterator iterator = content2.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				Pattern pattern = Pattern.compile("images/button_download.gif");
				Matcher matcher = pattern.matcher(string);
				if(matcher.find()) {
					String tmp = content2.get(count2+2);
					System.out.println(tmp);
					Pattern pattern3 = Pattern.compile("(\\d+)");
					Matcher matcher3 = pattern3.matcher(tmp);
					if(matcher3.find()) {
						idGc = matcher3.group();
						System.out.println(idGc);
					}
				}
				count2++;
			}
		}*/
		
		/*if(!"".equals(lrcUrl1.trim())) {
			String urlTmp = url+lrcUrl1;
			try {
				File file = new File(new URI(urlTmp));
				if(file.exists()) {
				    BufferedReader reader = new BufferedReader(new FileReader(file));
				    String line = "";
					try {
						line = reader.readLine();
						while(line != null) {
					    	System.out.println(line);
					    	line = reader.readLine();
					    }
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				    
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			HttpResponse response3;
			try {
				response3 = request.sendGet(url+lrcUrl1);
				System.out.println(response3.getContent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(response.getContent());
		
		
	}

}
