/*
 * 创建日期 2011-3-16
 *
 * 成都天和软件公司
 * 电话：028-85425861 
 * 传真：028-85425861-8008 
 * 邮编：610041 
 * 地址：成都市武侯区航空路6号丰德万瑞中心B座1001 
 * 版权所有
 */
package com.ruoyi.system.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @rem: 字符串工具
 * @author: luox
 * @version: 1.0
 * @since 2017-09-10
 */
public final class StringUtils {
	
	/**
	 * 空字符串
	 */
	public static final String EMPTY = "";

	private StringUtils() {
	}

	/**
	 * 首字母小写
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String firstCharLowerCase(String s) {
		if (isValid(s)) {
			return s.substring(0, 1).toLowerCase() + s.substring(1);
		}
		return s;
	}
	
	/**
	 * 删除前缀
	 * @param s
	 * @param prefix
	 * @return
	 */
	public static String removePrefix(String s, String prefix) {
		int index = s.indexOf(prefix);
		return index == 0 ? s.substring(prefix.length()) : s;
	}
	
	/**
	 * 删除后缀
	 * @param s
	 * @param suffix
	 * @return
	 */
	public static String removeSuffix(String s, String suffix) {
		return s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : s;
	}

	/**
	 * 首字母大写
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String firstCharUpperCase(String s) {
		if (isValid(s)) {
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		}
		return s;
	}

	/**
	 * 检查对象是否有效 obj != null && obj.toString().length() > 0
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isValid(Object obj) {
		return obj != null && obj.toString().length() > 0;
	}

	/**
	 * 是否是空的
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		return obj == null || obj.toString().length() == 0;
	}

	/**
	 * 转化为String对象
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static String asString(Object obj) {
		return obj != null ? obj.toString() : "";
	}

	/**
	 * 返回其中一个有效的对象 value != null && value.toString().length() > 0
	 * 
	 * @param values
	 */
	public static String tryThese(Object... values) {
		for (int i = 0; i < values.length; i++) {
			String value = StringUtils.asString(values[i]);
			if ( !value.isEmpty() ) {
				return value;
			}
		}
		return "";
	}
	
	/**
	 * EL表达式提供的定义方法
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static String tryThese(String v1 , String v2) {
		return tryThese(new Object[]{v1, v2});
	}

	/**
	 * 连接字符串
	 * 
	 * @param list
	 * @param split
	 * @return 字符串
	 */
	public static <T> String join(T[] list, String split) {
		return join(list, split, "");
	}
	
	/**
	 * 连接字符串
	 * 
	 * @param list
	 * @param split
	 * @return 字符串
	 */
	public static <T> String join(T[] list, String split, String wrap) {
		if (list == null)
			return null;
		StringBuilder s = new StringBuilder(128);
		for (int i = 0; i < list.length; i++) {
			if (i > 0) {
				s.append(split);
			}
			s.append(wrap + list[i] + wrap);
		}
		return s.toString();
	}
	
	/**
	 * 连接
	 * @param list
	 * @param split
	 * @param wrap
	 * @return
	 */
	public static <T> String join(List<T> list, String split, String wrap) {
		return join(list.toArray(), split, wrap);
	}

	/**
	 * 连接字符串
	 * 
	 * @param list
	 * @param split
	 * @return 字符串
	 */
	public static String join(List<?> list, String split) {
		return join(list.toArray(), split);
	}
	
	/**
	 * 包裹字符串 id:12, {, } 输出 {id:12}
	 * @param input 输入串
	 * @param begin {
	 * @param end }
	 * @return String
	 */
	public static String wrap(String begin, String input, String end) {
		if( !input.startsWith(begin) ){
			input = begin + input;
		}
		if( !input.endsWith(end) ){
			input = input + end;
		}
		return input;
	}

	/**
	 * 取得匹配的字符串
	 * 
	 * @param input
	 * @param regex
	 * @return
	 */
	public static List<String> matchs(String input, String regex) {
		return matchs(input, regex, 0);
	}

	/**
	 * 取得匹配的字符串
	 * 
	 * @param input
	 * @param regex
	 * @return
	 */
	public static List<String> matchs(String input, String regex, int group) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(input);
		List<String> matches = new ArrayList<String>();
		while (match.find()) {
			matches.add(match.group(group));
		}
		return matches;
	}
	
	/**
	 * 找到匹配的第一个字符串
	 * 
	 * @param input
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String matchFirst(String input, String regex, int group) {
		List<String> matches = matchs(input, regex, group);
		return matches. isEmpty() ? null : matches.get(0);
	}

	/**
	 * 截取指定长度字符串
	 * @return
	 */
	public static String getShorterString(String str, int maxLength) {
		return getShorterString(str, "...", maxLength);
	}

	/**
	 * 截取指定长度字符串
	 * 
	 * @param input
	 * @param tail
	 * @param length
	 * @return
	 */
	public static String getShorterString(String input, String tail, int length) {
		tail = isValid(tail) ? tail : "";
		StringBuffer buffer = new StringBuffer(512);
		try {
			int len = input.getBytes("GBK").length;
			if (len > length) {
				int ln = 0;
				for (int i = 0; ln < length; i++) {
					String temp = input.substring(i, i + 1);
					if (temp.getBytes("GBK").length == 2)
						ln += 2;
					else
						ln++;

					if (ln <= length)
						buffer.append(temp);
				}
			} else {
				return input;
			}
			buffer.append(tail);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 取得GBK编码
	 * 
	 * @return
	 */
	public static String getBytesString(String input, String code) {
		try {
			byte[] b = input.getBytes(code);
			return Arrays.toString(b);
		} catch (UnsupportedEncodingException e) {
			return String.valueOf(code.hashCode());
		}
	}

	/**
	 * 转换格式 CUST_INFO_ID - > custInfoId
	 * 
	 * @param input
	 * @return
	 */
	public static String getFieldString(String input) {
		if( input == null ){
			return null;
		}
		String field = input.toLowerCase();
		String[] values = field.split("\\_");
		StringBuffer b = new StringBuffer(input.length());
		for (int i = 0; i < values.length; i++) {
			if (i == 0)
				b.append(values[i]);
			else
				b.append(firstCharUpperCase(values[i]));
		}
		return b.toString();
	}
	
	/**
	 * 转换格式 CUST_INFO_ID - > custInfoId
	 * 
	 * @param columnName
	 * @return
	 */
	public static String toFieldName(String columnName) {
		return getFieldString(columnName);
	}
	
	/**
	 * 转换格式 custInfoId - > CUST_INFO_ID
	 * 
	 * @param field
	 * @return
	 */
	public static String toColumnName(String field) {
		if( field == null ){
			return null;
		}
		StringBuffer b = new StringBuffer(field.length() + 3);
		for (int i = 0; i < field.length(); i++) {
			Character char1 = field.charAt(i);
			if(Character.isUpperCase(char1) && i != 0){
				b.append("_");
			}
			b.append(char1);
		}
		return b.toString();
	}

	/**
	 * 转化为JSON值
	 * 
	 * @param value
	 * @return
	 * @throws IOException
	 */
	public static String toJsonValue(Object value) throws IOException {
		if (value instanceof Number) {
			return value.toString();
		} else {
			return "'" + value.toString() + "'";
		}
	}

	/**
	 * 字符串转化为UUID
	 * 
	 * @param value
	 * @return
	 */
	public static String toUUID(String value) {
		if (value == null)
			throw new RuntimeException("value is null!");
		return UUID.nameUUIDFromBytes(value.getBytes()).toString();
	}

	/**
	 * 获取Style样式中样式的值
	 * 
	 * @param styleString
	 * @param styleName
	 * @return 相应的值
	 */
	public static String getStyleValue(String styleString, String styleName) {
		String[] styles = styleString.split(";");
		for (int i = 0; i < styles.length; i++) {
			String tempValue = styles[i].trim();
			if (tempValue.startsWith(styleName)) {
				String[] style = tempValue.split(":");
				return style[1];
			}
		}
		return "";
	}
	
	/**
	 * 生成重复次字符
	 * @param charactor
	 * @param repeat
	 * @return
	 */
	public static String getRepeat(String charactor, int repeat){
		return repeat(charactor, repeat, "");
	}
	
	/**
	 * 生成重复次字符
	 * @param charactor
	 * @param repeat
	 * @return
	 */
	public static String repeat(String charactor, int repeat, String split){
		StringBuilder s = new StringBuilder(charactor.length()*repeat);
		for (int i = 0; i < repeat; i++) {
			if( i != 0 ){
				s.append( split != null ? split : "");
			}
			s.append(charactor);
		}
		return s.toString();
	}
	
	/**
	 * 取得长度
	 * @param text
	 * @return
	 */
	public static int length(String text){
		int len = text.length();
		try {
			len = text.getBytes("GBK").length;//SQLServer数据库用的GBK编码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return len;
	}

	/**
	 * 字符串替换函数
	 * @param data 字符串
	 * @param data from  旧值
	 * @param to from  新值
	 * */
	public static String replaceString(String data, String from, String to) {
		StringBuffer buf = new StringBuffer(data.length());
		int pos = -1;
		int i = 0;
		while ((pos = data.indexOf(from, i)) != -1) {
			buf.append(data.substring(i, pos)).append(to);
			i = pos + from.length();
		}
		buf.append(data.substring(i));
		return buf.toString();
	}


	public static void main(String[] args) {
		System.out.println(toUUID("1"));
		System.out.println(removePrefix("abcd123", "ab"));
		System.out.println(removeSuffix("abcd123", "123"));
		System.out.println(toColumnName("usernameid"));
		System.out.println(getFieldString(toColumnName("userNameId")));
		System.out.println(repeat("?", 10, ","));
		length("AAA中国（）111222bb");
	}
}