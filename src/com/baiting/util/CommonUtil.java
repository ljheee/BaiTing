package com.baiting.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil extends Util {

	public static void drawString(Graphics g, String s, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		int asc = fm.getAscent();
		g.drawString(s, x, y + asc);
	}

	/**
	 * 获取颜色
	 * 
	 * @param value
	 * @return
	 */
	public static Color getColor(String value) {
		Map<String, Integer> hexMap = new HashMap<String, Integer>();
		hexMap.put("0", 0);
		hexMap.put("1", 1);
		hexMap.put("2", 2);
		hexMap.put("3", 3);
		hexMap.put("4", 4);
		hexMap.put("5", 5);
		hexMap.put("6", 6);
		hexMap.put("7", 7);
		hexMap.put("8", 8);
		hexMap.put("9", 9);
		hexMap.put("A", 10);
		hexMap.put("B", 11);
		hexMap.put("C", 12);
		hexMap.put("D", 13);
		hexMap.put("E", 14);
		hexMap.put("F", 15);
		if (!StringUtil.isEmpty(value) && value.startsWith("#")) {
			Integer[] gbr = new Integer[3];
			String valueTmp = value.replaceAll("#", "");
			String[] valueArray = StringUtil.splitByLen(valueTmp, 2);
			for (int i = 0; i < valueArray.length; i++) {
				String[] valueArray2 = StringUtil.splitByLen(valueArray[i], 1);
				int value1 = hexMap.get(valueArray2[0].toUpperCase());
				int value2 = hexMap.get(valueArray2[1].toUpperCase());
				gbr[i] = value1 * 16 + value2;
			}
			return new Color(gbr[0], gbr[1], gbr[2]);
		}
		return null;
	}

	/**
	 * 把毫秒转换为几小时几分钟几秒
	 * @param milliseconds
	 * @return
	 */
	public static String millisecondsToHHMMSS(long milliseconds) {
		long second = (long)Math.round((milliseconds/1000));
		int hh = (int)(second/(60*60));
		int mm = (int)(second/60);
		int ss = (int)second%60;
		String hhStr = (hh<10?("0"+hh):hh)+"";
		String mmStr = (mm<10?("0"+mm):mm)+"";
		String ssStr = (ss<10?("0"+ss):ss)+"";
		return hhStr+":"+mmStr+":"+ssStr;
	}
	
	/**
	 * 把毫秒转换为几小时几分钟几秒
	 * @param milliseconds
	 * @return
	 */
	public static String microsecondsToHHMMSS(long microseconds) {
		long milliseconds = (long)Math.round((microseconds/(1000)));
		return millisecondsToHHMMSS(milliseconds);
	}

}
