package com.asosapp.phone.initprogram;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.asosapp.phone.receiver.NotificationClickEventReceiver;
import com.asosapp.phone.utils.SharePreferenceManager;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by ASOS_zhulr on 2015/12/24.
 */
public class MyApplication extends Application{
    //请求队列
    public static RequestQueue queue;
    private static final String JCHAT_CONFIGS = "JChat_configs";
    public static final String PICTURE_DIR = "sdcard/JChatDemo/pictures/";
    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.init(getApplicationContext());
        SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        new NotificationClickEventReceiver(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getHttpQueues(){
        return queue;
    }
}
