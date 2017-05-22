package com.baiting.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends Util {
	
	/**
	 * 判断字符串是否为空(null或"")
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if(null == value || "".equals(value.trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断对象的值是否为null
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj){
		return ((obj==null)?true:false);
	}
	
	/**
	 * 把null转化为其他值;如整型、浮点型、Double转换为0；String转为""
	 * @param obj
	 * @return
	 */
	public static Object nullToValue(Object obj) {
		if(obj != null) {
			return obj;
		} else {
			if(obj instanceof Integer || obj instanceof Double || obj instanceof Float) {
				return 0;
			} else if(obj instanceof String) {
				return "";
			} else {
				return obj;
			}
		}
	}
	
	/**
	 * 判断str字符串是否在字符数组array中
	 * @param str
	 * @param array
	 * @return
	 */
	public static boolean isExist(String str,String[] array) {
		if(array == null || array.length<1 || str == null) {
			return false;
		}
		for (int i = 0; i < array.length; i++) {
			if(str.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断str字符串是否在字符数组array中(不区分大小写)
	 * @param str
	 * @param array
	 * @return
	 */
	public static boolean isExistIgnoreCase(String str,String[] array) {
		if(array == null || array.length<1 || str == null) {
			return false;
		}
		for (int i = 0; i < array.length; i++) {
			if(str.equalsIgnoreCase(array[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ISO-8859-1转化为UTF-8
	 * @param value
	 * @return
	 */
	public static String ISO88591_TO_UTF8(String value) {
		if(isEmpty(value)) {
		  return null;
		} 
		try {
			return new String(value.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * ISO-8859-1编码转换为GBK
	 * @param value
	 * @return
	 */
	public static String ISO88591_TO_GBK(String value) {
		if(isEmpty(value)) {
		  return null;
		} 
		try {
			return new String(value.getBytes("ISO-8859-1"),"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return value;
		}
	}
	
	/**
	 * 转换编码
	 * @param value
	 * @param s_code
	 * @param d_code
	 * @return
	 */
	public static String transcoding(String value,String s_code,String d_code) {
		if(isEmpty(value)) {
			return value;
		}
		try {
			return new String(value.getBytes(s_code),d_code);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return value;
		}
	}
	
	/**
	 * 正则表达式匹配
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean match(String str,String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 判断是否包含在正则表达式中
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean isContains(String str,String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否包含在正则表达式中(忽略大小写)
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean isContainsIgnoreCase(String str,String regex) {
		str = str.toUpperCase();
		regex = regex.toUpperCase();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否包含在数组中
	 * @param str
	 * @param array
	 * @return
	 */
	public static boolean isContains(String str,Object[] array) {
		boolean flag = false;
		for (int i = 0; i < array.length; i++) {
			if(isContains(str, (String)array[i])) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 判断是否包含在数组中(忽略大小写)
	 * @param str
	 * @param array
	 * @return
	 */
	public static boolean isContainsIgnoreCase(String str,Object[] array) {
		boolean flag = false;
		str = str.toUpperCase();
		for (int i = 0; i < array.length; i++) {
			String tmp = ((String)array[i]).toUpperCase();
			if(isContains(str, tmp)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 通过正则表达式获取表达式里面的值
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String getValue(String str,String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}
	
	/**
	 * 按长度拆开字符串
	 * @param value
	 * @param len
	 * @return
	 */
	public static String[] splitByLen(String value,int len) {
		String[] tmp = new String[(int)Math.ceil((double)value.length()/len)];
		int count = 0;
		for (int i = 0; i < value.length(); i++) {
			if((i+1)%len==0) {
				tmp[count++] = value.substring((i+1)-len,i+1);
			} else if((i==value.length()-1) && (value.length()%len != 0)) {
				tmp[count] = value.substring(count*len);
			}
		}
		return tmp;
	}
	
	
	/**
	 * 判断字符串(value)的后缀是否包含在指定的字符数组(suffixs)中
	 * @param value
	 * @param suffixs
	 * @return
	 */
	public static boolean isSuffixContains(String value,String[] suffixs) {
		boolean is = false;
		if(!isEmpty(value) && null != suffixs && suffixs.length>0) {
			int startPostion = value.lastIndexOf(".");
			if(startPostion>0) {
				String suffix = value.substring(startPostion+1, value.length()).trim();
				if(StringUtil.isExistIgnoreCase(suffix, suffixs)) {
					is = true;
				} else {
					is = false;
				}
			}
		} else {
			log.info("参数传入有误-----");
		}
		return is;
	}

}
