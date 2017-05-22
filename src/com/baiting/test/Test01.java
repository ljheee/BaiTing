package com.baiting.test;

import com.baiting.util.CommonUtil;
import com.baiting.util.StringUtil;

public class Test01 {

	static {
		System.out.println("正在执行【static】。。。。。。");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String paths = System.getProperty("user.dir");
		//String path = paths+"/list/default.list";
		//SongListService songListService = new SongListService();
		//if(songListService.readSongDir("默认播放列表", paths+"/mp3")) {
		String value="f8eae7";
		String[] tmp = StringUtil.splitByLen(value, 2);
		for (int i = 0; i < tmp.length; i++) {
			System.out.println(tmp[i]);
		}
		//} else {
		//	System.out.println("读取失败~~");
		//}
		
		System.out.println(CommonUtil.getColor("#f8eae7").getGreen());

	}
	

}
