package com.baiting.test;

import com.baiting.util.StringUtil;

public class Test12 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String value="<a  href=\"http://ting.baidu.com/player/index.html?__methodName=mboxCtrl.playSong&__argsValue=2091717&fr=mp3\" target=\"_blank\" onclick=\"ting_media_addSong('2091717');return false;\" ><em>拯救</em></a>";
		//String value="<td class=\"third\"><span><a href=\"http://ting.baidu.com/artist/1152?fr=mp3\" target=\"_blank\">孙楠</a>&nbsp;</span></td>";
		System.out.println(StringUtil.isContains(value, "<a href=(.*)>(.*)</a>"));
//		
		String data = value.replaceAll("(.*)<a(.*)href=\"(.*)\"\\s*>(.*)</a>(.*)", "$4");
		data = data.replaceAll("(<em>|</em>)", "").trim();
		System.out.println(data);
		
		//BaiduSearchSongList test = new BaiduSearchSongList("折子戏","黄阅");
		//test.startGradSongList();
	}

}
