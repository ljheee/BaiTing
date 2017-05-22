package com.baiting.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test07 {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String name = "让我欢喜让我忧";
		String content = "1123Super star good is g";
		String rex_name = "([A-Z|a-z|\\s]){1,}";
		Pattern pattern = Pattern.compile(rex_name);
		Matcher matcher = pattern.matcher(content);
//		while(matcher.find()) {
//			System.out.println(matcher.group());
//		}
		System.out.println(matcher.matches());
	}

}
