package com.baiting.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Test15 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		URL url = null;
		try {
			url = new URL("http://www.mtvzz.com/fso/50/lrc_4294e6cd_247156.html");
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			InputStream in = urlConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line = reader.readLine();
			while(line != null) {
				System.out.println(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//OutputStream out = new FileOutputStream(downSongFile,true);
		
	}

}
