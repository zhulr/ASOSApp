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
    public static final int REQUEST_CODE_TAKE_PHOTO = 4;
    public static final int REQUEST_CODE_SELECT_PICTURE = 6;
    public static final int RESULT_CODE_SELECT_PICTURE = 8;
    public static final int REQUEST_CODE_SELECT_ALBUM = 10;
    public static final int RESULT_CODE_SELECT_ALBUM = 11;
    public static final int REQUEST_CODE_BROWSER_PICTURE = 12;
    public static final int RESULT_CODE_BROWSER_PICTURE = 13;
    public static final int REQUEST_CODE_CHAT_DETAIL = 14;
    public static final int RESULT_CODE_CHAT_DETAIL = 15;
    public static final int REQUEST_CODE_FRIEND_INFO = 16;
    public static final int RESULT_CODE_FRIEND_INFO = 17;
    public static final int REQUEST_CODE_CROP_PICTURE = 18;
    public static final int REQUEST_CODE_ME_INFO = 19;
    public static final int RESULT_CODE_ME_INFO = 20;
    public static final int REFRESH_GROUP_NAME = 3000;
    public static final int REFRESH_GROUP_NUM = 3001;
    public static final int ON_GROUP_EVENT = 3004;
    public static final int PAGE_MESSAGE_COUNT = 18;

    private static final String JCHAT_CONFIGS = "JChat_configs";

    public static final String TARGET_ID = "targetID";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String GROUP_ID = "groupID";
    public static final String IS_GROUP = "isGroup";
    public static final String GROUP_NAME = "groupName";
    public static final String STATUS = "status";
    public static final String POSITION = "position";
    public static final String MsgIDs = "msgIDs";
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
