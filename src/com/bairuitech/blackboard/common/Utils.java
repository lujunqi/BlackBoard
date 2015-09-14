package com.bairuitech.blackboard.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.widget.TextView;

/**
 * 通用代码
 * 
 * @author HP
 * 
 */
public class Utils {
	public final static int SIZE = 1024 * 70; 
	
	public static byte[] gzip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			gzip.close();
			
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	public static byte[] unGzip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return (obj + "").trim();
		}
	}

	public static void setTextView(Map<String, Object> m, TextView v, String key) {
		v.setText(toString(m.get(key)));
	}
	public static void setTextViewEx(Map<String, Object> m, TextView v, String key,String format) {
		String s = toString(m.get(key));
		v.setText(String.format(format, s));
	}

	public static void setTextView(Map<String, Object> m, TextView v,
			String key, String... en) {
		try {
			int i = Integer.parseInt(toString(m.get(key)));
			v.setText(en[i]);
		} catch (Exception e) {
			v.setText("");
		}
	}
	public static void setTextViewEx(Map<String, Object> m, TextView v,
			String key,String format, String... en) {
		try {
			int i = Integer.parseInt(toString(m.get(key)));
			String s = en[i];
			v.setText(String.format(format, s));
		} catch (Exception e) {
			v.setText("");
		}
	}
	
	

	// null 为true 否则false
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else if ("".equals(obj)) {
			return true;
		} else {
			return false;
		}
	}

	// 全部null false 否则 false
	public static boolean isNulls(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			if (isNull(objs[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证电话号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();

	}

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
			return flag;
		} catch (Exception e) {
		}
		return false;
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
