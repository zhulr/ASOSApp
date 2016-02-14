package com.asosapp.phone.utils;

import android.os.Environment;

public class Const {

	// 错误信息常量
	public static final String CONN_ERR = "网络连接异常，请稍后再试！";

	// 数据库名称
	public static final String DATABASE_NAME = "asosapp.db";

	// SD卡路径
	public static final String SD_CARD_URL = Environment
			.getExternalStorageDirectory().getPath() + "/ASOS";

	// 服务器的IP地址
	public static final String SERVICE_URL = "http://223.68.152.158:65500/Home";

	// sharePreference的文件名
	public static final String SHARE_PREFENCE_NAME = "程序名" + "SP";

	// 获取验证码
	public static final String GET_IDCODE = "/Login/getIdCode";

	// 登录接口
	public static final String LOGIN = "/Login/login.php";

	// 登录接口
	public static final String REGISTER = "/Login/register.php";

	// 新闻接口
	public static final String NEWS = "/News/newslist.php";

	// 新闻快速浏览接口
	public static final String NEWSINTRO = "/News/newsintro.php";

	// 新闻详情接口
	public static final String NEWSHTML = "/News/newshtml.php";

	// 获取版本信息
	public static final String GETVERSION = "/General/getVersion";

	//暖心卡购买预约信息录入接口
	public static final String HEARTCARD = "/Card/addcard.php";

	//司机卡购买预约信息录入接口
	public static final String DRIVERCARD = "/Card/adddriver.php";

	//查询是否购暖心卡接口
	public static final String SEARCHCARD = "/Card/searchcard.php";

	//查询是否购司机卡接口
	public static final String SEARCHDRIVER = "/Card/searchdriver.php";

	//留言接口
	public static final String LM = "/LM/lm.php";

}
