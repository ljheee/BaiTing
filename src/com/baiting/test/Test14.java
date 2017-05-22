package com.baiting.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test14 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String line1 = "var data = [{\"t\":\"大海\", \"s\":\"张雨生\", \"i\":\"\", \"a\":\"华纳国语超极品音色系列\", \"l\":1, \"r\":0,\"rt\":\"1\"},{\"t\":\"水手\", \"s\":\"郑智化\", \"i\":\"\", \"a\":\"金曲精选\", \"l\":1, \"r\":0,\"rt\":\"0\"}";
		
		Pattern pattern = Pattern.compile("\\{\\s*(.*?)\\s*\\}");
		Matcher matcher = pattern.matcher(line1);
		while(matcher.find()) {
			System.out.println(matcher.group());
		}
	}

}
