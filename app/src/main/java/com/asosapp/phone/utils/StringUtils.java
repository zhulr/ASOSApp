package com.asosapp.phone.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * 判断字符串是否为空
	 *
	 * @param s
	 * @return true表示字符串为null或者"" false表示非空
	 */
	public static boolean isEmpty(String s) {
		boolean isEmpty = false;
		if (null == s || "".equals(s.trim())) {
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * 判断两字符串是否相同
	 *
	 * @param s
	 *            字符串1
	 * @param t
	 *            字符串2
	 * @return true/false
	 */
	public static boolean isSame(String s, String t) {
		boolean isSame = false;
		if (s.equals(t) && !isEmpty(s) && !isEmpty(t)) {
			isSame = true;
		}
		return isSame;
	}

	/**
	 * 将字符串形式的数字转换为int型
	 *
	 * @param msg
	 * @return 正确则返回数字，否则返回-1
	 */
	public static int getNum(String msg) {
		Pattern p = Pattern.compile("[0-9\\.]+");
		Matcher m = p.matcher(msg);
		if (m.find()) {
			return Integer.parseInt(m.group());
		} else {
			return -1;
		}
	}

	/**
	 * 判断字符串是否为合法的手机号
	 *
	 * @param mobiles
	 * @return true/false
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^\\d{11}$");
		// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断字符串是否为合法的电子邮箱
	 *
	 * @param email
	 * @return true/false
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}


	/**
	 * 修改当月份和日期小于10的时候前补零
	 * @param ss
	 * @return
	 */
	public static String formatString(int ss) {
		if (ss < 10) {
			return "0" + ss;
		}
		return ss + "";
	}

}
