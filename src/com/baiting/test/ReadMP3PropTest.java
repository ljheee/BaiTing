package com.baiting.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.baiting.util.StringUtil;

public class ReadMP3PropTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String songPath = "E:\\Music\\范逸臣-放生.mp3";
		File songFile = new File(songPath);
		if(songFile.exists()) {
			String fileName = songFile.getName();
			int startPostion = fileName.lastIndexOf(".");
			if(startPostion>0) {
				String suffix = fileName.substring(startPostion+1, fileName.length()).trim();
				if(StringUtil.isExistIgnoreCase(suffix, new String[]{"mp3"})) {
					/*try {
						MP3File mp3File = new MP3File(songFile);
						AbstractID3v2 id3v2 = mp3File.getID3v2Tag();
						String title = "";
						String artist = "";
						CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
						detector.add(JChardetFacade.getInstance());
						detector.add(ASCIIDetector.getInstance());
						detector.add(UnicodeDetector.getInstance());
						Charset charset = null;
						charset = detector.detectCodepage(songFile.toURL());
						detector = null;
						System.out.println(charset.name());
						if(id3v2 == null) {
							ID3v1 id3v1tag = mp3File.getID3v1Tag();
							if(id3v1tag != null) {
								title = id3v1tag.getTitle();
								artist = id3v1tag.getArtist();
							} else {
								FilenameTag tag = mp3File.getFilenameTag();
								String info = tag.toString();
								String[] cols = info.split("\r\n");
								if(cols.length<2) {
									 cols = cols[0].replace("null:", "").trim().split("-");
								}
								artist = cols[0].replace("null:", "").trim();
								title = cols[1].replace("null:", "").trim();
							}
							id3v1tag = null;
						} else {
						   title = id3v2.getSongTitle();
						   artist = id3v2.getLeadArtist();
						}
						id3v2 = null;
						if(!"UTF-8".equalsIgnoreCase(charset.name())){
							//if(charset.name().toUpperCase().startsWith("UTF")){
							    //byte[] bs = artist.getBytes("ISO-8859-1");
							  //  artist = new String(bs, "UTF-16LE");
								artist = StringUtil.transcoding(artist, "ISO-8859-1", "UTF-16LE");
								title = StringUtil.transcoding(title, "UTF-16", "UTF-8");
							    // bs = title.getBytes("ISO-8859-1");
							    // title = new String(bs, "UTF-16LE");
							//} else {
								//title = StringUtil.ISO88591_TO_GBK(title);
								//artist = StringUtil.ISO88591_TO_GBK(artist);
							//}
						}
						System.out.println("标题:"+title+"\n艺术家："+artist);
					} catch (TagException e) {
						e.printStackTrace();
					}catch (IOException e) {
						e.printStackTrace();
					} finally {
						songFile = null;
					}
			*/
			   try {
				AudioFileFormat aff = AudioSystem.getAudioFileFormat(songFile);
				
				//String type = aff.getType().toString();
				//System.out.println(type);
				if(null != aff.getType() &&  "MP3".equalsIgnoreCase(aff.getType().toString())) {
					Map<String,Object> maps = aff.properties();
					String mp3Title = (String) maps.get("title");
					String author = (String) maps.get("author");
					System.out.println("歌名："+mp3Title);
					System.out.println("歌手："+author);
				}
			
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			   
			}
		}
	}
	}

}
