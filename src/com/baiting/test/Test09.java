package com.baiting.test;

import com.baiting.http.netsong.BaiduSongList;

public class Test09 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//String value = "<div class=\"img\"><a href=\"\" title=\"播放：燃点&nbsp;胡夏\" onclick=\"return omb(['mp3order','新歌TOP100','',0])\"><img src=\"http://imgs.zhangmen.baidu.com/ssaimg/110/110973.jpg\" onerror=\"this.src='http://static.mp3.baidu.com/imgs/bXAzaW5kZXg-/v2_0/default_100x100_img.jpg'\" /></a></div><div class=\"music-name\"><a class=\"play bold\" href=\"\" onclick=\"return omb(['mp3order','新歌TOP100','',0])\" >燃点</a></div>";
		//value = value.replaceAll("(.*)title=\"(.*)\"(.*)onclick=(.*)<img (.*)", "$2");
		//System.out.println(value);
		
		/*String value2 = "<a target=\"_blank\" href=\"http://mp3.baidu.com/singerlist/李炜,杨洋.html\" title='李炜,杨洋'>李炜,杨...</a>";
		value2 = value2.replaceAll("<a (.*)title='(.*)'>(.*)</a>", "$2");
		System.out.println(value2);*/
		BaiduSongList netSongList = new BaiduSongList();
		netSongList.setType(0);
		netSongList.startGradSongList();
	}

}
