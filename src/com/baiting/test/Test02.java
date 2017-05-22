package com.baiting.test;

import java.util.Iterator;
import java.util.List;

import com.baiting.bean.Lyric;
import com.baiting.bean.LyricStatement;
import com.baiting.service.lyric.LyricParseService;

public class Test02 {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		LyricParseService lrcParse = new LyricParseService();
		String path = System.getProperty("user.dir");
		Lyric lrc = lrcParse.readLrcFile(path+"/temp/lyric/S.H.E-半糖主义.lrc", false);
		if(lrc != null) {
			System.out.println(lrc.getTitle());
			System.out.println(lrc.getSinger());
			System.out.println(lrc.getAlbum());
			List<LyricStatement> list = lrc.getLrcInfos();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				LyricStatement lyricStatement = (LyricStatement) iterator.next();
				System.out.println(lyricStatement.getIndex()+"\t"+lyricStatement.getTime()+"\t"+lyricStatement.getStatement());
				
			}
			/*for (Entry<Long, String> entry:maps.entrySet()) {
				
				System.out.println(entry.getKey()+"===>"+entry.getValue());
			}*/
		}
	}
	
}
