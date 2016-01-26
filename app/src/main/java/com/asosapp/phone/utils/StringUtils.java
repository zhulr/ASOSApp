package com.asosapp.phone.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 *
	 * @param s
	 * @return true��ʾ�ַ���Ϊnull����"" false��ʾ�ǿ�
	 */
	public static boolean isEmpty(String s) {
		boolean isEmpty = false;
		if (null == s || "".equals(s.trim())) {
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * �ж����ַ����Ƿ���ͬ
	 *
	 * @param s
	 *            �ַ���1
	 * @param t
	 *            �ַ���2
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
	 * ���ַ�����ʽ������ת��Ϊint��
	 *
	 * @param msg
	 * @return ��ȷ�򷵻����֣����򷵻�-1
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
	 * �ж��ַ����Ƿ�Ϊ�Ϸ����ֻ���
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
	 * �ж��ַ����Ƿ�Ϊ�Ϸ��ĵ�������
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
	 * �޸ĵ��·ݺ�����С��10��ʱ��ǰ����
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
