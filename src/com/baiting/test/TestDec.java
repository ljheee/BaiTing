package com.baiting.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TestDec {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 String url = "郭静";
		 try {
			System.out.println(URLEncoder.encode(url, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
