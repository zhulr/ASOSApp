package com.asosapp.phone.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ASOS_zhulr on 2015/12/10.
 */
public class UserInfo {

    private String user_id;
    private static UserInfo userInfo;
    private boolean isLogin;
    private String user_name;//用户姓名
    private String user_nickname;//用户昵称
    private String user_phone;//用户电话号码
    private String user_type;//普通用户和VIP
    private boolean is_push;//是否接收推送
    public static UserInfo instance() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    /**
     * 退出登录
     */
    public void logOut(Context context) {
        this.isLogin = false;
        this.user_id = "";
        this.user_name = "";
        this.user_nickname = "";
        this.user_phone = "";
        this.user_type = "";
        this.is_push = true;

        //保存到配置文件
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.putString("user_id", user_id);
        editor.putString("user_name",user_name);
        editor.putString("user_nickname",user_nickname);
        editor.putString("user_phone",user_phone);
        editor.putString("user_type",user_type);
        editor.putBoolean("is_push", is_push);
        editor.commit();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Boolean getIs_push() {
        return is_push;
    }

    public void setIs_push(Boolean is_push) {
        this.is_push = is_push;
    }

}
