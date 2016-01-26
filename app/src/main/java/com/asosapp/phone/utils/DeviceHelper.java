package com.asosapp.phone.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 获取设备信息的类
 *
 * @author zhulr
 *
 */
public class DeviceHelper {

    /**
     * 检查WIFI是否连接
     *
     * @param context
     * @return 如果连接了返回true，否则返回false
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null == wifiInfo) {
            return false;
        }
        return wifiInfo.isAvailable();
    }

    /**
     * 检查手机网络(4G/3G/2G)是否连接
     *
     * @param context
     * @return 如果连接了返回true，否则返回false
     */
    public static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null == mobileNetworkInfo) {
            return false;
        }
        return mobileNetworkInfo.isConnected();
    }

    /**
     * 判断SD卡是否存在
     *
     * @return true表示SD卡存在,false表示不存在
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取设备的电话号码
     *
     * @param context
     * @return 设备的电话号码
     */
    public String getTelNO(Context context) {
        String telNo = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        return telNo;
    }

    /**
     * 获取设备IMEI
     *
     * @param context
     * @return 设备IMEI号
     */
    public String getMchId(Context context) {
        String mchId = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return mchId;
    }

    /**
     * 获得设备的分辨率,返回值为宽度和高度拼成的point类型值
     *
     * @param activity
     * @return Point(width,height)
     */
    public Point getResolution(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Point currentDpi = new Point(dm.widthPixels, dm.heightPixels);
        return currentDpi;
    }
}

