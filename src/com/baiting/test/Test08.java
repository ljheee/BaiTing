package com.baiting.test;

public class Test08 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "哈哈(呵呵)";
		int p = str.indexOf("(");
		if(p>0) {
			str = str.substring(0,p); 
		}
		System.out.println(str);
	}

}
