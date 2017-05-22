package com.baiting.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.baiting.http.HttpRequest;
import com.baiting.http.HttpResponse;

public class Test04 {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		HttpRequest request = new HttpRequest();
		Map<String,String> params = new HashMap<String, String>();
		String gm = "";
		String gs = "";
		String name = "太想爱你";
		String artist="张信哲";
		
		try {
			gm = URLEncoder.encode(name,"GBK");
		    gs = URLEncoder.encode(artist, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		params.put("gm_down",gm);
		params.put("id_gc", "211860");
		//HttpResponse response3 = request.sendPost("http://www.5ilrc.com/downlrc.asp",params);
		HttpResponse response3 = null;
		try {
			response3 = request.sendPost("http://localhost/action.asp",params);
			System.out.println(response3.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
