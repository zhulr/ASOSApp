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

	// 注册接口
	public static final String REGISTER = "/Login/register.php";

	// 新闻快速浏览接口
	public static final String NEWSINTRO = "/News/newsintro.php";

	// 获取版本信息
	public static final String GETVERSION = "/General/getVersion";

	//BOSS卡购买信息录入接口
	public static final String BOSSCARD = "/Card/addbosscard.php";

	//司机卡购买信息录入接口
	public static final String DRIVERCARD = "/Card/adddrivercard.php";

	//查询是否购卡接口
	public static final String SEARCHCARD = "/Card/searchcard.php";

	//查询是否购司机卡接口
	public static final String SEARCHDRIVER = "/Card/searchdriver.php";

	public static final String SEARCHCOUPON ="/Card/searchcoupon.php";

	//留言接口
	public static final String LM = "/LM/lm.php";

	// 医学新闻接口
	public static final String NEWSMEDICAL = "/News/newsmedicallist.php";

	// 医学新闻详情接口
	public static final String NEWSMEDICALHTML = "/News/newsmedicalhtml.php";

	// 法律新闻接口
	public static final String NEWSLAW = "/News/newslawlist.php";

	// 法律新闻详情接口
	public static final String NEWSLAWHTML = "/News/newslawhtml.php";

	// 保险新闻接口
	public static final String NEWSINSURANCE = "/News/newsinsurancelist.php";

	// 保险新闻详情接口
	public static final String NEWSINSURANCEHTML = "/News/newsinsurancehtml.php";

	// 赔偿新闻接口
	public static final String NEWSCOMPENSATION = "/News/newscompensationlist.php";

	// 赔偿新闻详情接口
	public static final String NEWSCOMPENSATIONHTML = "/News/newscompensationhtml.php";

	// 其他新闻接口
	public static final String NEWSREST = "/News/newsrestlist.php";

	// 其他新闻详情接口
	public static final String NEWSRESTHTML = "/News/newsresthtml.php";


}
